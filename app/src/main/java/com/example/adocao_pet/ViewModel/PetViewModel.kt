package com.example.adocao_pet.ViewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.adocao_pet.Database.AppDatabase
import com.example.adocao_pet.Models.PetModel
import com.example.adocao_pet.Network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
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
        fetchPetsFromBackend()
    }

    private fun observePets() {
        viewModelScope.launch {
            petDao.getAllPets().collectLatest { allPets ->
                _availablePets.value = allPets.filter { !it.isAdopted }
                _adoptedPets.value = allPets.filter { it.isAdopted }
            }
        }
    }

    private fun fetchPetsFromBackend() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val petsFromApi = RetrofitInstance.api.getPets()
                petDao.insertAll(petsFromApi)
            } catch (e: Exception) {
                Log.e("API_FETCH", "Erro: ${e.message}")
            }
        }
    }

    private suspend fun syncWithBackend(pet: PetModel) {
        try {
            val response = RetrofitInstance.api.updatePet(pet)
            if (response.isSuccessful) {
                Log.d("SYNC", "Sucesso: ${pet.name}")
            }
        } catch (e: Exception) {
            Log.e("SYNC", "Falha: ${e.message}")
        }
    }

    fun adoptPet(pet: PetModel) = viewModelScope.launch(Dispatchers.IO) {
        val updatedPet = pet.copy(isAdopted = true)
        petDao.insertPet(updatedPet)
        syncWithBackend(updatedPet)
    }

    fun cancelAdoption(pet: PetModel) = viewModelScope.launch(Dispatchers.IO) {
        val updatedPet = pet.copy(isAdopted = false)
        petDao.insertPet(updatedPet)
        syncWithBackend(updatedPet)
    }

    fun removePet(pet: PetModel) = viewModelScope.launch(Dispatchers.IO) {
        petDao.deletePet(pet)
        try {
            RetrofitInstance.api.deletePet(pet.id)
        } catch (e: Exception) {
            Log.e("SYNC", "Erro ao remover")
        }
    }

    fun savePet(pet: PetModel) = viewModelScope.launch(Dispatchers.IO) {
        petDao.insertPet(pet)
        syncWithBackend(pet)
    }

    fun updatePetName(petId: String, newName: String) = viewModelScope.launch(Dispatchers.IO) {
        val allPets = _availablePets.value + _adoptedPets.value
        allPets.find { it.id == petId }?.let { petEncontrado ->
            val petAtualizado = petEncontrado.copy(name = newName)
            petDao.insertPet(petAtualizado)
            syncWithBackend(petAtualizado)
        }
    }
}