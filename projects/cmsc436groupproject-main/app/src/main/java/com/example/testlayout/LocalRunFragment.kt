package com.example.testlayout

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import org.json.JSONObject

class LocalRunFragment : Fragment() {
    private lateinit var runs: Array<TextView>
    private lateinit var calendarView: CalendarView
    private lateinit var preferences: SharedPreferences
    private var adView: AdView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.local_running_data, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        runs = arrayOf(
            view.findViewById(R.id.first_run),
            view.findViewById(R.id.second_run),
            view.findViewById(R.id.third_run),
            view.findViewById(R.id.fourth_run),
        )
        calendarView = view.findViewById(R.id.run_calendar)
        preferences = requireContext().getSharedPreferences(
            "${requireContext().packageName}_preferences",
            Context.MODE_PRIVATE,
        )

        updateRecentRuns()
        buildAd(view)
    }

    override fun onDestroyView() {
        adView?.destroy()
        adView = null
        super.onDestroyView()
    }

    private fun updateRecentRuns() {
        val totalRuns = StopwatchRunnable.totalRuns
        Log.d(TAG, "Number of runs: $totalRuns")

        val firstRunIndex = maxOf(1, totalRuns - runs.size + 1)
        for (runIndex in totalRuns downTo firstRunIndex) {
            val storedRun = preferences.getString("run $runIndex", null) ?: continue
            val run = try {
                JSONObject(storedRun)
            } catch (exception: Exception) {
                Log.w(TAG, "Skipping malformed run entry: $runIndex", exception)
                continue
            }

            val date = run.optString("date")
            val time = run.optString("run time")
            val distance = run.optString("dist")
            val averageSpeed = run.optString("average accel").take(4)
            val displayIndex = totalRuns - runIndex

            runs[displayIndex].text = "$date   $time   ${distance}mi   ${averageSpeed}mph"
        }
    }

    private fun buildAd(view: View) {
        val adLayout: LinearLayout = view.findViewById(R.id.ad_view)
        adView = AdView(requireContext()).apply {
            setAdSize(AdSize(AdSize.FULL_WIDTH, AdSize.AUTO_HEIGHT))
            adUnitId = TEST_AD_UNIT_ID
            loadAd(
                AdRequest.Builder()
                    .addKeyword("fitness")
                    .addKeyword("workout")
                    .build(),
            )
        }
        adLayout.addView(adView)
    }

    private companion object {
        const val TAG = "LocalRunFragment"
        const val TEST_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    }
}
