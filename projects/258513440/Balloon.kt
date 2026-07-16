package com.example.project7

/**
 * Mutable circular balloon defined by its center coordinates and radius.
 */
class Balloon(
    private var x: Int,
    private var y: Int,
    private var radius: Int,
) {
    var isPopped: Boolean = false
        private set

    fun updateX(newX: String) {
        x = newX.toInt()
    }

    fun updateY(newY: String) {
        y = newY.toInt()
    }

    fun updateRadius(newRadius: String) {
        radius = newRadius.toInt()
    }

    fun getXPosition(): Int = x

    fun getYPosition(): Int = y

    fun getBalloonRadius(): Int = radius

    fun popBalloon() {
        isPopped = true
    }

    fun containsPoint(px: Float, py: Float): Boolean {
        val dx = px - x
        val dy = py - y
        return dx * dx + dy * dy <= radius * radius
    }

    override fun toString(): String = "Balloon coordinates: ($x, $y) with radius $radius"
}
