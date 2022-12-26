package com.synapsis.datasoc.presentation.datasoc

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.synapsis.challengeandroid.core.domain.DataUseCase
import com.synapsis.challengeandroid.core.domain.model.BuildModel
import com.synapsis.challengeandroid.core.domain.model.DataSoc
import com.synapsis.challengeandroid.core.domain.model.DataSocWithId
import kotlinx.coroutines.launch

class DataSocViewModel(private val dataUseCase: DataUseCase): ViewModel() {

    fun getFactoryBuild(): LiveData<List<BuildModel>> {
        return dataUseCase.getBuildFactory().asLiveData()
    }

    fun saveDataSoc(data: DataSoc){
        viewModelScope.launch {
            dataUseCase.saveNewData(data)
        }
    }

    fun deleteDataSoc(id: Int){
        viewModelScope.launch {
            dataUseCase.deleteDataSoc(id)
        }
    }

    fun getAllNewData(): LiveData<List<DataSocWithId>> {
        return dataUseCase.getAllSocData().asLiveData()
    }
}