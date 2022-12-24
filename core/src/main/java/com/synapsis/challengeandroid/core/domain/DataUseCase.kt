package com.synapsis.challengeandroid.core.domain

import com.synapsis.challengeandroid.core.domain.model.BuildModel
import com.synapsis.challengeandroid.core.domain.model.DataSoc
import com.synapsis.challengeandroid.core.domain.model.DataSocWithId
import com.synapsis.challengeandroid.core.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface DataUseCase {

    suspend fun deleteDataSoc(id: Int)

    fun getBuildFactory(): Flow<List<BuildModel>>

    suspend fun saveNewData(data: DataSoc)

    suspend fun registerUser(userModel: UserModel)

    fun getDetailuser(username: String): List<UserModel>

    fun getFirstRegisteredUser(): UserModel?

    fun getUserByNfc(nfc: String): UserModel?

    fun getAllSocData(): Flow<List<DataSocWithId>>

    suspend fun updateNfcInfo(id: Int, nfc: String)
}