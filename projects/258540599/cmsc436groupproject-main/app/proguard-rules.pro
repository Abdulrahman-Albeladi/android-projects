# Project-specific ProGuard rules.
# Configure applied rule files through the Android build configuration.
#
# Documentation:
# https://developer.android.com/build/shrink-code

# For WebView JavaScript interfaces, preserve the annotated interface class:
#
# -keepclassmembers class com.example.WebAppInterface {
#     public *;
# }

# Preserve source locations in obfuscated stack traces when needed:
#
# -keepattributes SourceFile,LineNumberTable
# -renamesourcefileattribute SourceFile
