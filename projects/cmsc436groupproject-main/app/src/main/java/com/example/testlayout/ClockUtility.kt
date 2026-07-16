package com.example.testlayout

/** Utilities for converting colon-delimited clock values to milliseconds. */
class ClockUtility {
    companion object {
        /**
         * Converts a clock value in `mm:ss` or `hh:mm:ss` format to milliseconds.
         *
         * Returns `0` when the input cannot be parsed or does not contain two or three fields.
         */
        fun milliseconds(clock: String): Long {
            val parts = clock.split(":")

            return try {
                when (parts.size) {
                    3 -> {
                        val hours = parts[0].toLong()
                        val minutes = parts[1].toLong()
                        val seconds = parts[2].toLong()
                        hours * 60 * 60 * 1_000 + minutes * 60 * 1_000 + seconds * 1_000
                    }

                    2 -> {
                        val minutes = parts[0].toLong()
                        val seconds = parts[1].toLong()
                        minutes * 60 * 1_000 + seconds * 1_000
                    }

                    else -> 0L
                }
            } catch (_: NumberFormatException) {
                0L
            }
        }
    }
}
