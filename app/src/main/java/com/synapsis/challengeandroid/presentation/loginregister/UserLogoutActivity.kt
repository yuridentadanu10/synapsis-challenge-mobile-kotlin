package com.synapsis.challengeandroid.presentation.loginregister

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.synapsis.challengeandroid.core.data.synapsis.local.sharedpref.UserSession
import com.synapsis.challengeandroid.databinding.ActivityUserLogoutBinding
import com.synapsis.challengeandroid.utils.BundleKeys


class UserLogoutActivity : AppCompatActivity() {
    private lateinit var _activityLoginRegisterBinding: ActivityUserLogoutBinding
    private val binding get() = _activityLoginRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityLoginRegisterBinding = ActivityUserLogoutBinding.inflate(layoutInflater)
        setContentView(_activityLoginRegisterBinding.root)

        binding.tvRegisterNfc.setOnClickListener {
            NfcRegisterActivity.startActivity(this, BundleKeys.PAGE_REGISTER)
        }
        binding.tvLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage("Are you sure you want to Logout?")
                .setCancelable(false)
                .setPositiveButton(
                    "Yes"
                ) { _, _ ->
                    UserSession.userId = 0
                    UserSession.userName = ""
                    LoginRegisterActivity.startActivity(this)
                }
                .setNegativeButton("No", null)
                .show()
        }

    }

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, UserLogoutActivity::class.java)
            context.startActivity(intent)
        }
    }
}