package com.synapsis.challengeandroid.presentation.loginregister

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synapsis.challengeandroid.core.domain.DataUseCase
import com.synapsis.challengeandroid.core.domain.model.UserModel
import kotlinx.coroutines.launch

class LoginRegisterViewModel(private val dataUseCase: DataUseCase) : ViewModel() {

    fun registerUser(userModel: UserModel) {
        viewModelScope.launch {
            dataUseCase.registerUser(userModel)
        }
    }

    fun updateNfcInfo(id: Int, nfc: String) {
        viewModelScope.launch {
            dataUseCase.updateNfcInfo(id, nfc)
        }
    }

    fun getUser(username: String): List<UserModel> {
        return dataUseCase.getDetailuser(username)
    }

    fun getFirstUser(): UserModel? {
        return dataUseCase.getFirstRegisteredUser()
    }

    fun getUserByNfc(nfc: String): UserModel? {
        return dataUseCase.getUserByNfc(nfc)
    }
}