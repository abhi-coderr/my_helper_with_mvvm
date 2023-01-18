package com.example.asyncapicalling2.network

import com.example.asyncapicalling2.network.model.IMDB
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("/API/SearchMovie/{api_key}/{expression}")
    suspend fun getIMDBData(
        @Path("api_key") apiKey : String,
        @Path("expression") searchExpression : String
    ) : Response<IMDB>

}