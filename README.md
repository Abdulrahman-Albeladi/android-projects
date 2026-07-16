# android-mobile-projects

Recovered Android coursework and group-project source from CMSC436 materials. This repository preserves publish-eligible implementation files while separating incomplete source-only exercises from the Gradle-based Android application.

## Contents

| Project | Location | Summary | Build status |
|---|---|---|---|
| Mortgage calculator | `projects/257922665` | Kotlin activity and mortgage-domain logic. | Source-only recovery; Android build files were not recovered. |
| Encryption utility | `projects/257463730` | Kotlin activities, encryption logic, and XML resources for a two-screen utility. | Source-only recovery; Android build files were not recovered. |
| Game | `projects/257832012` | Kotlin game and activity with a main layout. | Source-only recovery; Android build files were not recovered. |
| Pong | `projects/257972301` | Kotlin Pong implementation with a custom view and timer task. | Source-only recovery; Android build files were not recovered. |
| Read/write project | `projects/258157347` | Kotlin activity with separate read and write components. | Source-only recovery; Android build files were not recovered. |
| Balloons | `projects/258513440` | Kotlin balloon game/view code with SAX XML parsing support. | Source-only recovery; Android build files were not recovered. |
| Running/group application | `projects/258540599/cmsc436groupproject-main` | Gradle Android application with run tracking, local run display, groups, settings, location, accelerometer, and leaderboard-related components. | Gradle project recovered; not validated in this repository. |

## Repository layout

```text
projects/
├── 257922665/                         # Mortgage calculator source
├── 257463730/                         # Encryption utility source and resources
├── 257832012/                         # Game source and layout
├── 257972301/                         # Pong source
├── 258157347/                         # Read/write source and layout
├── 258513440/                         # Balloons source
├── 258540599/
│   └── cmsc436groupproject-main/       # Gradle-based running/group application
└── cmsc436groupproject-main/           # Recovered duplicate of the Gradle project
```

The Gradle project appears twice in the recovered file set: once under `projects/258540599/` and once directly under `projects/`. The copy under `projects/258540599/cmsc436groupproject-main` is the indexed copy because the associated recovery metadata is located in `projects/258540599`. The duplicate should not be treated as a separate application.

## Setup

### Source-only projects

The first six projects contain selected Kotlin, Java, and/or Android resource files, but no recovered Gradle wrapper, module build file, manifest, or complete Android project structure. They are useful as source examples but cannot be assembled directly from this repository as recovered.

To make one independently buildable, create a new Android project with a compatible Kotlin/Android Gradle configuration, place the recovered classes and resources in the appropriate module directories, provide an `AndroidManifest.xml`, and resolve any API or resource dependencies. This reconstruction work has not been performed here.

### Running/group application

The Gradle-based application includes a wrapper, top-level and app build scripts, version catalog, manifest, resources, and source files. From its project directory:

```bash
cd projects/258540599/cmsc436groupproject-main
./gradlew assembleDebug
```

On Windows:

```bat
gradlew.bat assembleDebug
```

The repository includes `local.properties.example`; copy it to `local.properties` and set `sdk.dir` for the local Android SDK installation. Do not commit `local.properties`.

A `google-services.json.example` file is included instead of a real Firebase/Google services configuration. If the Gradle configuration or enabled application features require Google services, create a project-specific configuration file and keep it outside version control unless it is explicitly safe to publish.

## Validation status

No build, unit test, instrumentation test, emulator test, device test, or functional test result was supplied with the recovered files. The presence of `ExampleUnitTest.kt` and `ExampleInstrumentedTest.kt` in the Gradle project indicates test source sets, not that tests have passed.

Recommended commands are provided below as commands to run locally; they are not records of completed validation.

## Private-data and service requirements

- No real `local.properties` file or real `google-services.json` file is included in the recovered project.
- The group application includes `LocationService.java`, `Accelerometer.kt`, and run-related UI classes. Running location- or sensor-dependent features may require a physical device, runtime permission approval, and appropriately configured device services.
- `LeaderboardManager.kt`, `LeaderboardEntry.kt`, `GroupFragment.kt`, and the Google services configuration example indicate that leaderboard or group functionality may depend on external service configuration. The recovered files alone do not establish the service backend, credentials, security rules, or data model.
- Do not use personal location histories, account credentials, API keys, or production service configurations as sample data. Use emulator locations, test accounts, and non-sensitive development configurations.

## Limitations

- Several projects are partial source recoveries rather than complete Android Studio projects.
- Original Android Gradle Plugin, Kotlin, compile SDK, and target-device assumptions are not documented for the source-only projects.
- The repository does not include a verified dependency lock, CI configuration, emulator configuration, screenshots, release artifacts, or test results.
- Names such as `Project6Read` and `Project6Write` are retained from the recovered source and do not describe a complete standalone product.
- The duplicate Gradle project should be consolidated or removed only after a file-level comparison confirms that no recovery differences are lost.
- No claims are made about correctness, security properties, encryption strength, background-location behavior, leaderboard availability, or compatibility with current Android releases.

## Provenance and attribution

These files were recovered from CMSC436 university/course materials. The repository removes assignment and grading context from the project presentation, but preserves the demonstrated implementation structure and recovered filenames where practical. The running application is stored in a directory named `cmsc436groupproject-main`, which indicates group-project provenance; the recovered files do not identify individual contributions or provide an authorship breakdown. No ownership, sole-authorship, or test-completion claim is made beyond the available materials.

## Maintenance notes

Before publishing changes or creating releases:

1. Keep SDK paths, service configuration, keys, and account-specific files out of version control.
2. Treat the source-only directories as examples until each has a reproducible Android project configuration.
3. Run the Gradle project’s build and tests in a supported Android SDK environment.
4. Review location permissions, service configuration, and data handling before enabling run tracking or leaderboard features.
5. Compare the two recovered Gradle-project copies before deleting either copy.
