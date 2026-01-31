package com.example.adocao_pet.ViewModel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.adocao_pet.Database.AppDatabase
import com.example.adocao_pet.Models.UserModel
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = AppDatabase.getDatabase(application).userDao()

    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    var errorMessage by mutableStateOf<String?>(null)

    /**
     * Lógica de Cadastro
     */
    fun register(onSuccess: () -> Unit) {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()

        when {
            name.isBlank() -> {
                errorMessage = "Por favor, insira o seu nome."
                return
            }
            !email.matches(emailRegex) -> {
                errorMessage = "Formato de e-mail inválido."
                return
            }
            password.length < 6 -> {
                errorMessage = "A senha deve ter pelo menos 6 caracteres."
                return
            }
        }

        viewModelScope.launch {
            try {
                val existing = userDao.getUserByEmail(email)
                if (existing != null) {
                    errorMessage = "Este e-mail já está cadastrado!"
                } else {
                    userDao.registerUser(
                        UserModel(
                            email = email,
                            name = name,
                            password = password
                        )
                    )
                    errorMessage = null
                    onSuccess()
                }
            } catch (e: Exception) {
                errorMessage = "Erro ao processar cadastro: ${e.message}"
            }
        }
    }


    fun login(onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Preencha todos os campos."
            return
        }

        viewModelScope.launch {
            try {
                val user = userDao.getUserByEmail(email)
                if (user == null) {
                    // Mensagem específica solicitada
                    errorMessage = "Conta não encontrada."
                } else if (user.password != password) {
                    errorMessage = "Senha incorreta."
                } else {
                    errorMessage = null
                    onSuccess()
                }
            } catch (e: Exception) {
                errorMessage = "Erro ao tentar fazer login."
            }
        }
    }

    fun clearFields() {
        name = ""
        email = ""
        password = ""
        errorMessage = null
    }
}