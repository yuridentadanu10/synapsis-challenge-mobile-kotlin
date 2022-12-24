package com.synapsis.challengeandroid.presentation.datasoc

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.LinearLayoutManager
import com.synapsis.challengeandroid.core.data.synapsis.local.sharedpref.UserSession
import com.synapsis.challengeandroid.core.domain.model.DataSoc
import com.synapsis.challengeandroid.core.utils.getCurrentDateAndTimeString
import com.synapsis.challengeandroid.databinding.ActivityDataSocBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DataSocActivity : AppCompatActivity() {

    private lateinit var _activityBinding: ActivityDataSocBinding
    private val binding get() = _activityBinding
    private val viewModel: DataSocViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityBinding = ActivityDataSocBinding.inflate(layoutInflater)
        setContentView(_activityBinding.root)

        initObserver()
        initView()
    }

    private fun initView() {
        binding.apply {
            val dateInString = getCurrentDateAndTimeString()
            buttonInsert.setOnClickListener {
                if (etInputData.text.isNotBlank()) {
                    viewModel.saveDataSoc(
                        DataSoc(
                            etInputData.text.toString(),
                            dateInString,
                            UserSession.userName.ifEmpty { "Anonym" }
                        )
                    )
                    getAllNewData()
                }
            }
        }
    }

    private fun getAllNewData() {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                viewModel.getAllNewData().observe(this) {
                    val dataAdapter = NewDataAdapter(it, {
                        QrCodeActivity.startActivity(this, it.textInput)
                    }, {
                        viewModel.deleteDataSoc(it.id)
                        getAllNewData()
                    })
                    binding.rvDataInput.apply {
                        adapter = dataAdapter
                        layoutManager =
                            LinearLayoutManager(
                                this@DataSocActivity,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                    }
                }
            }, 1000L
        )
    }

    private fun initObserver() {
        getAllNewData()
        viewModel.getFactoryBuild().observe(this) {
            val dataAdapter = DataSocAdapter(it)
            binding.rvBuildModel.apply {
                adapter = dataAdapter
                layoutManager =
                    LinearLayoutManager(this@DataSocActivity, LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, DataSocActivity::class.java)
            context.startActivity(intent)
        }
    }
}