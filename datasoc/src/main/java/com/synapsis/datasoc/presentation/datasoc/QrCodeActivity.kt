package com.synapsis.datasoc.presentation.datasoc

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.sumimakito.awesomeqr.AwesomeQrRenderer
import com.github.sumimakito.awesomeqr.option.RenderOption
import com.synapsis.challengeandroid.databinding.ActivityQrCodeBinding
import com.synapsis.challengeandroid.utils.ext.showToast

class QrCodeActivity : AppCompatActivity() {

    private lateinit var _activityBinding: ActivityQrCodeBinding
    private val binding get() = _activityBinding
    private var textQR: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityBinding = ActivityQrCodeBinding.inflate(layoutInflater)
        setContentView(_activityBinding.root)
        initIntent()

        val renderOption = RenderOption()
        renderOption.content = textQR.orEmpty()
        renderOption.size = 800
        renderOption.borderWidth = 20

        try {
            val result = AwesomeQrRenderer.render(renderOption)
            if (result.bitmap != null) {
                binding.imgQRCode.setImageBitmap(result.bitmap)
            } else {
                showToast("Error QR Code")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initIntent() {
        textQR = intent.getStringExtra(BUNDLE_STRING)
    }

    companion object{
        private const val BUNDLE_STRING = "string"
        fun startActivity(context: Context, string: String) {
            val intent = Intent(context, QrCodeActivity::class.java)
            intent.putExtra(BUNDLE_STRING, string)
            context.startActivity(intent)
        }
    }
}