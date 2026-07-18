Encryption Tool (Android, Kotlin)

Summary
- Small, self-contained example of a Caesar-style text cipher implemented in Kotlin.
- Simple two-screen flow: collect input and shift on the first screen, display the encrypted output on the second.
- Focuses on clean separation between UI and logic, basic input validation, and passing data via intents.

What this demonstrates
- Separation of concerns: encryption logic lives in a small, pure Kotlin class that is easy to read and test in isolation.
- Intent extras and activity-to-activity navigation.
- Basic input validation and straightforward XML-based UI.

Files in this folder
- Kotlin
  - MainActivity.kt — captures the plaintext and shift amount; navigates to the result screen.
  - EncryptActivity.kt — receives inputs via intent extras and shows the encrypted result.
  - Encryption.kt — simple Caesar-style cipher logic used by the UI layer.
- Layouts (res/layout)
  - activity_main.xml — input screen layout.
  - activity_encrypt.xml — result screen layout.
- Resources (res/values)
  - strings.xml, colors.xml, themes.xml — basic app resources referenced by the layouts and activities.

Quickstart for reviewers
Option A: Skim the code
- Start with Encryption.kt to see the core logic, then open MainActivity.kt and EncryptActivity.kt for the UI flow.

Option B: Run it in a fresh Android Studio project (source-only snapshot)
- Create a new Android app (Empty Views Activity, Kotlin).
- Match package names: replicate the package declared at the top of these Kotlin files in your project, or adjust the package declarations to your project’s namespace.
- Copy the three Kotlin files into app/src/main/java/... according to their package path.
- Copy the two layout XML files into app/src/main/res/layout/.
- Merge values from strings.xml, colors.xml, and themes.xml into your project’s corresponding files (or replace if starting from a blank template).
- Ensure both activities are declared in AndroidManifest.xml. MainActivity should be the launcher; add an activity entry for EncryptActivity if it is not added automatically.
- Build and run on an emulator or device.

Known limitations and notes
- The Caesar cipher is not cryptographically secure and is intended only for demonstration/learning purposes.
- Character handling specifics (e.g., which characters are shifted) are defined in Encryption.kt; see that file for exact behavior.
- This folder is a source snapshot, not a full Gradle module. It is meant to be dropped into a minimal wrapper project created in Android Studio.
- No tests are included; the pure Kotlin Encryption.kt class is structured so unit tests can be added easily if desired.

Ideas for extension
- Add unit tests around Encryption.kt covering edge cases.
- Improve input UX (error states, limits, accessibility copy, and content descriptions).
- Provide a share/copy action on the result screen.
