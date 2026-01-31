package com.example.adocao_pet.ViewModel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.adocao_pet.Database.AppDatabase
import com.example.adocao_pet.Models.PetModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PetViewModel(application: Application) : AndroidViewModel(application) {

    private val petDao = AppDatabase.getDatabase(application).petDao()

    private val _availablePets = MutableStateFlow<List<PetModel>>(emptyList())
    val availablePets: StateFlow<List<PetModel>> = _availablePets

    private val _adoptedPets = MutableStateFlow<List<PetModel>>(emptyList())
    val adoptedPets: StateFlow<List<PetModel>> = _adoptedPets

    var searchQuery by mutableStateOf("")

    init {
        observePets()
    }

    private fun observePets() {
        viewModelScope.launch {
            petDao.getAllPets().collectLatest { allPets ->
                _availablePets.value = allPets.filter { !it.isAdopted }
                _adoptedPets.value = allPets.filter { it.isAdopted }
            }
        }
    }

    fun insertInitialData(pets: List<PetModel>) {
        viewModelScope.launch {
            pets.forEach { petDao.insertPet(it) }
        }
    }

    fun adoptPet(pet: PetModel) {
        viewModelScope.launch {
            petDao.updatePet(pet.copy(isAdopted = true))
        }
    }

    fun cancelAdoption(pet: PetModel) {
        viewModelScope.launch {
            petDao.updatePet(pet.copy(isAdopted = false))
        }
    }

    fun removePet(pet: PetModel) {
        viewModelScope.launch {
            petDao.deletePet(pet)
        }
    }

    fun updatePetName(petId: String, newName: String) {
        viewModelScope.launch {
            val pet = (_availablePets.value + _adoptedPets.value).find { it.id == petId }
            pet?.let {
                petDao.updatePet(it.copy(name = newName))
            }
        }
    }

}