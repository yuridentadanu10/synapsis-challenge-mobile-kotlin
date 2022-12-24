package com.synapsis.challengeandroid.core.domain.repository

import com.synapsis.challengeandroid.core.domain.model.BuildModel
import com.synapsis.challengeandroid.core.domain.model.DataSoc
import com.synapsis.challengeandroid.core.domain.model.DataSocWithId
import com.synapsis.challengeandroid.core.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface IDataRepository {

    suspend fun saveNewData(data: DataSoc)
    suspend fun registerNewUser(user: UserModel)
    suspend fun updateNfcInfo(id: Int, nfc: String)

    suspend fun deleteDataSoc(id: Int)

    fun getBuildFactory(): Flow<List<BuildModel>>

    fun getAllDataSoc(): Flow<List<DataSocWithId>>

    fun getDetailuser(userName: String): List<UserModel>

    fun getFirstRegisteredUser(): UserModel?

    fun getUserByNfc(nfc: String): UserModel?

}