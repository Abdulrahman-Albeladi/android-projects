package com.example.testlayout

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONObject
import java.time.LocalDate
import java.util.Timer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class RunFragment : Fragment() {

    private lateinit var startStop: Button
    private lateinit var stopwatchTextView: TextView
    private lateinit var headerTextView: TextView
    private lateinit var speedTextView: TextView
    private lateinit var executor: ExecutorService
    private var stopwatchTask: Future<*>? = null

    private var clockStarted = false
    private var runEnded = false

    private lateinit var hostActivity: Activity
    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null
    private val sensorEventListener: sel = sel()

    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var myLocationCallback: MyLocationCallback
    private var startLocation: Location? = null
    private var endLocation: Location? = null

    private lateinit var database: DatabaseReference
    private lateinit var stopwatchRunnable: StopwatchRunnable
    private var logTimer: Timer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_run, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance().reference
        hostActivity = requireActivity()

        startStop = view.findViewById(R.id.run_start_stop_button)
        stopwatchTextView = view.findViewById(R.id.run_stopwatch)
        headerTextView = view.findViewById(R.id.run_header)
        speedTextView = view.findViewById(R.id.run_speed)

        sensorManager = hostActivity.getSystemService(Activity.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        sensor?.let {
            sensorManager.registerListener(
                sensorEventListener,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        locationClient = LocationServices.getFusedLocationProviderClient(hostActivity)
        locationRequest = LocationRequest.Builder(100)
            .setMaxUpdateAgeMillis(1000)
            .build()
        myLocationCallback = MyLocationCallback()
        requestLocationUpdatesIfPermitted()

        startStopwatchTask()

        startStop.setOnClickListener {
            if (!clockStarted) {
                startRun()
            } else {
                stopRun()
            }
        }

        stopwatchTextView.setOnLongClickListener {
            resetAndStoreRun()
            true
        }

        logTimer = Timer().apply {
            schedule(LogTask(sensorEventListener), 0, 5000)
        }
    }

    private fun startRun() {
        Log.i(TAG, "Stopwatch started")
        stopwatchRunnable.resume()
        startStop.text = "STOP"
        headerTextView.text = "Running"
        clockStarted = true
        runEnded = false
        startLocation = null

        getCurrentLocation { location ->
            startLocation = location
        }
    }

    private fun stopRun() {
        Log.i(TAG, "Stopwatch stopped")
        stopwatchRunnable.pause()
        startStop.text = "START"
        headerTextView.text = "Stopped"
        clockStarted = false
        runEnded = true

        submitLongestRunTime(stopwatchRunnable.totalTime)
    }

    private fun resetAndStoreRun() {
        endLocation = null
        getCurrentLocation { location ->
            endLocation = location
            storeLocalPreferences(hostActivity, stopwatchRunnable)
            runEnded = false
        }

        if (!hasLocationPermission()) {
            storeLocalPreferences(hostActivity, stopwatchRunnable)
            runEnded = false
        }

        stopwatchTask?.cancel(true)
        executor.shutdown()
        stopwatchTextView.text = "00:00:00"
        startStopwatchTask()
    }

    private fun startStopwatchTask() {
        executor = Executors.newSingleThreadExecutor()
        stopwatchRunnable = StopwatchRunnable(this, stopwatchTextView)
        stopwatchTask = executor.submit(stopwatchRunnable)
    }

    fun storeLocalPreferences(context: Context, stopwatchRunnable: StopwatchRunnable) {
        val run = JSONObject().apply {
            put("date", LocalDate.now().toString())

            val distance = startLocation?.let { start ->
                endLocation?.let { end -> start.distanceTo(end) }
            }

            if (distance != null) {
                put("dist", distance)
            } else {
                Toast.makeText(context, "Invalid location data", Toast.LENGTH_SHORT).show()
                put("dist", 0.0f)
            }

            put("run time", stopwatchRunnable.totalTime)
            put("average accel", "${sensorEventListener.avgVelocity}mph")
        }

        val preferences: SharedPreferences = context.getSharedPreferences(
            "${context.packageName}_preferences",
            Context.MODE_PRIVATE
        )
        preferences.edit()
            .putString("run ${StopwatchRunnable.totalRuns + 1}", run.toString())
            .apply()

        Log.w(TAG, run.toString())
    }

    private fun submitLongestRunTime(runTime: String) {
        val leaderboardEntry = LeaderboardEntry(runTime)
        database.child("leaderboard").push().setValue(leaderboardEntry)
            .addOnSuccessListener {
                Log.w(TAG, "Data successfully written to Firebase: $runTime")
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to write data to Firebase", exception)
            }
    }

    private fun requestLocationUpdatesIfPermitted() {
        if (!hasLocationPermission()) {
            ActivityCompat.requestPermissions(
                hostActivity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        locationClient.requestLocationUpdates(
            locationRequest,
            myLocationCallback,
            Looper.getMainLooper()
        )
    }

    private fun getCurrentLocation(onLocationAvailable: (Location?) -> Unit) {
        if (!hasLocationPermission()) {
            onLocationAvailable(null)
            return
        }

        locationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                if (location == null) {
                    Log.i(TAG, "Could not get current location")
                }
                onLocationAvailable(location)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Unable to get current location", exception)
                onLocationAvailable(null)
            }
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            hostActivity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            hostActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @Deprecated("Deprecated in Android API")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && hasLocationPermission()) {
            requestLocationUpdatesIfPermitted()
        }
    }

    override fun onDestroyView() {
        sensorManager.unregisterListener(sensorEventListener)
        if (::locationClient.isInitialized && ::myLocationCallback.isInitialized) {
            locationClient.removeLocationUpdates(myLocationCallback)
        }
        logTimer?.cancel()
        stopwatchTask?.cancel(true)
        if (::executor.isInitialized) {
            executor.shutdown()
        }
        super.onDestroyView()
    }

    inner class sel : SensorEventListener {
        private val linearVelocitySamples = mutableListOf<Double>()
        private var initialVelocity = 0.0
        var avgVelocity = 0.0
            private set
        private var velocity = 0.0

        fun logVelocity() {
            Log.i(TAG, "speed = $velocity")
        }

        fun logAvgVelocity() {
            Log.i(TAG, "avg speed = $avgVelocity")
        }

        override fun onSensorChanged(event: SensorEvent?) {
            val activeSensor = sensor
            if (event == null || event.sensor != activeSensor) {
                return
            }

            val acceleration = event.values
            val magnitude = Math.sqrt(
                acceleration[0].toDouble() * acceleration[0] +
                    acceleration[1].toDouble() * acceleration[1] +
                    acceleration[2].toDouble() * acceleration[2]
            )

            val timeInterval = 0.001
            velocity = initialVelocity + magnitude * timeInterval
            initialVelocity = velocity
            linearVelocitySamples.add(velocity)
            avgVelocity = linearVelocitySamples.average()

            if (clockStarted) {
                speedTextView.text = String.format("%.2f mph", velocity)
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
    }

    inner class MyLocationCallback : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation ?: run {
                Log.i(TAG, "Location could not be updated")
                return
            }

            if (clockStarted && startLocation == null) {
                startLocation = location
            }
            if (runEnded) {
                endLocation = location
            }
        }
    }

    private companion object {
        const val TAG = "RunFragment"
        const val LOCATION_PERMISSION_REQUEST_CODE = 101
    }
}
