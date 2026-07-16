package com.example.testlayout

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

/** Loads and displays leaderboard entries stored in Firebase Realtime Database. */
class LeaderboardManager(private val context: Context) {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun displayLeaderboard(recyclerView: RecyclerView) {
        database.child(LEADERBOARD_PATH)
            .orderByChild(RUN_TIME_FIELD)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val entries = snapshot.children.mapNotNull { data ->
                        data.getValue(LeaderboardEntry::class.java)
                    }.sortedByDescending { entry ->
                        convertTimeToMillis(entry.runTime)
                    }

                    updateRecyclerView(recyclerView, entries)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Unable to load leaderboard entries.", error.toException())
                }
            })
    }

    private fun updateRecyclerView(
        recyclerView: RecyclerView,
        leaderboardList: List<LeaderboardEntry>
    ) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = LeaderboardAdapter(leaderboardList)
    }

    private fun convertTimeToMillis(time: String): Long {
        return try {
            val formatter = SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone(UTC_TIME_ZONE)
            }
            formatter.parse(time)?.time ?: 0L
        } catch (exception: Exception) {
            Log.e(TAG, "Unable to parse leaderboard runtime: $time", exception)
            0L
        }
    }

    private companion object {
        const val TAG = "LeaderboardManager"
        const val LEADERBOARD_PATH = "leaderboard"
        const val RUN_TIME_FIELD = "runTime"
        const val TIME_FORMAT = "HH:mm:ss"
        const val UTC_TIME_ZONE = "UTC"
    }
}

class LeaderboardAdapter(private val leaderboardList: List<LeaderboardEntry>) :
    RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>() {

    class LeaderboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rankTextView: TextView = itemView.findViewById(R.id.rank)
        val runTimeTextView: TextView = itemView.findViewById(R.id.run_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.leaderboard_item, parent, false)
        return LeaderboardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        val entry = leaderboardList[position]
        holder.rankTextView.text = (position + 1).toString()
        holder.runTimeTextView.text = entry.runTime
    }

    override fun getItemCount(): Int = leaderboardList.size
}
