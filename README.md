# Appium Mobile - Java Reference

[![Mobile Tests](https://github.com/lucas-porto1/appium-mobile-java/actions/workflows/mobile-tests.yml/badge.svg?branch=master)](https://github.com/lucas-porto1/appium-mobile-java/actions/workflows/mobile-tests.yml)

A modern Android test automation reference project using Appium 3, Java, JUnit, Maven, and the UiAutomator2 driver.

The project uses Appium's official Android ApiDemos application to demonstrate native navigation, alerts, form controls, gestures, explicit waits, failure artifacts, test reports, environment configuration, and Android emulator execution in GitHub Actions.

## Design principles

- **Keep tests readable:** scenarios and assertions remain visible in test classes.
- **Use Screen Objects with purpose:** screens own selectors and user interactions without hiding test intent behind a generic DSL.
- **Create one driver per test:** every scenario starts independently and always closes its session.
- **Prefer explicit waits:** synchronization is based on observable UI state instead of fixed sleeps.
- **Keep configuration outside the code:** device, server, application, and timeout values come from the environment.
- **Use current Appium APIs:** `UiAutomator2Options`, `AppiumBy`, W3C actions, and the root Appium server path replace legacy capabilities and touch APIs.
- **Keep the toolchain reproducible:** Maven Wrapper, npm lockfile, pinned Java/Selenium compatibility, and Dependabot reduce machine-specific behavior.

## Technology stack

- Java 25 LTS
- Appium Server 3.6
- Appium Java Client 10.1
- UiAutomator2 Driver 8.4
- JUnit 6
- Maven 3.9 through Maven Wrapper
- Node.js 24 LTS and npm

## Prerequisites

- Java 25 or 26
- Node.js 24 and npm
- Android Studio or Android SDK command-line tools
- Android SDK Platform Tools, Build Tools 35, and Emulator
- An Android API 35 x86_64 emulator or a connected Android device

The Android Emulator requires hardware virtualization. On Windows, prefer Windows Hypervisor Platform.

## First-time setup

Install the pinned Appium server and Android driver:

```bash
npm ci
```

Download the official ApiDemos APK used by the tests:

```bash
npm run setup:app
```

The command is idempotent: it skips the download when the expected APK is already available and its checksum is valid.

Create the local environment file:

```bash
cp .env.example .env
```

On Windows PowerShell:

```powershell
Copy-Item .env.example .env
```

Create an Android Virtual Device through Android Studio's Device Manager using a Pixel 6 profile, the Google APIs Android 35 x86_64 image, and the name `Appium_API_35`. When using a real device instead, enable USB debugging and update `ANDROID_UDID` in `.env` with the identifier reported by `adb devices`.

## Daily execution

List the available Android Virtual Devices and start the emulator used by the default `.env` configuration:

```bash
emulator -list-avds
emulator -avd Appium_API_35
```

On Windows PowerShell, the explicit SDK path also works when `emulator` is not available directly in `PATH`:

```powershell
& "$env:ANDROID_HOME\emulator\emulator.exe" -list-avds
& "$env:ANDROID_HOME\emulator\emulator.exe" -avd Appium_API_35
```

Keep the emulator open. In another terminal, wait for Android to finish booting and confirm that the device status is `device` rather than `offline`:

```bash
adb devices
npm run appium:doctor
```

The default local configuration expects the device identifier `emulator-5554`. If a different identifier is displayed, update `ANDROID_UDID` in `.env`.

Start Appium in a second terminal and keep it running:

```bash
npm run appium:start
```

Run the tests in a third terminal:

```bash
./mvnw test
```

On Windows PowerShell:

```powershell
.\mvnw.cmd test
```

## Available commands

```bash
npm run setup:app       # download and verify the ApiDemos APK
npm run appium:start    # start the local Appium server
npm run appium:doctor   # validate UiAutomator2 prerequisites
npm run appium:drivers  # list the locally installed Appium drivers
./mvnw test             # execute the mobile tests
./mvnw spotless:apply   # format Java source files
./mvnw verify           # compile, test, check formatting, and generate the HTML report
```

## Troubleshooting

- **`JAVA_HOME not found`:** configure `JAVA_HOME` for Java 25 or 26, then open a new terminal and run `java -version`.
- **No connected Android device:** start the emulator, wait for `adb devices` to show the status `device`, and confirm that its identifier matches `ANDROID_UDID` in `.env`.
- **Unable to connect to Appium:** keep `npm run appium:start` running in a separate terminal and confirm that `APPIUM_SERVER_URL` points to that server.

## Project structure

```text
.
|-- .github/
|   |-- workflows/                  # Android emulator CI execution
|   `-- dependabot.yml              # semiannual dependency updates
|-- scripts/
|   |-- download-demo-app.mjs       # reproducible demo APK download
|   `-- run-mobile-tests-ci.sh      # Appium startup and CI test execution
|-- src/test/java/io/github/lucasporto1/appium/
|   |-- config/                     # validated environment configuration
|   |-- driver/                     # Android driver creation
|   |-- screens/                    # selectors and screen interactions
|   |-- support/                    # test lifecycle and failure diagnostics
|   `-- tests/                      # executable scenarios and assertions
|-- .env.example                    # documented local configuration contract
|-- package.json                    # Appium server and driver toolchain
|-- pom.xml                         # Java dependencies and build configuration
`-- README.md
```

The downloaded APK is stored under `apps/` locally and is intentionally ignored by Git.

## Environment variables

| Variable | Purpose | Example |
| --- | --- | --- |
| `APPIUM_SERVER_URL` | Appium server address | `http://127.0.0.1:4723` |
| `ANDROID_DEVICE_NAME` | Human-readable device name | `Android Emulator` |
| `ANDROID_UDID` | Specific emulator or device identifier | `emulator-5554` |
| `APP_PATH` | Relative or absolute APK path | `apps/ApiDemos-debug.apk` |
| `ELEMENT_TIMEOUT_SECONDS` | Explicit UI wait timeout | `15` |
| `NEW_COMMAND_TIMEOUT_SECONDS` | Appium session inactivity timeout | `120` |

`ANDROID_UDID` is optional when only one compatible device is connected. Environment variables supplied by CI take precedence over `.env` values.

## Test coverage

- Native navigation and Android alert handling
- Text input and checkbox state validation
- W3C touch gesture for drag and drop
- Screenshot and page-source capture under `target/failure-artifacts/` when a test fails

Each test starts a fresh Appium session, so scenarios do not depend on execution order or state left by another test.

## Execution, flakiness, and reports

Tests run sequentially because the default environment provides a single Android emulator. Parallel mobile execution requires a separate device or emulator, UDID, and UiAutomator2 system port for each worker.

Maven Surefire writes machine-readable XML and text results to `target/surefire-reports/`. Running `./mvnw verify` also creates the human-readable report at `target/reports/mobile-test-report.html`.

In CI, a failing test is rerun once to identify intermittent behavior. A test that passes only on the retry produces a visible workflow warning and remains recorded as flaky in the report. A failure that persists after the retry fails the workflow. Test reports are uploaded for every run. Screenshots, page sources, device logcat, and the Appium server log are uploaded when the workflow fails.

## Adding a feature

1. Add a Screen Object under `screens/` when the feature introduces reusable selectors or interactions.
2. Keep native one-step operations visible when an abstraction would only rename an Appium command.
3. Add a focused `*Test.java` class under `tests/` with behavior and assertions.
4. Put only cross-screen synchronization helpers in `BaseScreen`; avoid turning it into a large generic DSL.
5. Prefer accessibility IDs and resource IDs over XPath.
6. Run `./mvnw verify` before submitting changes.

## Continuous integration

GitHub Actions provisions Java and Node.js, installs the pinned Appium toolchain, downloads the demo APK, boots an Android API 35 emulator, starts Appium, and runs the complete suite. Reports and failure diagnostics are uploaded as short-lived artifacts.

No secrets are required for the public ApiDemos application. Real application credentials must be stored in a local `.env` file and GitHub Actions secrets, never committed to the repository.

Failure screenshots, page sources, logs, and reports can contain application data. Review the repository visibility and artifact retention policy before using this template with sensitive environments.
