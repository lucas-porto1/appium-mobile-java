package io.github.lucasporto.appium.config;

import io.github.cdimascio.dotenv.Dotenv;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;

public record TestEnvironment(
    URI appiumServerUri,
    Path appPath,
    String deviceName,
    Optional<String> deviceUdid,
    Duration elementTimeout,
    Duration newCommandTimeout) {

  public static TestEnvironment load() {
    var dotenv = Dotenv.configure().ignoreIfMissing().load();
    var appPath = resolveAppPath(require(dotenv, "APP_PATH"));

    if (!Files.isRegularFile(appPath)) {
      throw new IllegalStateException(
          "Application not found at " + appPath + ". Run `npm run setup:app` first.");
    }

    return new TestEnvironment(
        URI.create(value(dotenv, "APPIUM_SERVER_URL", "http://127.0.0.1:4723")),
        appPath,
        value(dotenv, "ANDROID_DEVICE_NAME", "Android Emulator"),
        optionalValue(dotenv, "ANDROID_UDID"),
        seconds(dotenv, "ELEMENT_TIMEOUT_SECONDS", 15),
        seconds(dotenv, "NEW_COMMAND_TIMEOUT_SECONDS", 120));
  }

  private static Path resolveAppPath(String configuredPath) {
    var path = Path.of(configuredPath);
    return (path.isAbsolute() ? path : Path.of("").toAbsolutePath().resolve(path))
        .normalize()
        .toAbsolutePath();
  }

  private static String require(Dotenv dotenv, String name) {
    return optionalValue(dotenv, name)
        .orElseThrow(
            () -> new IllegalStateException("Missing required environment variable: " + name));
  }

  private static String value(Dotenv dotenv, String name, String defaultValue) {
    return optionalValue(dotenv, name).orElse(defaultValue);
  }

  private static Optional<String> optionalValue(Dotenv dotenv, String name) {
    var environmentValue = System.getenv(name);
    var configuredValue = environmentValue != null ? environmentValue : dotenv.get(name);
    return Optional.ofNullable(configuredValue).map(String::trim).filter(value -> !value.isEmpty());
  }

  private static Duration seconds(Dotenv dotenv, String name, long defaultValue) {
    var configuredValue = value(dotenv, name, Long.toString(defaultValue));

    try {
      var seconds = Long.parseLong(configuredValue);
      if (seconds <= 0) {
        throw new NumberFormatException("value must be positive");
      }
      return Duration.ofSeconds(seconds);
    } catch (NumberFormatException exception) {
      throw new IllegalStateException(name + " must be a positive integer.", exception);
    }
  }
}
