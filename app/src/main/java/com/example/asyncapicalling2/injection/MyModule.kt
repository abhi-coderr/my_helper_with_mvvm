package com.example.asyncapicalling2.injection

import com.example.asyncapicalling2.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val BASE_URL = "https://imdb-api.com/"

@Module
@InstallIn(SingletonComponent::class)
class MyModule {

    @Provides
    @Singleton
    fun retrofitInstance() : ApiService {
        val api : ApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
        return  api
    }

}