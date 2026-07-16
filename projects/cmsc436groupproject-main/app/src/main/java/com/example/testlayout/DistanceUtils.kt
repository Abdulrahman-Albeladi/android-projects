package com.example.testlayout

import android.content.Context
import androidx.preference.PreferenceManager

object DistanceUtils {
    private const val MILES_TO_KILOMETERS = 1.60934
    private const val DISPLAY_METRICS_KEY = "display_metrics"
    private const val MILES = "miles"
    private const val KILOMETERS = "km"

    fun getPreferredMetric(context: Context): String {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(DISPLAY_METRICS_KEY, MILES) ?: MILES
    }

    fun convertToPreferredMetric(context: Context, distanceInMiles: Double): Double {
        return if (getPreferredMetric(context) == KILOMETERS) {
            distanceInMiles * MILES_TO_KILOMETERS
        } else {
            distanceInMiles
        }
    }

    fun formatDistance(context: Context, distance: Double): String {
        return if (getPreferredMetric(context) == KILOMETERS) {
            String.format("%.2f km", distance)
        } else {
            String.format("%.2f miles", distance)
        }
    }
}
