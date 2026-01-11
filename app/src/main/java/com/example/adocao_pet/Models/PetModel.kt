package com.example.adocao_pet.Models

import java.util.UUID

data class PetModel(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val breed: String,
    val age: String,
    val category: String,
    val imageUrl: String,
    val description: String
)
