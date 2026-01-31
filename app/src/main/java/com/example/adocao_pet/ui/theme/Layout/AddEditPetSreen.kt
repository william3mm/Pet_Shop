package com.example.adocao_pet.ui.theme.Layout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.adocao_pet.Models.PetModel
import com.example.adocao_pet.ViewModel.PetViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditPetScreen(
    navController: NavController,
    viewModel: PetViewModel,
    petId: String? = null
) {
    val availablePets by viewModel.availablePets.collectAsState()
    val existingPet = availablePets.find { it.id == petId }

    var name by remember { mutableStateOf(existingPet?.name ?: "") }
    var breed by remember { mutableStateOf(existingPet?.breed ?: "") }
    var category by remember { mutableStateOf(existingPet?.category ?: "Dog") }
    var description by remember { mutableStateOf(existingPet?.description ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (petId == null) "Cadastrar Pet" else "Editar Pet") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFF9800),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome do Pet") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = breed,
                onValueChange = { breed = it },
                label = { Text("Raça") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Categoria:", style = MaterialTheme.typography.labelLarge)
            Row(Modifier.padding(vertical = 8.dp)) {
                FilterChip(
                    selected = category == "Dog",
                    onClick = { category = "Dog" },
                    label = { Text("Cão") }
                )
                Spacer(Modifier.width(8.dp))
                FilterChip(
                    selected = category == "Cat",
                    onClick = { category = "Cat" },
                    label = { Text("Gato") }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descrição (história do pet)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4
            )

            Button(
                onClick = {
                    if (name.isNotBlank() && breed.isNotBlank()) {
                        val pet = PetModel(
                            id = petId ?: UUID.randomUUID().toString(),
                            name = name,
                            breed = breed,
                            category = category,
                            age = existingPet?.age ?: "Recém-chegado",
                            imageUrl = existingPet?.imageUrl ?: "https://images.unsplash.com/photo-1543466835-00a7907e9de1",
                            description = description,
                            isAdopted = existingPet?.isAdopted ?: false
                        )

                        viewModel.adoptPet(pet)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
            ) {
                Text("Salvar no Banco de Dados", color = Color.White)
            }
        }
    }
}