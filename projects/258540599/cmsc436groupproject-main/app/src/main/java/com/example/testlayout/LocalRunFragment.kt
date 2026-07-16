package com.example.testlayout

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import org.json.JSONObject
import kotlin.math.min

class LocalRunFragment : Fragment() {
    private lateinit var runViews: Array<TextView>
    private lateinit var preferences: SharedPreferences
    private var adView: AdView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.local_running_data, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        runViews = arrayOf(
            view.findViewById(R.id.first_run),
            view.findViewById(R.id.second_run),
            view.findViewById(R.id.third_run),
            view.findViewById(R.id.fourth_run),
        )
        preferences = requireContext().getSharedPreferences(
            "${requireContext().packageName}_preferences",
            Context.MODE_PRIVATE,
        )

        updateRecentRuns()
        buildAd(view)
    }

    private fun updateRecentRuns() {
        val totalRuns = StopwatchRunnable.totalRuns
        Log.d(TAG, "Number of stored runs: $totalRuns")

        val runsToDisplay = min(totalRuns, runViews.size)
        for (offset in 0 until runsToDisplay) {
            val runNumber = totalRuns - offset
            val runJson = preferences.getString("run $runNumber", null) ?: continue

            val run = runCatching { JSONObject(runJson) }.getOrElse {
                Log.w(TAG, "Ignoring malformed run record $runNumber", it)
                continue
            }

            val date = run.optString("date")
            val time = run.optString("run time")
            val distance = run.optString("dist")
            val averageSpeed = run.optString("average accel")

            if (date.isEmpty() || time.isEmpty() || distance.isEmpty() || averageSpeed.isEmpty()) {
                Log.w(TAG, "Ignoring incomplete run record $runNumber")
                continue
            }

            val formattedTime = if (time.length > 3) time.dropLast(3) else time
            val formattedSpeed = averageSpeed.take(3)
            runViews[offset].text = "$date   $formattedTime   ${distance}mi   ${formattedSpeed}mph"
        }
    }

    private fun buildAd(view: View) {
        val banner = AdView(requireContext()).apply {
            setAdSize(AdSize(AdSize.FULL_WIDTH, AdSize.AUTO_HEIGHT))
            adUnitId = TEST_AD_UNIT_ID
        }

        val adRequest = AdRequest.Builder()
            .addKeyword("fitness")
            .addKeyword("workout")
            .build()

        view.findViewById<LinearLayout>(R.id.ad_view).addView(banner)
        banner.loadAd(adRequest)
        adView = banner
    }

    override fun onDestroyView() {
        adView?.destroy()
        adView = null
        super.onDestroyView()
    }

    private companion object {
        private const val TAG = "LocalRunFragment"

        // Google Mobile Ads sample banner unit ID for development builds.
        private const val TEST_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    }
}
