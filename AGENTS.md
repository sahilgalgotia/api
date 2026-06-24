# AGENTS.md

## Cursor Cloud specific instructions

### Project: Sarvika (GLSOS) — Android (Java)

A multilingual, region-aware "Global Life Services OS" Android app. Core layers:

- `app/src/main/java/com/sarvika/glsos/core/` — pure-Java engine (no Android deps),
  unit-testable: `SarvikaEngine` (language detection + geo/region + domain
  localization), `Domain`, `Language`, `Region`, `RegionRepository`, `Session`.
- `app/src/main/java/com/sarvika/glsos/ui/` — `LoginActivity` (launcher),
  `DashboardActivity`, `ServiceActivity`.
- Localized resources in `res/values` (en), `res/values-hi` (Hindi),
  `res/values-ta` (Tamil).

### Build / test / lint / run

Uses the Gradle wrapper (`./gradlew`). Requires a JDK (17+) and the Android SDK.
- Build debug APK: `./gradlew :app:assembleDebug` → `app/build/outputs/apk/debug/app-debug.apk`
- Unit + Robolectric tests: `./gradlew :app:testDebugUnitTest`
- Lint: `./gradlew :app:lintDebug`

### Non-obvious caveats

- The Android SDK is **not** part of the Gradle wrapper. It must be installed
  separately (cmdline-tools + `platforms;android-34` + `build-tools;34.0.0`) and
  pointed to via `local.properties` (`sdk.dir=...`) or `ANDROID_SDK_ROOT`.
  `local.properties` is intentionally gitignored.
- This environment has **no KVM**, so a hardware-accelerated Android emulator is
  not available. To verify UI without an emulator, the test suite uses
  **Robolectric** (`@Config(sdk=33)`) which runs Activities headlessly on the JVM.
- `ScreenshotTest` renders each screen to real PNGs via Robolectric
  `@GraphicsMode(NATIVE)`; override the output dir with
  `-DscreenshotDir=<path>` (defaults to `build/screenshots`). Useful for visual
  verification in CI / cloud agents where no emulator exists.
- `app_name` is marked `translatable="false"` (brand name) to keep lint clean
  across locales.
