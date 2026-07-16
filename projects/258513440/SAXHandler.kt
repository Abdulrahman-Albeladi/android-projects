package com.example.project7

import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

class SAXHandler : DefaultHandler() {
    private val balloons = mutableListOf<Balloon>()
    private var currentBalloon: Balloon? = null
    private var currentElement: String? = null
    private val textBuffer = StringBuilder()

    override fun startElement(
        uri: String?,
        localName: String?,
        qName: String?,
        attributes: Attributes?
    ) {
        currentElement = qName
        textBuffer.clear()

        if (qName == "balloon") {
            val x = attributes?.getValue("x")?.toIntOrNull() ?: 0
            val y = attributes?.getValue("y")?.toIntOrNull() ?: 0
            val radius = attributes?.getValue("radius")?.toIntOrNull() ?: 0
            currentBalloon = Balloon(x, y, radius)
        }
    }

    override fun characters(ch: CharArray, start: Int, length: Int) {
        textBuffer.append(ch, start, length)
    }

    override fun endElement(uri: String?, localName: String?, qName: String?) {
        val text = textBuffer.toString().trim()

        currentBalloon?.let { balloon ->
            when (qName) {
                "x" -> balloon.updateX(text)
                "y" -> balloon.updateY(text)
                "radius" -> balloon.updateRadius(text)
                "balloon" -> {
                    balloons.add(balloon)
                    currentBalloon = null
                }
            }
        }

        currentElement = null
        textBuffer.clear()
    }

    fun getBalloons(): List<Balloon> = balloons
}
