package com.example.testlayout

class ClockUtility {
    companion object {
        /**
         * Converts a clock value in `mm:ss` or `hh:mm:ss` format to milliseconds.
         *
         * Invalid or unsupported values return `0`.
         */
        fun milliseconds(clock: String): Long {
            val components = clock.split(':').map { it.toIntOrNull() ?: return 0L }

            return when (components.size) {
                3 -> components[0].toLong() * 60 * 60 * 1_000 +
                    components[1].toLong() * 60 * 1_000 +
                    components[2].toLong() * 1_000

                2 -> components[0].toLong() * 60 * 1_000 +
                    components[1].toLong() * 1_000

                else -> 0L
            }
        }
    }
}
