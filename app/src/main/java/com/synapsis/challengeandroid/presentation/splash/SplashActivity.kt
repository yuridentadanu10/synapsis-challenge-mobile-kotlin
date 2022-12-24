package com.synapsis.challengeandroid.presentation.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.synapsis.challengeandroid.core.data.synapsis.local.sharedpref.UserSession
import com.synapsis.challengeandroid.databinding.ActivitySplashBinding
import com.synapsis.challengeandroid.presentation.home.HomeActivity
import com.synapsis.challengeandroid.presentation.loginregister.LoginRegisterActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var _activitySplashBinding: ActivitySplashBinding

    private val binding get() = _activitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(_activitySplashBinding.root)

        val runnable = Runnable {
            if (UserSession.userName.isEmpty()) {
                val intent = Intent(this, LoginRegisterActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        Handler(Looper.getMainLooper()).postDelayed(runnable, 1500L)
    }
}