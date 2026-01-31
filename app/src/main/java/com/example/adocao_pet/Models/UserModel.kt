package com.example.adocao_pet.Models


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserModel(
    @PrimaryKey val email: String,
    val name: String,
    val password: String
)