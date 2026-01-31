package com.example.adocao_pet.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "pets")
data class PetModel(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val breed: String,
    val age: String,
    val category: String,
    val imageUrl: String,
    val description: String,
    val isAdopted: Boolean = false
)