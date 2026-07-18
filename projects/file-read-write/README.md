File Read/Write (remote I/O sample)

Summary
- Small Android/Kotlin exercise that reads from and writes to a remote HTTP(S) endpoint on a background thread and updates the UI on the main thread.
- This folder is a source-only snapshot intended for quick code review. To run it, drop the files into a minimal Android Studio wrapper app (see Quickstart below).

What this demonstrates
- Wiring simple UI actions to background work and back to the UI thread.
- Basic remote I/O patterns (one "read" request and one "write" request).
- Error surface to the UI and basic state handling.

Files in this folder
- MainActivity.kt — Wires buttons/inputs to background read/write tasks and updates the UI.
- Project6Read.kt — Performs a remote “read” call on a background thread and returns the response text.
- Project6Write.kt — Performs a remote “write” call on a background thread.
- activity_main.xml — Minimal layout used by MainActivity.

Endpoint placeholders and configuration (read this first)
- Treat the endpoint strings used by this example as placeholders. This repository does not ship or rely on any live institutional servers.
- Where to change them:
  - Project6Read.kt: look for a constant like ENDPOINT and set it to your read URL (for example, https://example.com/read).
  - Project6Write.kt: look for a constant like DEFAULT_ENDPOINT and set it to your write URL (for example, https://example.com/write).
- If your local copy still references any course or university URLs (from historical coursework), replace them with endpoints you control before running.
- Production tip (optional): In a real app you would not hard-code these. You’d inject them (e.g., via BuildConfig, DI, or manifest meta-data). This snapshot keeps it simple and uses constants.

Quickstart (source-only)
1) Create a minimal Android Studio project (Empty Views Activity). Kotlin, minSdk that suits your emulator is fine.
2) Add Internet permission to your app/src/main/AndroidManifest.xml:
   - <uses-permission android:name="android.permission.INTERNET" />
3) Copy the Kotlin files from this folder into your module’s package and update the package declarations at the top of the files to match your app’s package.
4) Copy activity_main.xml into app/src/main/res/layout/ (or paste the layout contents into an existing layout) and ensure MainActivity calls setContentView(R.layout.activity_main).
5) Configure the endpoints in Project6Read.kt and Project6Write.kt (see “Endpoint placeholders and configuration”).
6) Build and run on an emulator or device. Tap the buttons to trigger read/write. Without a reachable server, network actions will fail gracefully (you can still inspect UI wiring and error handling).

Local testing tips
- Emulator host mapping: On the Android emulator, 10.0.2.2 points to your development machine. Example placeholder: http://10.0.2.2:8080/read
- Cleartext HTTP: If you use http:// for local testing on Android 9+, enable cleartext traffic (via a network security config) or use https://.
- Any simple HTTP echo service you control can stand in for testing. For read, return a 200 OK with a small text body. For write, accept a POST and return 200 OK.

Notes and limitations
- Uses legacy background threading (e.g., AsyncTask-style patterns) for teaching purposes. Modern apps would use Kotlin coroutines, WorkManager, or a reactive approach with structured concurrency.
- This is a source snapshot, not a full Gradle module. You’ll need to wire it into an Android Studio wrapper app as outlined above.
- Class names carry assignment-flavored naming (Project6Read/Project6Write). They’re kept as-is to preserve the original code snapshot; feel free to rename when you integrate.

Ethical use and attribution
- Any course or university endpoints referenced in historical code are not owned or operated by me and should not be used. Replace them with endpoints you control.
- No third-party services are required to run this example; you can test locally as described above.
