package com.example.adocao_pet.ViewModel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.adocao_pet.Database.AppDatabase
import com.example.adocao_pet.Models.PetModel
import com.example.adocao_pet.Network.RetrofitInstance
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
        viewModelScope.launch {
            try {
                val petsFromApi = RetrofitInstance.api.getPets()
                // Uso do insertAll para eficiência
                petDao.insertAll(petsFromApi)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun adoptPet(pet: PetModel) = viewModelScope.launch {
        petDao.insertPet(pet.copy(isAdopted = true))
    }

    fun cancelAdoption(pet: PetModel) = viewModelScope.launch {
        petDao.insertPet(pet.copy(isAdopted = false))
    }

    fun removePet(pet: PetModel) = viewModelScope.launch {
        petDao.deletePet(pet)
    }

    fun savePet(pet: PetModel) = viewModelScope.launch {
        petDao.insertPet(pet)
    }

    fun updatePetName(petId: String, newName: String) = viewModelScope.launch {
        val allPets = _availablePets.value + _adoptedPets.value
        allPets.find { it.id == petId }?.let { petEncontrado ->
            val petAtualizado = petEncontrado.copy(name = newName)

            petDao.insertPet(petAtualizado)

            try {
                val response = RetrofitInstance.api.updatePet(petAtualizado)
                if (response.isSuccessful) {
                    android.util.Log.d("SYNC", "Sucesso ao atualizar no servidor")
                }
            } catch (e: Exception) {
                android.util.Log.e("SYNC", "Falha ao enviar para o servidor: ${e.message}")
            }
        }
    }
}