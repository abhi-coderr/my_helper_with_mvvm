package com.example.asyncapicalling2.ui.viewmodels

import android.content.Context
import androidx.datastore.dataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asyncapicalling2.network.ApiService
import com.example.asyncapicalling2.network.model.IMDB
import com.example.asyncapicalling2.utils.Const.Companion.API_KEY
import com.example.asyncapicalling2.utils.PreferenceDataStoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val retrofitInstance : ApiService): ViewModel() {

    val responseData = MutableLiveData<IMDB>()
    val errorMessage = MutableLiveData<String>()
    val showProgress = MutableLiveData<Boolean>()

    var job : Job? = null

    fun getImdbData(searchExpression : String){

        showProgress.value = true

        job = viewModelScope.launch{
            val response = retrofitInstance.getIMDBData(API_KEY, searchExpression)
            withContext(Dispatchers.Main){
                if (response.isSuccessful){
                    responseData.postValue(response.body())
                    showProgress.value = false
                }
                else{
                    onError("Error : ${response.message()}")
                }
            }
        }

    }

    private fun onError(message : String){
        errorMessage.value = message
        showProgress.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}