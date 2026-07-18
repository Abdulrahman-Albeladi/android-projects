Pong game (Android/Kotlin)

A compact, single-activity Pong-style example that separates game state from rendering and input. It uses a custom View for drawing, a simple fixed-rate update loop, and touch input for paddle control. Useful for showing custom drawing, collision checks, timing, and lightweight state management in Kotlin.

What's here
- MainActivity.kt: Orchestrates the game session and lifecycle. Starts/stops the fixed-rate update loop and (in this snapshot) uses SharedPreferences to persist lightweight state such as a best score.
- GameView.kt: Custom View responsible for rendering (Canvas + Paint) and handling touch input. It invalidates itself after each update to trigger redraw.
- Pong.kt: Encapsulates game rules and state (ball, paddles, velocities, scoring, and collisions). The rest of the app calls into this class to advance the simulation.
- GameTimerTask.kt: Posts periodic updates back onto the main thread via a Handler so UI and game state stay in sync.

Controls
- Touch input is handled inside GameView (see onTouchEvent). Drag horizontally to move the player paddle; the view consumes MotionEvent gestures to reposition the paddle accordingly.
- To change input behavior (e.g., restrict control to a screen region or add sensitivity), adjust the mapping logic in GameView’s touch handling.

Update loop (timing)
- A Timer schedules GameTimerTask at a fixed interval. Each tick:
  - Advances game state via the Pong class (e.g., position updates, collision checks, scoring).
  - Requests a redraw by invalidating GameView on the UI thread.
- The loop starts in Activity onResume/onStart and is canceled in onPause/onStop so the game does not run in the background. Adjust these hooks in MainActivity to match your desired lifecycle behavior.

Rendering
- GameView overrides onDraw(Canvas) to draw paddles, ball, and background. Sizes and positions are derived from the current view width/height so it adapts to different devices.
- onSizeChanged is used to recalculate bounds when the view size changes (e.g., rotation).
- Drawing is immediate-mode each frame (clear, then draw current state). No external assets are required; shapes are rendered with Paint.

Quickstart (source-only snapshot)
This repository provides source files, not a standalone Gradle module. To run it:
1) In Android Studio, create a new Empty Activity project (any package name). Minimum SDK 21+ is fine.
2) Copy these four Kotlin files into your app module’s Kotlin source directory (match your package name):
   - MainActivity.kt
   - GameView.kt
   - Pong.kt
   - GameTimerTask.kt
   Update the package declaration at the top of each file to your app’s package if needed.
3) Ensure your Activity sets the content view to the custom view (e.g., in onCreate: setContentView(GameView(this))). Alternatively, inflate a layout that hosts GameView if you wrap it in XML.
4) Build and run on a device or emulator. Drag horizontally to move the paddle and keep the ball in play.

Tuning and configuration
- Frame rate / tick period: Adjust the fixed-delay value used when scheduling GameTimerTask (see where the Timer is created in MainActivity).
- Game feel: Paddle width/height, ball radius, base speed, and speed increments are defined as constants or fields in Pong and/or GameView.
- Scoring and resets: Look in Pong for score updates and ball reset logic when a point is scored.
- Persistence: If a best score or similar value is saved, the keys and behavior are set in MainActivity via SharedPreferences.

Known limitations and notes
- Source-only: These files are intended to be dropped into a minimal wrapper app; no module-level Gradle files are included.
- Timing approach: Uses Timer/Handler for simplicity. For production or smoother rendering, consider Choreographer, View.postOnAnimation, coroutines with a frame clock, or a SurfaceView-based renderer.
- Single-player and features: The example concentrates on fundamentals (input, drawing, collisions). It does not include menus, audio, or complex game states by design.

Why this example
- Demonstrates clear separation of concerns: Activity (lifecycle), View (input + rendering), and model (game rules).
- Shows practical Canvas drawing, basic physics/collision checks, and a straightforward update loop suitable for small games or interactive widgets.