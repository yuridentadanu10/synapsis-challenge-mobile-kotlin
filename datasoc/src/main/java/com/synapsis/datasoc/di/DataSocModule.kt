package com.synapsis.datasoc.di

import com.synapsis.datasoc.presentation.datasoc.DataSocViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val DataSocModule = module {

    viewModel { DataSocViewModel(get()) }

}