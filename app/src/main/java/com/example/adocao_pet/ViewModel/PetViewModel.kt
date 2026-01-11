package com.example.adocao_pet.ViewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.adocao_pet.Data.mockPets
import com.example.adocao_pet.Models.PetModel

class PetViewModel : ViewModel() {
    private val _availablePets = mutableStateListOf<PetModel>().apply { addAll(mockPets) }
    val availablePets: List<PetModel> get() = _availablePets

    private val _adoptedPets = mutableStateListOf<PetModel>()
    val adoptedPets: List<PetModel> get() = _adoptedPets

    fun adoptPet(pet: PetModel) {
        _availablePets.remove(pet)
        _adoptedPets.add(pet)
    }

    fun removePet(idDaRemocao: String) {
        _availablePets.removeAll { it.id == idDaRemocao }
        _adoptedPets.removeAll { it.id == idDaRemocao }
    }

    fun updatePetName(petId: String, newName: String) {
        val index = _adoptedPets.indexOfFirst { it.id == petId }
        if (index != -1) {
            val updatedPet = _adoptedPets[index].copy(name = newName)
            _adoptedPets[index] = updatedPet
        }
    }

    fun cancelAdoption(pet: PetModel) {
        _adoptedPets.remove(pet)
        _availablePets.add(pet)
    }
}