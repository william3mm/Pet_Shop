package com.example.adocao_pet.Network

import com.example.adocao_pet.Models.PetModel
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PetApiService {
    @GET("api/pets")
    suspend fun getPets(): List<PetModel>

    @POST("api/pets")
    suspend fun updatePet(@Body pet: PetModel): Response<PetModel>

    @DELETE("api/pets/{id}")
    suspend fun deletePet(@Path("id") id: String): Response<Unit>
}

object RetrofitInstance {
    private const val BASE_URL = "http://192.168.1.3:3000/"

    val api: PetApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PetApiService::class.java)
    }
}