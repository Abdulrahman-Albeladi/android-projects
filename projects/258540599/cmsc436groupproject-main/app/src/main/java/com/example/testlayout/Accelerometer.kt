package com.example.testlayout

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class Accelerometer(context: Context) {

    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
    private val sensorEventListener = LinearAccelerationListener()

    init {
        sensorManager.registerListener(
            sensorEventListener,
            sensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    fun getSensorManager(): SensorManager = sensorManager

    private inner class LinearAccelerationListener : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            val accelerationSensor = sensor ?: return
            if (event?.sensor != accelerationSensor || event.values.size < 3) {
                return
            }

            val x = event.values[0].toDouble()
            val y = event.values[1].toDouble()
            val z = event.values[2].toDouble()
            val magnitude = Math.sqrt(x * x + y * y + z * z)

            Log.i(LOG_TAG, magnitude.toString())
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
    }

    private companion object {
        const val LOG_TAG = "RunFragment"
    }
}
