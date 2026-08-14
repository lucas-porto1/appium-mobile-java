#!/usr/bin/env bash

set -euo pipefail

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
  if curl --fail --silent http://127.0.0.1:4723/status > /dev/null; then
    ./mvnw --batch-mode --no-transfer-progress test
    exit 0
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
