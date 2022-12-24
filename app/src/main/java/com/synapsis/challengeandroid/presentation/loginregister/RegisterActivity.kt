package com.synapsis.challengeandroid.presentation.loginregister

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.synapsis.challengeandroid.core.data.synapsis.local.sharedpref.UserSession
import com.synapsis.challengeandroid.core.domain.model.UserModel
import com.synapsis.challengeandroid.databinding.ActivityRegisterBinding
import com.synapsis.challengeandroid.presentation.home.HomeActivity
import com.synapsis.challengeandroid.utils.BundleKeys
import com.synapsis.challengeandroid.utils.ext.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var _activityRegisterBinding: ActivityRegisterBinding
    private val binding get() = _activityRegisterBinding
    private val viewModel: LoginRegisterViewModel by viewModel()
    private var page: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityRegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(_activityRegisterBinding.root)
        initIntent()
        setupView()
        setupActionView()

    }

    private fun initIntent() {
        page = intent.getStringExtra(BUNDLE_PAGE)
    }

    private fun setupView() {
        if (page == BundleKeys.PAGE_LOGIN) {
            binding.btnRegister.visibility = View.GONE
            binding.btnLogin.visibility = View.VISIBLE
            binding.etConfirmPassword.visibility = View.GONE
        } else {
            binding.btnRegister.visibility = View.VISIBLE
            binding.btnLogin.visibility = View.GONE
        }
    }

    private fun setupActionView() {
        binding.apply {
            btnRegister.setOnClickListener {
                registerClick()
            }
            btnLogin.setOnClickListener {
                loginClick()
            }
        }
    }

    private fun loginClick() {
        val userName = binding.etInputUsername.text.toString()
        val password = binding.etInputPassword.text.toString()
        if (userName.isNotEmpty() && password.isNotEmpty()) {
            val user = viewModel.getUser(userName)
            if (user.isNotEmpty()) {
                if (password == user.firstOrNull()?.password) {
                    UserSession.userName = user.firstOrNull()?.userName.orEmpty()
                    UserSession.userId = user.firstOrNull()?.id ?: 0
                    HomeActivity.startActivity(this@RegisterActivity)
                } else {
                    showToast("Wrong Password")
                }
            } else {
                showToast("User Not Registered")
            }
        } else {
            showToast("Form cannot be empty")
        }
    }

    private fun registerClick() {
        val userName = binding.etInputUsername.text.toString()
        val password = binding.etInputPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()
        if (userName.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()
        ) {
            if (binding.etInputPassword.text.toString() == binding.etConfirmPassword.text.toString()) {
                val user = viewModel.getUser(userName)
                if (user.isEmpty()) {
                    val userModel = UserModel(
                        userName = userName,
                        password = password
                    )
                    viewModel.registerUser(userModel)
                    Handler(Looper.getMainLooper()).postDelayed(
                        {
                            val loginUser = viewModel.getUser(userName)
                            loginUser.let {
                                UserSession.userName = it.firstOrNull()?.userName.orEmpty()
                                UserSession.userId = it.firstOrNull()?.id ?: 0
                            }
                            showToast("Success Register")
                            HomeActivity.startActivity(this@RegisterActivity)
                        }, 1000L
                    )

                } else {
                    showToast("User already exist")
                }
            } else {
                showToast("Password not match")
            }
        } else {
            showToast("Form cannot be empty")
        }
    }


    companion object {
        private const val BUNDLE_PAGE = "page"
        fun startActivity(context: Context, page: String) {
            val intent = Intent(context, RegisterActivity::class.java)
            intent.putExtra(BUNDLE_PAGE, page)
            context.startActivity(intent)
        }
    }
}