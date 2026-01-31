package com.example.adocao_pet.ViewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adocao_pet.Models.PetModel
import com.example.adocao_pet.Network.Retrofit
import kotlinx.coroutines.launch

class PetViewModel : ViewModel() {

    private val _availablePets = mutableStateListOf<PetModel>()
    val availablePets: List<PetModel> get() = _availablePets

    private val _adoptedPets = mutableStateListOf<PetModel>()
    val adoptedPets: List<PetModel> get() = _adoptedPets

    init {
        loadPets()
    }

    fun loadPets() {
        viewModelScope.launch {
            try {
                val allPets = Retrofit.instance.getPets()
                println("ALELUIA: Recebi ${allPets.size} pets")
                _availablePets.clear()
                _availablePets.addAll(allPets.filter { !it.isAdopted })
            } catch (e: Exception) {
                // ISSO VAI APARECER EM VERMELHO NO LOGCAT
                android.util.Log.e("RETROFIT_ERROR", "Causa do erro: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun adoptPet(pet: PetModel) {
        viewModelScope.launch {
            try {
                val updatedPet = pet.copy(isAdopted = true)
                Retrofit.instance.savePet(updatedPet)

                _availablePets.remove(pet)
                _adoptedPets.add(updatedPet)
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun removePet(idDaRemocao: String) {
        viewModelScope.launch {
            try {
                Retrofit.instance.deletePet(idDaRemocao)
                _availablePets.removeAll { it.id == idDaRemocao }
                _adoptedPets.removeAll { it.id == idDaRemocao }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun updatePetName(petId: String, newName: String) {
        viewModelScope.launch {
            try {
                val pet = _adoptedPets.find { it.id == petId } ?: _availablePets.find { it.id == petId }
                pet?.let {
                    val updatedPet = it.copy(name = newName)
                    Retrofit.instance.savePet(updatedPet)
                    loadPets()
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun cancelAdoption(pet: PetModel) {
        viewModelScope.launch {
            try {
                val updatedPet = pet.copy(isAdopted = false)
                Retrofit.instance.savePet(updatedPet)

                _adoptedPets.remove(pet)
                _availablePets.add(updatedPet)
            } catch (e: Exception) { e.printStackTrace() }
        }
    }
}