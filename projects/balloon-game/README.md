Balloon game (SAX + touch)

What this is
- A small Android/Kotlin exercise that parses balloon definitions from an XML resource using a SAX parser, renders them in a custom View, and lets the user interact by tapping balloons.
- This folder contains source files only (no Gradle build files). See “Getting it running” for how to try it inside your own Android Studio project.

Key files
- MainActivity.kt — Loads an XML file from res/raw, parses it via SAX, and makes the resulting balloons available to the view.
- GameView.kt — Custom View responsible for drawing balloons on a Canvas and responding to touch events (e.g., tap to pop via hit-testing).
- Balloon.kt — Simple model for a balloon (e.g., center, radius, color, and popped state/logic).
- Balloons.kt — Convenience container or helper for managing multiple Balloon instances.
- SAXHandler.kt — org.xml.sax.helpers.DefaultHandler implementation that streams the XML and constructs Balloon objects from elements/attributes.

How the SAX parsing works
- SAX is an event-driven, streaming parser. Instead of building a full tree in memory, the parser calls back into a handler as it encounters elements.
- In SAXHandler.kt you’ll find overrides for callbacks such as:
  - startElement(...) — See which tag started and read any attributes (e.g., x, y, radius, color).
  - characters(...) — Capture element text when the XML uses nested tags for values.
  - endElement(...) — Finalize a Balloon when a balloon element closes and add it to a list.
- MainActivity opens the raw resource (Resources.openRawResource) and drives the SAX parse. After parsing, the handler exposes the parsed collection for rendering.

Balloon interaction at a glance
- Rendering: GameView draws each non-popped balloon as a circle (Paint/Canvas).
- Input: On touch, the view hit-tests the touch point against each balloon (distance-to-center ≤ radius). A hit marks the balloon as popped and requests a redraw (invalidate()).
- State: The Balloon model tracks whether it is popped so drawing and input can ignore it afterward.

Known limitation (missing raw resource)
- MainActivity references a raw XML resource (R.raw.balloons3) that is not included in this repository. Add one to run the example:
  - Create: app/src/main/res/raw/balloons3.xml
  - Ensure the XML’s tag/attribute names match what SAXHandler.kt expects. If you prefer a different filename, update the reference in MainActivity accordingly.

Example balloons3.xml (minimal)
- This snippet shows a simple, attribute-based format. Adjust names and structure to match SAXHandler.kt exactly.

  <balloons>
      <balloon x="96" y="220" radius="36" color="#E91E63" />
      <balloon x="220" y="380" radius="42" color="#2196F3" />
      <balloon x="320" y="140" radius="30" color="#4CAF50" />
  </balloons>

Notes
- Coordinates and radius are interpreted in pixels relative to the view’s canvas. You may want to scale values based on device size/density.
- Colors typically use Android’s #RRGGBB or #AARRGGBB format (parsed via Color.parseColor in many examples).
- If your handler uses nested tags instead of attributes (e.g., <x>...</x>), place values inside elements and rely on characters(...) to capture them.

Getting it running (source-only)
- Create a minimal Android Studio project (Empty Activity) targeting a recent SDK.
- Copy the Kotlin files from this folder into your app/src/main/java/<package> to match the package declarations in the files.
- Add the balloons3.xml file under app/src/main/res/raw/ (see the example above).
- Ensure MainActivity is declared as the launcher activity in AndroidManifest.xml.
- Build and run. You should see balloons drawn; tap to pop them.

Troubleshooting
- Resources$NotFoundException for R.raw.balloons3: Confirm the file name is exactly balloons3.xml and it is placed under res/raw/ (not assets/), and that the R.raw reference matches.
- Nothing appears on screen: Check that the balloon coordinates/radii fit within the view’s bounds; try smaller coordinates and moderate radii.
- Colors not applied: Verify valid hex color strings with a leading #.
