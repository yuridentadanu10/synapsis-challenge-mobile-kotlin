package com.synapsis.challengeandroid.core.data.synapsis

import android.os.Build
import com.synapsis.challengeandroid.core.BuildConfig
import com.synapsis.challengeandroid.core.data.synapsis.local.LocalDataSource
import com.synapsis.challengeandroid.core.domain.mapper.mapToDataSoc
import com.synapsis.challengeandroid.core.domain.mapper.mapToUser
import com.synapsis.challengeandroid.core.domain.mapper.mapToUserModel
import com.synapsis.challengeandroid.core.domain.model.BuildModel
import com.synapsis.challengeandroid.core.domain.model.DataSoc
import com.synapsis.challengeandroid.core.domain.model.DataSocWithId
import com.synapsis.challengeandroid.core.domain.model.UserModel
import com.synapsis.challengeandroid.core.domain.repository.IDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DataRepository(
    private val localDataSource: LocalDataSource
) : IDataRepository {

    override suspend fun deleteDataSoc(id: Int) {
        localDataSource.deleteDataSoc(id)
    }

    override fun getBuildFactory(): Flow<List<BuildModel>> = flow {
        val buildModelList = mutableListOf<BuildModel>()

        buildModelList.add(BuildModel("Model", Build.MODEL))
        buildModelList.add(BuildModel("Manufacturer", Build.MANUFACTURER))
        buildModelList.add(BuildModel("SDK", Build.VERSION.SDK_INT.toString()))
        buildModelList.add(BuildModel("Version Code", Build.VERSION.BASE_OS))
        buildModelList.add(BuildModel("Build Variant", BuildConfig.BUILD_TYPE))

        emit(buildModelList)
    }

    override suspend fun saveNewData(data: DataSoc) {
        localDataSource.saveNewDataSoc(data.mapToDataSoc())
    }

    override suspend fun registerNewUser(user: UserModel) {
        localDataSource.registerUser(user.mapToUser())
    }

    override suspend fun updateNfcInfo(id: Int, nfc: String) {
        localDataSource.updateNfcInfo(id, nfc)
    }

    override fun getAllDataSoc(): Flow<List<DataSocWithId>> = flow {
        localDataSource.getAllDataSoc().collect { data ->
            val dataList = mutableListOf<DataSocWithId>()
            data.forEach { dataEntity ->
                dataList.add(dataEntity.mapToDataSoc())
            }
            emit(dataList)
        }
    }

    override fun getDetailuser(userName: String): List<UserModel> {
        val dataList = mutableListOf<UserModel>()
        localDataSource.getUserDetail(userName).forEach { dataEntity ->
            dataList.add(dataEntity.mapToUserModel())
        }
        return dataList
    }

    override fun getFirstRegisteredUser(): UserModel? {
        return localDataSource.getFirstRegisteredUser()?.mapToUserModel()
    }

    override fun getUserByNfc(nfc: String): UserModel? {
        return localDataSource.getUserByNfc(nfc)?.mapToUserModel()
    }
}