package com.synapsis.challengeandroid.presentation.loginregister

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.synapsis.challengeandroid.core.data.synapsis.local.sharedpref.UserSession
import com.synapsis.challengeandroid.core.domain.model.UserModel
import com.synapsis.challengeandroid.databinding.ActivityNfcRegisterBinding
import com.synapsis.challengeandroid.presentation.home.HomeActivity
import com.synapsis.challengeandroid.utils.BundleKeys
import com.synapsis.challengeandroid.utils.ext.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class NfcRegisterActivity : AppCompatActivity() {
    private lateinit var _activityLoginRegisterBinding: ActivityNfcRegisterBinding
    private val binding get() = _activityLoginRegisterBinding

    private var nfcAdapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null
    private var intentFilters: Array<IntentFilter>? = null
    private var techLists: Array<Array<String>>? = null

    private val viewModel: LoginRegisterViewModel by viewModel()

    private var page: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityLoginRegisterBinding = ActivityNfcRegisterBinding.inflate(layoutInflater)
        setContentView(_activityLoginRegisterBinding.root)
        initIntent()
        val intent = Intent(this, javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)

        intentFilters = arrayOf(IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED))


        techLists = arrayOf(
            arrayOf(android.nfc.tech.NfcA::class.java.name),
            arrayOf(android.nfc.tech.NfcB::class.java.name),
            arrayOf(android.nfc.tech.NfcF::class.java.name),
        )

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.action == NfcAdapter.ACTION_TECH_DISCOVERED) {
            val tagIdByte: ByteArray = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID) ?: return
            val tagId = ArrayList<String>()
            tagIdByte.forEach { tagId.add(String.format("%02X", it)) }
            binding.tagText.text = tagId.joinToString(":")
            val userModel = viewModel.getUserByNfc(tagId.joinToString(":"))
            if (page == BundleKeys.PAGE_LOGIN) {
                loginNfc(userModel)
            } else {
                registerNfcNumber(userModel, tagId.joinToString(":"))
            }
        }
    }

    private fun registerNfcNumber(userModel: UserModel?, nfcInfo: String) {
        if (userModel == null) {
            viewModel.updateNfcInfo(UserSession.userId, nfcInfo)
            showToast("Success register your nfc card for login")
            finish()
        } else {
            if (userModel.userName == UserSession.userName) {
                showToast("This card have been registered to this account")
            } else {
                showToast("This card have been registered to other account")
            }
        }
    }

    private fun loginNfc(user: UserModel?) {
        if (user == null) {
            showToast("This card is not sync with any account, please login than register your card")
        } else {
            UserSession.userName = user.userName
            UserSession.userId = user.id
            HomeActivity.startActivity(this)
        }
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, intentFilters, techLists)
    }

    override fun onPause() {
        nfcAdapter?.disableForegroundDispatch(this)
        super.onPause()
    }

    private fun initIntent() {
        page = intent.getStringExtra(BUNDLE_PAGE)
    }

    companion object {
        private const val BUNDLE_PAGE = "page"
        fun startActivity(context: Context, page: String) {
            val intent = Intent(context, NfcRegisterActivity::class.java)
            intent.putExtra(BUNDLE_PAGE, page)
            context.startActivity(intent)
        }
    }
}