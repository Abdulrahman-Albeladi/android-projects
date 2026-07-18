Color Sequence Game

A compact Simon-style memory game in Kotlin. The app builds an ever-growing sequence of colors; you repeat the sequence by tapping the color buttons. Logic and lightweight persistence live in a small Game class.

What this demonstrates
- Modeling game state with a single source of truth
- Input handling and sequence validation
- Progress persistence using SharedPreferences
- Simple Activity-to-logic separation and UI event wiring

Gameplay overview
- Round flow: each round appends one random color to the internal sequence.
- Player input: each tap is checked against the expected color at the current index of that sequence.
- Outcomes after each tap:
  - Correct so far: advance the input index and wait for the next tap.
  - Round complete: sequence fully matched; a new round starts with the sequence extended by one color.
  - Mistake: the run ends and the sequence resets (progress/score are updated accordingly).
- Scoring: typically the sequence length you successfully matched; some UIs also show a running best (longest streak) for the session.

Persistence
- Progress values (for example, current round length and/or best streak) are stored in app-private SharedPreferences.
- On launch, saved values are read so progress can survive process restarts.
- Clearing the app’s storage resets any saved progress.

Project files
- Game.kt: core rules, sequence generation, input validation, and small persistence reads/writes.
- MainActivity.kt: wires button taps to the Game instance and updates on-screen UI.
- activity_main.xml: layout backing the screen; defines the color buttons and any labels referenced by MainActivity.

Quickstart (source-only snapshot)
This repository provides source files, not a full Gradle project. To try it quickly:
1) In Android Studio, create a new Empty Views Activity project (Kotlin).
2) Copy projects/color-sequence-game/Game.kt into app/src/main/java/<your.package>/.
3) Copy projects/color-sequence-game/MainActivity.kt into the same package (or adjust its package name to match your project).
4) Copy projects/color-sequence-game/activity_main.xml into app/src/main/res/layout/ (replace the template layout or update setContentView accordingly).
5) Ensure AndroidManifest.xml points the LAUNCHER activity to this MainActivity if you replaced the default one.
6) Build and run on an emulator or device.

Notes
- No special permissions or external services are required.
- If you change package or resource names, update references in MainActivity.kt accordingly.

Known limitations
- Minimal UI/animations; focus is on core state and input handling.
- No unit tests included in this snapshot.
- Uses SharedPreferences directly for simplicity; larger apps might use DataStore or a database and ViewModel state.

Ideas to extend
- Add visual playback of the sequence and tap feedback animations/sounds.
- Make difficulty adjustable (e.g., playback speed, number of colors, mistake tolerance).
- Add explicit reset and high-score screens, or cloud backup of best scores.
- Port the UI to Jetpack Compose for a modern declarative approach.
