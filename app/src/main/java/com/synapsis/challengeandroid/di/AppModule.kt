package com.synapsis.challengeandroid.di

import com.synapsis.challengeandroid.core.domain.DataInteractor
import com.synapsis.challengeandroid.core.domain.DataUseCase
import com.synapsis.challengeandroid.presentation.datasoc.DataSocViewModel
import com.synapsis.challengeandroid.presentation.loginregister.LoginRegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<DataUseCase> { DataInteractor(get()) }
}

val viewModelModule = module {

    viewModel { DataSocViewModel(get()) }
    viewModel { LoginRegisterViewModel(get()) }
}