package com.example.testlayout

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import kotlin.math.sqrt

class Accelerometer(context: Context) {

    private val sensorManager: SensorManager = requireNotNull(
        getSystemService(context, Context.SENSOR_SERVICE::class.java)
    ) { "Sensor service is unavailable." }

    private val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
    private val sensorEventListener = AccelerometerListener()

    init {
        sensor?.let {
            sensorManager.registerListener(
                sensorEventListener,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    fun getSensorManager(): SensorManager = sensorManager

    private inner class AccelerometerListener : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            event ?: return
            if (event.sensor != sensor || event.values.size < 3) {
                Log.i(LOG_TAG, "invalid accelerometer values")
                return
            }

            val acceleration = event.values
            val magnitude = sqrt(
                acceleration[0].toDouble() * acceleration[0] +
                    acceleration[1].toDouble() * acceleration[1] +
                    acceleration[2].toDouble() * acceleration[2]
            )

            Log.i(LOG_TAG, magnitude.toString())
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
    }

    private companion object {
        const val LOG_TAG = "RunFragment"
    }
}
