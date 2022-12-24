package com.synapsis.challengeandroid.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.synapsis.challengeandroid.di.favoriteModule
import com.synapsis.challengeandroid.favorite.databinding.ActivityFavoriteBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteActivity : AppCompatActivity() {

    private val favoriteViewModel: FavoriteViewModel by viewModel()

    private lateinit var _activityFavoriteBinding: ActivityFavoriteBinding
    private val binding get() = _activityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityFavoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(_activityFavoriteBinding.root)

        loadKoinModules(favoriteModule)

        initUI()
        initObservers()
    }

    private fun initUI() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initObservers() {

    }

}