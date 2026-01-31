package com.example.adocao_pet.Network


import com.example.adocao_pet.Models.PetModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface PetApiService {
    @GET("api/pets")
    suspend fun getPets(): List<PetModel>

    @POST("api/pets")
    suspend fun savePet(@Body pet: PetModel): PetModel

    @DELETE("api/pets/{id}")
    suspend fun deletePet(@Path("id") id: String)
}

object Retrofit {
    private const val BASE_URL = "http://192.168.1.8:3000/"

    val instance: PetApiService by lazy {
        retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PetApiService::class.java)
    }
}