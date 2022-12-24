package com.synapsis.challengeandroid.presentation.loginregister

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.synapsis.challengeandroid.core.data.synapsis.local.sharedpref.UserSession
import com.synapsis.challengeandroid.databinding.ActivityLoginRegisterBinding
import com.synapsis.challengeandroid.presentation.home.HomeActivity
import com.synapsis.challengeandroid.utils.BundleKeys
import com.synapsis.challengeandroid.utils.ext.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.Executor

class LoginRegisterActivity : AppCompatActivity() {
    private lateinit var _activityLoginRegisterBinding: ActivityLoginRegisterBinding
    private val binding get() = _activityLoginRegisterBinding
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private val viewModel: LoginRegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityLoginRegisterBinding = ActivityLoginRegisterBinding.inflate(layoutInflater)
        setContentView(_activityLoginRegisterBinding.root)

        binding.tvRegister.setOnClickListener {
            RegisterActivity.startActivity(this@LoginRegisterActivity, BundleKeys.PAGE_REGISTER)
        }

        binding.loginUsername.setOnClickListener {
            RegisterActivity.startActivity(this@LoginRegisterActivity, BundleKeys.PAGE_LOGIN)
        }

        binding.loginQrCode.setOnClickListener {
            QrLoginActivity.startActivity(this@LoginRegisterActivity)
        }

        binding.loginWithNfcCard.setOnClickListener {
            NfcRegisterActivity.startActivity(this@LoginRegisterActivity, BundleKeys.PAGE_LOGIN)
        }

        setupFingerPrint()
    }

    private fun setupFingerPrint() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    showToast(errString.toString())
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    val user = viewModel.getFirstUser()
                    if (user == null) {
                        showToast("you have never registered")
                    } else {
                        UserSession.userName = user.userName
                        UserSession.userId = user.id
                        showToast("Autentifikasi Berhasil")
                        HomeActivity.startActivity(this@LoginRegisterActivity)
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    showToast("Autentifikasi Gagal")
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Fingerprint Login")
            .setSubtitle("make sure you have been registered in this application")
            .setNegativeButtonText("Use Password")
            .build()

        binding.loginWithFingerPrint.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }

    }

    companion object {

        fun startActivity(context: Context) {
            val intent = Intent(context, LoginRegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }
}