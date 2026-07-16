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
import java.util.Locale
import java.util.Timer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class RunFragment : Fragment() {

    private lateinit var startStopButton: Button
    private lateinit var stopwatchTextView: TextView
    private lateinit var headerTextView: TextView
    private lateinit var speedTextView: TextView

    private lateinit var executor: ExecutorService
    private var stopwatchTask: Future<*>? = null
    private lateinit var stopwatchRunnable: StopwatchRunnable

    private lateinit var hostActivity: Activity
    private lateinit var sensorManager: SensorManager
    private var linearAccelerationSensor: Sensor? = null
    private val sensorEventListener = SensorListener()

    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private val locationCallback = RunLocationCallback()

    private var startLocation: Location? = null
    private var endLocation: Location? = null
    private var clockStarted = false
    private var runEnded = false

    private lateinit var database: DatabaseReference
    private var logTimer: Timer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_run, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hostActivity = requireActivity()
        database = FirebaseDatabase.getInstance().reference

        startStopButton = view.findViewById(R.id.run_start_stop_button)
        stopwatchTextView = view.findViewById(R.id.run_stopwatch)
        headerTextView = view.findViewById(R.id.run_header)
        speedTextView = view.findViewById(R.id.run_speed)

        initializeSensor()
        initializeLocationUpdates()
        startStopwatchWorker()

        startStopButton.setOnClickListener {
            if (clockStarted) {
                stopRun()
            } else {
                startRun()
            }
        }

        stopwatchTextView.setOnLongClickListener {
            resetRun()
            true
        }

        logTimer = Timer().apply {
            schedule(LogTask(sensorEventListener), 0, LOG_INTERVAL_MILLIS)
        }
    }

    private fun initializeSensor() {
        sensorManager = hostActivity.getSystemService(Activity.SENSOR_SERVICE) as SensorManager
        linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

        val sensor = linearAccelerationSensor
        if (sensor == null) {
            Log.w(TAG, "Linear acceleration sensor is not available on this device")
            return
        }

        sensorManager.registerListener(
            sensorEventListener,
            sensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    private fun initializeLocationUpdates() {
        locationClient = LocationServices.getFusedLocationProviderClient(hostActivity)
        locationRequest = LocationRequest.Builder(100)
            .setMaxUpdateAgeMillis(1_000)
            .build()

        requestLocationUpdatesIfPermitted()
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
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            hostActivity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                hostActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
    }

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

    private fun startStopwatchWorker() {
        executor = Executors.newSingleThreadExecutor()
        stopwatchRunnable = StopwatchRunnable(this, stopwatchTextView)
        stopwatchTask = executor.submit(stopwatchRunnable)
    }

    private fun startRun() {
        Log.i(TAG, "Stopwatch started")
        stopwatchRunnable.resume()
        startStopButton.text = getString(R.string.run_stop)
        headerTextView.text = getString(R.string.run_running)
        clockStarted = true
        runEnded = false
        startLocation = null
        endLocation = null

        captureCurrentLocation { location ->
            startLocation = location
        }
    }

    private fun stopRun() {
        Log.i(TAG, "Stopwatch stopped")
        stopwatchRunnable.pause()
        startStopButton.text = getString(R.string.run_start)
        headerTextView.text = getString(R.string.run_stopped)
        clockStarted = false
        runEnded = true

        submitLongestRunTime(stopwatchRunnable.totalTime)
    }

    private fun resetRun() {
        clockStarted = false
        runEnded = true

        captureCurrentLocation { location ->
            endLocation = location
            storeLocalPreferences(hostActivity, stopwatchRunnable)
            replaceStopwatchWorker()
        }
    }

    private fun captureCurrentLocation(onLocationAvailable: (Location?) -> Unit) {
        if (!hasLocationPermission()) {
            Toast.makeText(hostActivity, "Location permission is required to record run distance", Toast.LENGTH_SHORT)
                .show()
            onLocationAvailable(null)
            return
        }

        locationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                if (location == null) {
                    Log.i(TAG, "Current location is unavailable")
                }
                onLocationAvailable(location)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Unable to get current location", exception)
                onLocationAvailable(null)
            }
    }

    private fun replaceStopwatchWorker() {
        stopwatchTask?.cancel(true)
        executor.shutdown()

        stopwatchTextView.text = "00:00:00"
        startStopButton.text = getString(R.string.run_start)
        headerTextView.text = getString(R.string.run_stopped)
        speedTextView.text = ""
        startLocation = null
        endLocation = null
        runEnded = false

        startStopwatchWorker()
    }

    fun storeLocalPreferences(context: Context, stopwatchRunnable: StopwatchRunnable) {
        val run = JSONObject().apply {
            put("date", LocalDate.now().toString())

            val distance = startLocation?.let { start ->
                endLocation?.let { end -> start.distanceTo(end) }
            }

            if (distance == null) {
                Toast.makeText(context, "Invalid location data", Toast.LENGTH_SHORT).show()
                put("dist", 0.0f)
            } else {
                put("dist", distance)
            }

            put("run time", stopwatchRunnable.totalTime)
            put("average accel", "${sensorEventListener.avgVelocity}mph")
        }

        val preferences: SharedPreferences = context.getSharedPreferences(
            "${context.packageName}_preferences",
            Context.MODE_PRIVATE
        )

        preferences.edit()
            .putString("run ${StopwatchRunnable.totalRuns}", run.toString())
            .apply()

        Log.w(TAG, run.toString())
    }

    private fun submitLongestRunTime(runTime: Any) {
        val leaderboardEntry = LeaderboardEntry(runTime.toString())
        database.child("leaderboard").push().setValue(leaderboardEntry)
            .addOnSuccessListener {
                Log.w(TAG, "Run time written to Firebase: $runTime")
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to write run time to Firebase", exception)
            }
    }

    override fun onDestroyView() {
        if (::sensorManager.isInitialized) {
            sensorManager.unregisterListener(sensorEventListener)
        }
        if (::locationClient.isInitialized) {
            locationClient.removeLocationUpdates(locationCallback)
        }

        logTimer?.cancel()
        stopwatchTask?.cancel(true)
        if (::executor.isInitialized) {
            executor.shutdown()
        }

        super.onDestroyView()
    }

    inner class SensorListener : SensorEventListener {
        private val velocities = mutableListOf<Double>()
        private var previousVelocity = 0.0
        private var velocity = 0.0

        var avgVelocity: Double = 0.0
            private set

        fun logVelocity() {
            Log.i(TAG, "speed = $velocity")
        }

        fun logAvgVelocity() {
            Log.i(TAG, "avg speed = $avgVelocity")
        }

        override fun onSensorChanged(event: SensorEvent?) {
            val sensor = linearAccelerationSensor ?: return
            if (event == null || event.sensor != sensor) {
                return
            }

            val acceleration = event.values
            val magnitude = kotlin.math.sqrt(
                acceleration[0].toDouble() * acceleration[0] +
                    acceleration[1].toDouble() * acceleration[1] +
                    acceleration[2].toDouble() * acceleration[2]
            )

            velocity = previousVelocity + magnitude * SENSOR_TIME_INTERVAL_SECONDS
            previousVelocity = velocity
            velocities.add(velocity)
            avgVelocity = velocities.average()

            if (clockStarted && view != null) {
                speedTextView.text = String.format(Locale.US, "%.2f mph", velocity)
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
    }

    inner class RunLocationCallback : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            if (location == null) {
                Log.i(TAG, "Location could not be updated")
                return
            }

            if (clockStarted) {
                startLocation = location
            }
            if (runEnded) {
                endLocation = location
            }
        }
    }

    companion object {
        private const val TAG = "RunFragment"
        private const val LOCATION_PERMISSION_REQUEST_CODE = 101
        private const val LOG_INTERVAL_MILLIS = 5_000L
        private const val SENSOR_TIME_INTERVAL_SECONDS = 0.001
    }
}
