#!/usr/bin/env bash

set -euo pipefail

appium_status_url="${APPIUM_SERVER_URL:-http://127.0.0.1:4723}"
appium_status_url="${appium_status_url%/}/status"

npm run appium:start > appium.log 2>&1 &
appium_pid=$!

cleanup() {
  if kill -0 "$appium_pid" 2>/dev/null; then
    kill "$appium_pid" 2>/dev/null || true
    wait "$appium_pid" 2>/dev/null || true
  fi
}

trap cleanup EXIT

for attempt in $(seq 1 30); do
  if curl --fail --silent "$appium_status_url" > /dev/null; then
    adb logcat -c || true

    test_exit=0
    ./mvnw \
      --batch-mode \
      --no-transfer-progress \
      -Dsurefire.rerunFailingTestsCount=1 \
      test || test_exit=$?

    if grep -R -q '<flaky' target/surefire-reports 2>/dev/null; then
      echo "::warning title=Flaky mobile test detected::At least one test passed only after retry. Review the mobile-test-reports artifact."
    fi

    if [[ "$test_exit" -ne 0 ]]; then
      mkdir -p target/failure-artifacts
      adb logcat -d -v threadtime > target/failure-artifacts/device-logcat.txt 2>&1 || true
    fi

    exit "$test_exit"
  fi

  if ! kill -0 "$appium_pid" 2>/dev/null; then
    echo "Appium stopped before becoming ready."
    cat appium.log
    exit 1
  fi

  if [[ "$attempt" -eq 30 ]]; then
    echo "Appium did not become ready within 30 seconds."
    cat appium.log
    exit 1
  fi

  sleep 1
done
