package com.example.testlayout

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SettingsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_setting, container, false).also { view ->
            recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView).apply {
                layoutManager = LinearLayoutManager(requireContext())
            }

            childFragmentManager.commit {
                replace(R.id.settings_container, PreferenceFragment.newInstance())
            }
        }
    }

    class PreferenceFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener {

        private lateinit var sharedPreferences: SharedPreferences
        private lateinit var database: DatabaseReference
        private var leaderboardListener: ValueEventListener? = null

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)

            sharedPreferences = preferenceManager.sharedPreferences
            sharedPreferences.registerOnSharedPreferenceChangeListener(this)
            database = FirebaseDatabase.getInstance().reference

            findPreference<Preference>("reset_app")?.setOnPreferenceClickListener {
                resetAppData()
                true
            }

            initializePreferences()
        }

        override fun onDestroy() {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
            removeLeaderboardListener()
            super.onDestroy()
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
            when (key) {
                "background_color" -> {
                    updateBackgroundColor(sharedPreferences.getString(key, "default").orEmpty())
                }

                "data_display" -> {
                    updateDataDisplay(sharedPreferences.getString(key, "top").orEmpty())
                }
            }
        }

        private fun initializePreferences() {
            updateBackgroundColor(sharedPreferences.getString("background_color", "default").orEmpty())
            updateDataDisplay(sharedPreferences.getString("data_display", "top").orEmpty())
        }

        private fun updateBackgroundColor(color: String) {
            showToast("Background color changed to $color")

            val colorInt = when (color) {
                "blue" -> Color.BLUE
                "green" -> Color.GREEN
                "red" -> Color.RED
                else -> Color.WHITE
            }

            view?.setBackgroundColor(colorInt)
        }

        private fun updateDataDisplay(dataDisplay: String) {
            showToast("Data display set to $dataDisplay")
            removeLeaderboardListener()

            leaderboardListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val leaderboard = snapshot.children.mapNotNull { data ->
                        data.getValue(LeaderboardEntry::class.java)
                    }.sortedByDescending { entry ->
                        entry.runTime.toLongOrNull() ?: Long.MIN_VALUE
                    }

                    val displayedEntries = when (dataDisplay) {
                        "top3" -> leaderboard.take(3)
                        "top" -> leaderboard.take(1)
                        else -> leaderboard
                    }

                    settingsRecyclerView()?.adapter = LeaderboardAdapter(displayedEntries)
                }

                override fun onCancelled(error: DatabaseError) {
                    showToast("Error fetching leaderboard: ${error.message}")
                }
            }

            database.child("leaderboard")
                .orderByChild("runTime")
                .addValueEventListener(requireNotNull(leaderboardListener))
        }

        private fun removeLeaderboardListener() {
            leaderboardListener?.let { listener ->
                database.child("leaderboard").removeEventListener(listener)
            }
            leaderboardListener = null
        }

        private fun resetAppData() {
            showToast("App data reset!")
            sharedPreferences.edit().clear().apply()
            clearFirebaseData()
            activity?.recreate()
        }

        private fun clearFirebaseData() {
            database.child("leaderboard").removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast("Leaderboard data cleared")
                } else {
                    showToast("Failed to clear leaderboard data")
                }
            }
        }

        private fun settingsRecyclerView(): RecyclerView? {
            return (parentFragment as? SettingsFragment)?.recyclerView
        }

        private fun showToast(message: String) {
            context?.let { Toast.makeText(it, message, Toast.LENGTH_SHORT).show() }
        }

        companion object {
            fun newInstance() = PreferenceFragment()
        }
    }
}
