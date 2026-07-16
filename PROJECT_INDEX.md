# Project index

## 257922665 — Mortgage calculator

- **Path:** `projects/257922665`
- **Recovered files:** `MainActivity.kt`, `Mortgage.kt`, `metadata.yml`
- **Observed implementation:** Kotlin activity code and a separate mortgage-related model or calculation class.
- **Setup:** Not independently buildable as recovered. No manifest, resource files, Gradle build scripts, or wrapper are present in the supplied file set.
- **Validation status:** Not validated. No test files or executed test results were supplied.
- **Private-data requirements:** None evident from the recovered filenames.
- **Limitations:** Calculation behavior, input validation, formatting, and Android API compatibility require source review and a reconstructed Android project before publication as a runnable app.
- **Provenance:** Recovered CMSC436 material; `metadata.yml` is retained with the source recovery.

## 257463730 — Encryption utility

- **Path:** `projects/257463730`
- **Recovered files:** `MainActivity.kt`, `EncryptActivity.kt`, `Encryption.kt`, `activity_main.xml`, `activity_encrypt.xml`, `colors.xml`, `strings.xml`, `themes.xml`, `metadata.yml`
- **Observed implementation:** A Kotlin utility with a main activity, a separate encryption activity, encryption-related logic, and supporting XML resources.
- **Setup:** Not independently buildable as recovered. Module Gradle files, manifest, wrapper, and dependency declarations are absent.
- **Validation status:** Not validated. No test results or security review were supplied.
- **Private-data requirements:** None evident from the recovered filenames. Users should not place real secrets or sensitive plaintext in demonstration builds.
- **Limitations:** The `Encryption.kt` filename alone does not establish the algorithm, key handling, threat model, or suitability for production use. Do not describe this project as providing secure storage or production-grade cryptography without a dedicated review.
- **Provenance:** Recovered CMSC436 material; `metadata.yml` is retained with the source recovery.

## 257832012 — Game

- **Path:** `projects/257832012`
- **Recovered files:** `MainActivity.kt`, `Game.kt`, `activity_main.xml`, `metadata.yml`
- **Observed implementation:** Kotlin activity and game classes with one recovered main layout.
- **Setup:** Not independently buildable as recovered because the Android project configuration and manifest are not present.
- **Validation status:** Not validated. No test files or gameplay verification results were supplied.
- **Private-data requirements:** None evident from the recovered filenames.
- **Limitations:** Rules, controls, rendering behavior, lifecycle handling, and supported Android versions cannot be established from the file list alone.
- **Provenance:** Recovered CMSC436 material; `metadata.yml` is retained with the source recovery.

## 257972301 — Pong

- **Path:** `projects/257972301`
- **Recovered files:** `MainActivity.kt`, `Pong.kt`, `GameView.kt`, `GameTimerTask.kt`, `metadata.yml`
- **Observed implementation:** A Kotlin Pong implementation with an activity, custom game view, and timer-task component.
- **Setup:** Not independently buildable as recovered. No XML resources, manifest, Gradle scripts, or wrapper were supplied for this directory.
- **Validation status:** Not validated. No device or emulator gameplay results were supplied.
- **Private-data requirements:** None evident from the recovered filenames.
- **Limitations:** Timing behavior, thread/lifecycle safety, input behavior, rendering performance, and screen-size handling require verification after reconstruction.
- **Provenance:** Recovered CMSC436 material; `metadata.yml` is retained with the source recovery.

## 258157347 — Read/write project

- **Path:** `projects/258157347`
- **Recovered files:** `MainActivity.kt`, `Project6Read.kt`, `Project6Write.kt`, `activity_main.xml`, `metadata.yml`
- **Observed implementation:** A Kotlin activity with distinct read and write components.
- **Setup:** Not independently buildable as recovered because the Android module configuration, manifest, and wrapper are absent.
- **Validation status:** Not validated. No persistence, permissions, or error-path test results were supplied.
- **Private-data requirements:** The filenames suggest local data access, but the storage location and data type cannot be determined from the file list. Use non-sensitive test data until storage behavior is reviewed.
- **Limitations:** File naming is retained from the recovery. Storage scope, permission requirements, overwrite behavior, encoding, and Android-version compatibility need source-level verification.
- **Provenance:** Recovered CMSC436 material; `metadata.yml` is retained with the source recovery.

## 258513440 — Balloons

- **Path:** `projects/258513440`
- **Recovered files:** `MainActivity.kt`, `Balloons.kt`, `Balloon.kt`, `GameView.kt`, `SAXHandler.kt`, `metadata.yml`
- **Observed implementation:** Kotlin balloon/game classes, a custom view, and a SAX parser handler.
- **Setup:** Not independently buildable as recovered. Android project configuration, manifest, XML resources, and input XML assets were not included in the supplied file set.
- **Validation status:** Not validated. No parser, rendering, or gameplay test results were supplied.
- **Private-data requirements:** No private data is evident. If the SAX handler consumes external XML, use controlled non-sensitive sample XML during reconstruction.
- **Limitations:** The XML schema, source of XML data, parser error handling, and required resources are not established by the recovered file list.
- **Provenance:** Recovered CMSC436 material; `metadata.yml` is retained with the source recovery.

## 258540599 — Running/group application

- **Canonical path:** `projects/258540599/cmsc436groupproject-main`
- **Duplicate recovery path:** `projects/cmsc436groupproject-main`
- **Recovered files:** Gradle wrapper, Gradle Kotlin build scripts, version catalog, Android manifest, resources, Kotlin and Java application sources, and placeholder unit/instrumentation tests.
- **Observed implementation:** An Android application organized around run tracking, locally displayed run data, groups, settings, location services, accelerometer access, timing utilities, distance utilities, logging, and leaderboard-related classes. Its resources include bottom navigation and layouts for run, group, settings, leaderboard item, and local run data screens.
- **Setup:** Copy `local.properties.example` to `local.properties`, configure `sdk.dir`, and use the included Gradle wrapper. If enabled build features require it, supply a non-production Google services configuration based on `app/google-services.json.example`.
- **Validation status:** Not validated. `ExampleUnitTest.kt` and `ExampleInstrumentedTest.kt` are present, but no execution results were supplied. No claim is made that the project currently assembles or that application features work.
- **Private-data requirements:** A real Android SDK path is required locally. Location and accelerometer features may require a compatible device or emulator and runtime permissions. Group and leaderboard behavior may require service/backend configuration not included in the recovery. Real user locations, account credentials, API keys, and production Google services configuration must remain private.
- **Limitations:** Backend availability, authentication, remote data schema, service rules, permission behavior, and compatibility with current Android versions are unverified. The package path `com/example/testlayout` appears to be a development package name and may need replacement before release. The project exists in two recovered locations and should be compared before deduplication.
- **Provenance:** Recovered CMSC436 group-project material. The directory name indicates group-project origin, but individual contribution records and team attribution were not supplied.
