package com.synapsis.challengeandroid.core.data.synapsis.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNewData(dataEntity: DataSocEntity)

    @Query("SELECT * FROM soc_data")
    fun getAllDataSoc(): Flow<List<DataSocEntity>>

    @Query("DELETE FROM soc_data WHERE id = :id")
    suspend fun deleteDataSoc(id: Int)

    @Insert
    suspend fun registerUser(register: UserEntity)

    @Query("SELECT * FROM user_data WHERE username LIKE :userName")
    fun getUserDetail(userName: String): List<UserEntity>

    @Query("SELECT * FROM user_data WHERE id LIKE 1")
    fun getFirstRegisteredUser(): UserEntity?

    @Query("UPDATE user_data SET nfcInfo = :nfcInfo WHERE id = :id")
    fun updateNfcInfo(id: Int, nfcInfo: String?)

    @Query("SELECT * FROM user_data WHERE nfcInfo LIKE :nfcInfo")
    fun getUserByNfc(nfcInfo: String): UserEntity?

}