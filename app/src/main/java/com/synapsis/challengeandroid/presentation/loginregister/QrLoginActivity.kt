package com.synapsis.challengeandroid.presentation.loginregister

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.synapsis.challengeandroid.core.ApplicationProvider.context
import com.synapsis.challengeandroid.core.data.synapsis.local.sharedpref.UserSession
import com.synapsis.challengeandroid.databinding.ActivityQrLoginBinding
import com.synapsis.challengeandroid.presentation.home.HomeActivity
import com.synapsis.challengeandroid.presentation.loginregister.Qr.BarcodeBoxView
import com.synapsis.challengeandroid.presentation.loginregister.Qr.QrCodeAnalyzer
import com.synapsis.challengeandroid.utils.ext.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class QrLoginActivity : AppCompatActivity() {
    private lateinit var _activityBinding: ActivityQrLoginBinding
    private val binding get() = _activityBinding
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var barcodeBoxView: BarcodeBoxView
    private val viewModel: LoginRegisterViewModel by viewModel()
    val PERMISSION_ID = 41

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityBinding = ActivityQrLoginBinding.inflate(layoutInflater)
        setContentView(_activityBinding.root)

        barcodeBoxView = BarcodeBoxView(this)
        addContentView(barcodeBoxView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))

        cameraExecutor = Executors.newSingleThreadExecutor()
        if (checkPermissions()){
            startCamera()
        } else {
            requestPermissions()
        }

    }

    private fun login(userName: String) {
        if (userName.isNotEmpty()) {
            val user = viewModel.getUser(userName)
            if (user.isNotEmpty()) {
                UserSession.userName = user.firstOrNull()?.userName.orEmpty()
                UserSession.userId = user.firstOrNull()?.id ?: 0
                HomeActivity.startActivity(this)
            } else {
                showToast("User Not Registered")
            }
        } else {
            showToast("Form cannot be empty")
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, QrCodeAnalyzer(
                        context, barcodeBoxView, binding.previewView.width.toFloat(),
                        binding.previewView.height.toFloat()
                    ) { qr ->
                        login(qr)
                    })
                }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalyzer
                )

            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
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
                startCamera()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {

        fun startActivity(context: Context) {
            val intent = Intent(context, QrLoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}