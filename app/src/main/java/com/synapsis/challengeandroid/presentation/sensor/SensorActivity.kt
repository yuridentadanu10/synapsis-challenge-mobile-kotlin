package com.synapsis.challengeandroid.presentation.sensor

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationManager
import android.os.BatteryManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.synapsis.challengeandroid.R
import com.synapsis.challengeandroid.core.utils.getCurrentDateTime
import com.synapsis.challengeandroid.core.utils.toStringDate
import com.synapsis.challengeandroid.databinding.ActivitySensorBinding
import com.synapsis.challengeandroid.utils.BundleKeys



class SensorActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var _activitySensorbinding: ActivitySensorBinding
    private val binding get() = _activitySensorbinding
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var sensorManager: SensorManager
    val PERMISSION_ID = 42
    var thread: Thread? = null
    var plotData = true
    private var page: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activitySensorbinding = ActivitySensorBinding.inflate(layoutInflater)
        setContentView(_activitySensorbinding.root)
        initIntent()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        initView()
        initSensor()
        if (page == BundleKeys.PAGE_A) {
            binding.linearlayourPageA.isVisible = true
            binding.linearlayourPageB.isVisible = false
            getLastLocation()
        } else {
            binding.linearlayourPageA.isVisible = false
            binding.linearlayourPageB.isVisible = true
            initChart(binding.lineChartAccelero)
            initChart(binding.lineChartGyro)
            initChart(binding.lineChartMagneto)
        }

    }

    private fun initIntent() {
        page = intent.getStringExtra(BUNDLE_PAGE)
    }

    private fun initSensor() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }

        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }

        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }

    }

    private fun initView() {
        val bm = applicationContext.getSystemService(BATTERY_SERVICE) as BatteryManager
        val batLevel: Int = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        val date = getCurrentDateTime()
        val dateInString = date.toStringDate("yyyy/MM/dd HH:mm:ss")
        binding.apply {
            tvBattery.text = getString(R.string.text_battery, batLevel)
            tvDateTime.text = getString(R.string.text_date, dateInString)
        }
    }

    private fun initChart(viewBinding: LineChart) {
        viewBinding.apply {
            description.isEnabled = true

            description.text = when (viewBinding) {
                binding.lineChartAccelero -> "Accelerometer"
                binding.lineChartMagneto -> "Magneto Meter"
                binding.lineChartGyro -> "Gyroscope"
                else -> "Error"
            }
            setTouchEnabled(true)
            isDragEnabled = false
            setScaleEnabled(true)
            setPinchZoom(true)

            val data = LineData()
            data.setValueTextColor(Color.BLACK)
            this.data = data
            legend.textColor = Color.BLACK

            xAxis.apply {
                textColor = Color.BLACK
                setDrawGridLines(true)
                setAvoidFirstLastClipping(true)
                isEnabled = true
            }

            axisLeft.apply {
                textColor = Color.BLACK
                setDrawGridLines(false)
                axisMaximum = 50f
                axisMinimum = -10f
                setDrawGridLines(true)
            }

            axisRight.apply {
                isEnabled = false
            }
            xAxis.setDrawGridLines(false)
            setDrawBorders(false)
        }
        startPlot()
    }

    private fun startPlot() {
        if (thread != null) {
            thread?.interrupt()
        }
        thread = Thread {
            while (true) {
                plotData = true
                try {
                    Thread.sleep(10)
                } catch (e: InterruptedException) {
                }
            }
        }
        thread?.start()
    }

    private fun addEntry(event: SensorEvent, viewBinding: LineChart) {
        val data = viewBinding.data
        if (data != null) {
            var set = data.getDataSetByIndex(0)
            var set1 = data.getDataSetByIndex(1)
            var set2 = data.getDataSetByIndex(2)
            if (set == null) {
                set = createSet()
                set1 = createSet1()
                set2 = createSet2()
                data.addDataSet(set)
                data.addDataSet(set1)
                data.addDataSet(set2)
            }
            data.addEntry(
                Entry(set.entryCount.toFloat(), event.values[0]), 0
            )
            data.addEntry(
                Entry(set1.entryCount.toFloat(), event.values[1]), 1
            )
            data.addEntry(
                Entry(set2.entryCount.toFloat(), event.values[2]), 2
            )
            data.notifyDataChanged()
            viewBinding.apply {
                notifyDataSetChanged()
                setMaxVisibleValueCount(150)
                moveViewToX(data.entryCount.toFloat())
                setVisibleXRange(0f, 20f)
            }
        }
    }

    private fun createSet(): LineDataSet {
        val set = LineDataSet(null, "X Axis")
        set.axisDependency = YAxis.AxisDependency.LEFT
        set.lineWidth = 3f
        set.color = Color.MAGENTA
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.cubicIntensity = 0.2f
        return set
    }

    private fun createSet1(): LineDataSet {
        val set = LineDataSet(null, "Y Axis")
        set.axisDependency = YAxis.AxisDependency.LEFT
        set.lineWidth = 3f
        set.color = Color.BLACK
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.cubicIntensity = 0.2f
        return set
    }

    private fun createSet2(): LineDataSet {
        val set = LineDataSet(null, "Z Axis")
        set.axisDependency = YAxis.AxisDependency.LEFT
        set.lineWidth = 3f
        set.color = Color.RED
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.cubicIntensity = 0.2f
        return set
    }


    override fun onSensorChanged(p0: SensorEvent?) {
        when (p0?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                val acc =
                    "\nHardware Name: ${p0.sensor.name}\n X= ${p0.values[0]}\n Y= ${p0.values[1]}\n Z= ${p0.values[2]}"
                binding.tvAccelerometer.text = getString(R.string.text_accelerometer, acc)

                if (plotData) {
                    addEntry(p0, binding.lineChartAccelero);
                    plotData = false
                }
            }
            Sensor.TYPE_GYROSCOPE -> {
                val gyro =
                    "\nHardware Name: ${p0.sensor.name}\n X= ${p0.values[0]}\n Y= ${p0.values[1]}\n Z= ${p0.values[2]}"
                binding.tvGyro.text = getString(R.string.text_gyro, gyro)

                if (plotData) {
                    addEntry(p0, binding.lineChartGyro);
                    plotData = false;
                }
            }

            Sensor.TYPE_MAGNETIC_FIELD -> {
                val magneto =
                    "\nHardware Name: ${p0.sensor.name}\n X= ${p0.values[0]}\n Y= ${p0.values[1]}\n Z= ${p0.values[2]}"
                binding.tvMagneto.text = getString(R.string.text_magnetic, magneto)

                if (plotData) {
                    addEntry(p0, binding.lineChartMagneto);
                    plotData = false;
                }
            }

        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    try {
                        var location: Location? = task.result
                        if (location == null) {
                            requestNewLocationData()
                        } else {
                            val lct =
                                "\n Latitude: ${location.latitude} \n Longitude ${location.longitude}"
                            binding.tvGPS.text = getString(R.string.text_gps, lct)
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, "This Device not support Location", Toast.LENGTH_LONG)
                            .show()
                    }

                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback, Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location =
                "\n Latitude: ${locationResult.lastLocation?.latitude} \n Longitude ${locationResult.lastLocation?.longitude}"
            binding.tvGPS.text = getString(R.string.text_gps, location)
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ), PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        thread?.interrupt()
        super.onDestroy()
    }

    companion object {
        private const val BUNDLE_PAGE = "page"

        fun startActivity(context: Context, page: String) {
            val intent = Intent(context, SensorActivity::class.java)
            intent.putExtra(BUNDLE_PAGE, page)
            context.startActivity(intent)
        }
    }

}