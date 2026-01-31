package com.example.adocao_pet.Database

import androidx.room.*
import com.example.adocao_pet.Models.PetModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {
    @Query("SELECT * FROM pets")
    fun getAllPets(): Flow<List<PetModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: PetModel)

    @Delete
    suspend fun deletePet(pet: PetModel)

    @Update
    suspend fun updatePet(pet: PetModel)
}