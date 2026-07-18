Mortgage calculator (Kotlin)

Overview
- A small, self-contained Kotlin example that models a fixed-rate mortgage and computes key figures. The math lives in Mortgage.kt; MainActivity.kt shows a minimal usage example and logs the results.
- Useful to demonstrate clear separation of calculation logic from UI and simple numeric formatting.

What it calculates
- Inputs
  - Principal (loan amount)
  - Annual interest rate (APR, as a percentage)
  - Term (years)
- Derived values (standard fixed-rate, fully amortizing loan)
  - Monthly interest rate r = APR / 12
  - Number of payments n = years × 12
  - Monthly payment (principal + interest): payment = P × r / (1 − (1 + r)^(−n))
    - Note: when APR is 0, this simplifies to payment = P / n
  - Total paid = payment × n
  - Total interest = total paid − P
- The example also formats numbers for readability (e.g., currency), so outputs are easy to scan.

Example results (approximate)
- Principal $300,000, APR 6.0%, 30 years
  - Monthly payment ≈ $1,798.65
  - Total paid over term ≈ $647,514
  - Total interest ≈ $347,514
- Principal $200,000, APR 5.0%, 30 years
  - Monthly payment ≈ $1,073.64
  - Total paid over term ≈ $386,510
  - Total interest ≈ $186,510
- Notes
  - Values shown are rounded and may differ slightly depending on rounding mode and formatting.

Quickstart (source-only snapshot)
- These are source files, not a standalone Android Studio project. To run them:
  1) In Android Studio, create a new project (Empty Views Activity, Kotlin).
  2) Copy Mortgage.kt and MainActivity.kt from this folder into app/src/main/java/<your/package>/ in your new project.
  3) Update the package declaration at the top of both files to match your app’s package.
  4) Replace the template MainActivity with this one (or call the Mortgage calculations from your own activity).
  5) Build and run on an emulator or device. Open Logcat to see the computed results, or wire the values to TextViews if you want an on-screen display.
- Reuse tip: Mortgage.kt is plain Kotlin and can be reused in other modules or unit tests.

Files in this folder
- Mortgage.kt — Encapsulates the mortgage math and basic formatting of results.
- MainActivity.kt — Minimal Android activity that constructs a Mortgage and prints/logs results.

Notes and limitations
- Source-only: no Gradle files, manifest, or layouts are included here.
- The example focuses on principal-and-interest for a fixed-rate loan. It does not model taxes, insurance, PMI, fees, adjustable rates, extra payments, or a full amortization schedule.
- Currency and percent formatting may vary by device locale.
