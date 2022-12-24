package com.synapsis.challengeandroid.core.data.synapsis.local

import com.synapsis.challengeandroid.core.data.synapsis.local.room.DataDao
import com.synapsis.challengeandroid.core.data.synapsis.local.room.DataSocEntity
import com.synapsis.challengeandroid.core.data.synapsis.local.room.UserEntity
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val dao: DataDao) {

    suspend fun saveNewDataSoc(dataEntity: DataSocEntity) {
        dao.saveNewData(dataEntity)
    }

    fun getAllDataSoc(): Flow<List<DataSocEntity>> = dao.getAllDataSoc()

    suspend fun deleteDataSoc(id: Int) {
        dao.deleteDataSoc(id)
    }

    suspend fun registerUser(userEntity: UserEntity) {
        dao.registerUser(userEntity)
    }

    suspend fun updateNfcInfo(id: Int, nfc: String) {
        dao.updateNfcInfo(id, nfc)
    }

    fun getUserDetail(username: String): List<UserEntity> = dao.getUserDetail(username)

    fun getFirstRegisteredUser(): UserEntity? = dao.getFirstRegisteredUser()

    fun getUserByNfc(nfc: String): UserEntity? = dao.getUserByNfc(nfc)

}