package com.synapsis.challengeandroid.core.domain

import com.synapsis.challengeandroid.core.data.synapsis.DataRepository
import com.synapsis.challengeandroid.core.domain.model.BuildModel
import com.synapsis.challengeandroid.core.domain.model.DataSoc
import com.synapsis.challengeandroid.core.domain.model.DataSocWithId
import com.synapsis.challengeandroid.core.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

class DataInteractor(private val dataRepository: DataRepository): DataUseCase {

    override suspend fun deleteDataSoc(id: Int) {
        return dataRepository.deleteDataSoc(id)
    }

    override fun getBuildFactory(): Flow<List<BuildModel>> {
        return dataRepository.getBuildFactory()
    }

    override suspend fun saveNewData(data: DataSoc) {
        return dataRepository.saveNewData(data)
    }

    override suspend fun registerUser(userModel: UserModel) {
        return dataRepository.registerNewUser(userModel)
    }

    override fun getDetailuser(username: String): List<UserModel> {
        return dataRepository.getDetailuser(username)
    }

    override fun getFirstRegisteredUser(): UserModel? {
        return dataRepository.getFirstRegisteredUser()
    }

    override fun getUserByNfc(nfc: String): UserModel? {
        return dataRepository.getUserByNfc(nfc)
    }

    override fun getAllSocData(): Flow<List<DataSocWithId>> {
        return dataRepository.getAllDataSoc()
    }

    override suspend fun updateNfcInfo(id: Int, nfc: String) {
        return dataRepository.updateNfcInfo(id, nfc)
    }
}