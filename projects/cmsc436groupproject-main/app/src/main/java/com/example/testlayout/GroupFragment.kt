package com.example.testlayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GroupFragment : Fragment() {
    private lateinit var leaderboardManager: LeaderboardManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view = inflater.inflate(R.layout.fragment_group, container, false)

        leaderboardManager = LeaderboardManager(requireContext())

        val recyclerView = view.findViewById<RecyclerView>(R.id.leaderboard_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        leaderboardManager.displayLeaderboard(recyclerView)

        return view
    }
}
