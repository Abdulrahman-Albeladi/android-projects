package com.example.project7

class Balloons {
    private val balloonCollection = mutableListOf<Balloon>()

    fun add(balloon: Balloon) {
        balloonCollection.add(balloon)
    }

    fun removePoppedBalloons(): Boolean = balloonCollection.removeAll { it.isPopped }

    fun countRemaining(): Int = balloonCollection.count { !it.isPopped }

    fun listAllBalloons(): List<Balloon> = balloonCollection.toList()
}
