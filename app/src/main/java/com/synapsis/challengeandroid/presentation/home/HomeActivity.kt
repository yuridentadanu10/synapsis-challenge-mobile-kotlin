package com.synapsis.challengeandroid.presentation.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.synapsis.challengeandroid.R
import com.synapsis.challengeandroid.core.data.synapsis.local.sharedpref.UserSession
import com.synapsis.challengeandroid.databinding.ActivityHomeBinding
import com.synapsis.challengeandroid.presentation.camera.CameraActivity
import com.synapsis.challengeandroid.presentation.datasoc.DataSocActivity
import com.synapsis.challengeandroid.presentation.loginregister.UserLogoutActivity
import com.synapsis.challengeandroid.presentation.sensor.SensorActivity
import com.synapsis.challengeandroid.utils.BundleKeys

class HomeActivity : AppCompatActivity() {

    private lateinit var _activityHomeBinding: ActivityHomeBinding

    private val binding get() = _activityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(_activityHomeBinding.root)
        binding.tvNameLogin.text = UserSession.userName.ifEmpty { getString(R.string.text_login) }
        binding.apply {
            cvHalamanA.setOnClickListener {
                SensorActivity.startActivity(this@HomeActivity, BundleKeys.PAGE_A)
            }
            cvHalamanB.setOnClickListener {
                DataSocActivity.startActivity(this@HomeActivity)
            }
            cvHalamanC.setOnClickListener {
                SensorActivity.startActivity(this@HomeActivity, BundleKeys.PAGE_C)
            }
            rvProfileUser.setOnClickListener{
                UserLogoutActivity.startActivity(this@HomeActivity)
            }
            cvHalamanBonus.setOnClickListener {
                CameraActivity.startActivity(this@HomeActivity)
            }
        }


    }

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

}