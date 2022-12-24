package com.synapsis.challengeandroid.core.domain.mapper

import com.synapsis.challengeandroid.core.data.synapsis.local.room.DataSocEntity
import com.synapsis.challengeandroid.core.data.synapsis.local.room.UserEntity
import com.synapsis.challengeandroid.core.domain.model.DataSoc
import com.synapsis.challengeandroid.core.domain.model.DataSocWithId
import com.synapsis.challengeandroid.core.domain.model.UserModel

fun DataSocEntity.mapToDataSoc(): DataSocWithId =
    DataSocWithId(
        id = this.id,
        textInput = this.textInput,
        date = this.date,
        createdBy = this.createdBy
    )

fun DataSoc.mapToDataSoc(): DataSocEntity =
    DataSocEntity(
        textInput = this.textInput,
        createdBy = this.createdBy,
        date =  this.date
    )

fun UserModel.mapToUser(): UserEntity =
    UserEntity(
        id = this.id,
        username = this.userName,
        password =  this.password,
        fingerPrintInfo = this.fingerPrint,
        nfcNumber = this.nfcInfo
    )

fun UserEntity.mapToUserModel(): UserModel = UserModel(
    id = this.id,
    userName = this.username,
    password =  this.password,
    fingerPrint = this.fingerPrintInfo,
    nfcInfo = this.nfcNumber
)