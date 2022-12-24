package com.synapsis.challengeandroid.di

import com.synapsis.challengeandroid.favorite.FavoriteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoriteModule = module {

    viewModel { FavoriteViewModel(get()) }

}