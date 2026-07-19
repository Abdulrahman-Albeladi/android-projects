# Android Projects


Small Android coursework exercises showcasing Kotlin, UI state, basic persistence, and simple game logic.

This repository curates a handful of focused Android/Kotlin exercises I wrote to practice core skills: modeling state, handling input, rendering custom views, and interacting with simple remote endpoints. Each project is intentionally small so an engineer can scan it in minutes and see how I structure code, reason about state transitions, and keep logic testable. These are source-only snapshots; below I describe how to run any one example inside a minimal Android Studio wrapper project. Where original coursework referenced university-hosted endpoints, I’ve documented how to replace them so the code does not rely on third‑party infrastructure.

Technologies: Kotlin · Android

## What this repository demonstrates

- Clean, readable Kotlin with small, focused classes
- UI state management and event handling (buttons, gestures, simple view state machines)
- Custom drawing and simple game loops (render/update on a timer)
- Local persistence with SharedPreferences
- Basic networking and I/O patterns (simple HTTP requests; input validation)
- XML parsing using an event-driven SAX handler
- Separation of UI from core logic (e.g., Encryption.kt, Mortgage.kt, Pong.kt)

## How to run these source-only exercises

These folders are not standalone Gradle modules. To run one:

1) Create a minimal wrapper app
- In Android Studio, create a new Empty Views Activity project (any package name you prefer).
- Ensure you target a recent SDK; no special features are required.

2) Copy the example’s source
- Copy the Kotlin files from the chosen project folder into app/src/main/java/<your.package>.
- Copy any layout XMLs (if present) into app/src/main/res/layout.
- If the example references colors/strings/themes, copy those resource entries as needed.

3) Wire up the launcher activity
- Set the example’s MainActivity (or the activity you choose) as the launcher in AndroidManifest.xml.

4) Add permissions if needed
- For the file-read-write example, add android.permission.INTERNET.

5) Replace any placeholder or course endpoints before running
- Do not send traffic to endpoints you don’t own. Point network examples at a local/mock server or your own test endpoint.

6) Build and run
- Sync Gradle and launch on an emulator or device.

## Projects at a glance

| Project | Location | One‑liner summary |
|---|---|---|
| Encryption Tool | [`projects/encryption-tool`](projects/encryption-tool) | Simple shift‑cipher UI showing input validation, intent extras, and pure Kotlin logic separation. |
| Color Sequence Game | [`projects/color-sequence-game`](projects/color-sequence-game) | Simon‑style memory game demonstrating UI state updates and SharedPreferences for progress. |
| Mortgage Calculator | [`projects/mortgage-calculator`](projects/mortgage-calculator) | Core mortgage math and formatting with a minimal activity wrapper. |
| Pong Game | [`projects/pong-game`](projects/pong-game) | Custom View rendering, timer‑driven update loop, collisions, and scoring. |
| File Read and Write | [`projects/file-read-write`](projects/file-read-write) | Minimal HTTP read/write flow using AsyncTask and basic input handling. |
| Balloon Game | [`projects/balloon-game`](projects/balloon-game) | SAX‑parsed XML drives on‑screen balloons; tap‑to‑pop interaction and drawing. |

## Per‑project summaries and quickstart notes

Encryption Tool
- What it shows: Input validation, passing data via intents, and a pure Kotlin encryption routine (simple character shift).
- Key files: MainActivity.kt, EncryptActivity.kt, Encryption.kt; layouts: activity_main.xml, activity_encrypt.xml; plus colors/strings/themes resources.
- Quickstart: Copy the Kotlin files and the two layouts into your wrapper project; set MainActivity as the launcher.

Color Sequence Game
- What it shows: A compact state machine for a Simon‑like sequence game; persists progress via SharedPreferences.
- Key files: Game.kt (state/logic), MainActivity.kt (UI events), activity_main.xml.
- Quickstart: Copy Game.kt, MainActivity.kt, and the layout; set MainActivity as the launcher.

Mortgage Calculator
- What it shows: Clean separation of financial math (monthly payment, totals) from the activity; examples of formatting.
- Key files: Mortgage.kt (math/formatting), MainActivity.kt (UI stub).
- Quickstart: Copy both Kotlin files; add a simple layout (e.g., inputs + result TextView) or log results from MainActivity to verify output.

Pong Game
- What it shows: A simple game loop (Timer/Handler), custom drawing in a View, collision detection, and scoring.
- Key files: GameView.kt (rendering/input), GameTimerTask.kt (timing), Pong.kt (game rules), MainActivity.kt (setup).
- Quickstart: Copy these four Kotlin files; MainActivity already sets a custom GameView so no XML is strictly required.

File Read and Write
- What it shows: Minimal HTTP GET/POST flows, UI wiring for reading/writing text, and basic input validation; uses AsyncTask for simplicity.
- Key files: MainActivity.kt, Project6Read.kt, Project6Write.kt; layout: activity_main.xml.
- Quickstart: Copy the Kotlin files and the layout; add android.permission.INTERNET; replace any endpoint constants with your own test URLs or a local mock server before running.

Balloon Game
- What it shows: Parsing a small XML with SAX to build objects used by a custom‑drawn mini‑game; touch input and basic animation.
- Key files: MainActivity.kt, GameView.kt, SAXHandler.kt, Balloon.kt, Balloons.kt.
- Quickstart: This example expects a raw XML resource at res/raw/balloons3.xml describing balloons (e.g., a list of items with position/size/color). That file is not included here; create your own minimal XML to match the handler’s expectations, then run.

## Limitations and notes

- Source‑only snapshots: These are not packaged as independent Gradle modules. Use the wrapper‑project approach above.
- Networking demo uses AsyncTask: Preserved for pedagogy. In modern apps I use Kotlin coroutines with Retrofit/OkHttp or WorkManager for deferrable work.
- Missing resource in balloon game: The sample raw XML (res/raw/balloons3.xml) is not included; create a small file aligned to the SAXHandler’s fields (position, size, color) if you want to run it.
- Package names and some identifiers reflect original coursework conventions; feel free to rename when integrating into a wrapper app.

## Ethical use and endpoint disclaimer

- No live institutional or third‑party endpoints should be used from this repository. If you see university‑hosted URLs in comments or string constants, treat them as historical placeholders from the original coursework. Replace all such values with your own local/mock endpoints before running.
- Don’t send traffic to servers you don’t control. Provide your own test infrastructure, or stub out the network calls for demonstration.

## Relevance to roles

- Android/Kotlin engineer: Demonstrates UI event handling, state modeling, custom views, and local persistence in small, readable examples.
- Mobile generalist or full‑stack engineer: Shows how I separate UI from core logic, keep business rules testable, and reason about state transitions.
- Prototyping/game‑mechanics work: Simple loops, collision checks, and drawing are implemented clearly and concisely.

## License and attribution

Use and redistribution are governed by the repository’s LICENSE. Any referenced course servers or endpoints are not owned or operated by me; they appear only as historical context and must be replaced before any execution.
