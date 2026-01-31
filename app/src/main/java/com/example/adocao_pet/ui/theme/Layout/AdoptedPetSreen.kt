package com.example.adocao_pet.ui.theme.Layout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.adocao_pet.Models.PetModel
import com.example.adocao_pet.ViewModel.PetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdoptedPetsScreen(navController: NavController, viewModel: PetViewModel) {
    // IMPORTANTE: Coleta o StateFlow do Room como Estado do Compose
    val adoptedPets by viewModel.adoptedPets.collectAsState()

    var petToEdit by remember { mutableStateOf<PetModel?>(null) }
    var newName by remember { mutableStateOf("") }

    // Dialogo para Editar Nome (Lógica de UI)
    if (petToEdit != null) {
        AlertDialog(
            onDismissRequest = { petToEdit = null },
            title = { Text("Mudar nome de ${petToEdit?.name}") },
            text = {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Novo nome") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        petToEdit?.let { viewModel.updatePetName(it.id, newName) }
                        petToEdit = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
                ) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { petToEdit = null }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus Pets Adotados") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFF9800),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        if (adoptedPets.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Ainda não adotou nenhum animal.",
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(adoptedPets, key = { it.id }) { pet ->
                    Box(modifier = Modifier.fillMaxWidth()) {
                        // Card do Pet
                        PetCard(pet = pet, onClick = {
                            newName = pet.name
                            petToEdit = pet
                        })

                        IconButton(
                            onClick = { viewModel.cancelAdoption(pet) },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Cancelar Adoção",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}