package com.synapsis.challengeandroid.presentation.camera

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Paint.DITHER_FLAG
import android.graphics.PointF
import android.graphics.Rect
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.synapsis.challengeandroid.core.utils.getCurrentDateAndTimeString
import com.synapsis.challengeandroid.databinding.ActivityCameraBinding
import com.thorny.photoeasy.PhotoEasy


class CameraActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var _activityBinding: ActivityCameraBinding
    private val binding get() = _activityBinding

    val PERMISSION_ID = 41
    private lateinit var photoEasy: PhotoEasy
    private lateinit var sensorManager: SensorManager
    var magneto = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityBinding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(_activityBinding.root)
        binding.btnCamera.setOnClickListener {
            if (checkPermissions()){
                cameraAction()
            } else {
                requestPermissions()
            }

        }
        initSensor()
    }

    private fun initSensor() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }

    }

    private fun cameraAction() {
        photoEasy = PhotoEasy.builder()
            .setActivity(this)
            .build()

        photoEasy.startActivityForResult(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        photoEasy.onActivityResult(requestCode, resultCode) {
            val magnetoWaterMark = addWatermark(it, magneto)
            val dateTimeWaterMark = addWatermark(
                magnetoWaterMark,
                "Date and Time = ${getCurrentDateAndTimeString()}",
                WatermarkOptions(Corner.TOP_LEFT)
            )
            binding.imageview.setImageBitmap(dateTimeWaterMark)
        }
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.CAMERA,
            ), PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                cameraAction()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }


    private fun addWatermark(
        bitmap: Bitmap,
        watermarkText: String,
        options: WatermarkOptions = WatermarkOptions()
    ): Bitmap {
        val result = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(result)
        val paint = Paint(ANTI_ALIAS_FLAG or DITHER_FLAG)
        paint.textAlign = when (options.corner) {
            Corner.TOP_LEFT,
            Corner.BOTTOM_LEFT -> Paint.Align.LEFT
            Corner.TOP_RIGHT,
            Corner.BOTTOM_RIGHT -> Paint.Align.RIGHT
        }
        val textSize = result.width * options.textSizeToWidthRatio
        paint.textSize = textSize
        paint.color = options.textColor
        if (options.shadowColor != null) {
            paint.setShadowLayer(textSize / 2, 0f, 0f, options.shadowColor)
        }
        if (options.typeface != null) {
            paint.typeface = options.typeface
        }
        val padding = result.width * options.paddingToWidthRatio
        val coordinates =
            calculateCoordinates(
                watermarkText,
                paint,
                options,
                canvas.width,
                canvas.height,
                padding
            )
        canvas.drawText(watermarkText, coordinates.x, coordinates.y, paint)
        return result
    }

    private fun calculateCoordinates(
        watermarkText: String,
        paint: Paint,
        options: WatermarkOptions,
        width: Int,
        height: Int,
        padding: Float
    ): PointF {
        val x = when (options.corner) {
            Corner.TOP_LEFT,
            Corner.BOTTOM_LEFT -> {
                padding
            }
            Corner.TOP_RIGHT,
            Corner.BOTTOM_RIGHT -> {
                width - padding
            }
        }
        val y = when (options.corner) {
            Corner.BOTTOM_LEFT,
            Corner.BOTTOM_RIGHT -> {
                height - padding
            }
            Corner.TOP_LEFT,
            Corner.TOP_RIGHT -> {
                val bounds = Rect()
                paint.getTextBounds(watermarkText, 0, watermarkText.length, bounds)
                val textHeight = bounds.height()
                textHeight + padding

            }
        }
        return PointF(x, y)
    }


    override fun onSensorChanged(p0: SensorEvent?) {
        when (p0?.sensor?.type) {
            Sensor.TYPE_MAGNETIC_FIELD -> {
                magneto =
                    "Magneto X= ${p0.values[0]}\n Y= ${p0.values[1]}\n Z= ${p0.values[2]}"
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, CameraActivity::class.java)
            context.startActivity(intent)
        }
    }

}