package com.example.adocao_pet.ui.theme.Layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
    val existingPet = viewModel.availablePets.find { it.id == petId }

    var name by remember { mutableStateOf(existingPet?.name ?: "") }
    var breed by remember { mutableStateOf(existingPet?.breed ?: "") }
    var category by remember { mutableStateOf(existingPet?.category ?: "Dog") }
    var description by remember { mutableStateOf(existingPet?.description ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (petId == null) "Cadastrar Pet" else "Editar Pet") })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = breed, onValueChange = { breed = it }, label = { Text("Raça") }, modifier = Modifier.fillMaxWidth())

            Row(Modifier.padding(vertical = 8.dp)) {
                FilterChip(selected = category == "Dog", onClick = { category = "Dog" }, label = { Text("Cão") })
                Spacer(Modifier.width(8.dp))
                FilterChip(selected = category == "Cat", onClick = { category = "Cat" }, label = { Text("Gato") })
            }

            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descrição") }, modifier = Modifier.fillMaxWidth(), minLines = 3)

            Button(
                onClick = {
                    val pet = PetModel(
                        id = petId ?: UUID.randomUUID().toString(),
                        name = name,
                        breed = breed,
                        category = category,
                        age = "Recém-chegado",
                        imageUrl = "https://images.unsplash.com/photo-1543466835-00a7907e9de1", // Imagem padrão
                        description = description
                    )
                    if (petId == null) viewModel.adoptPet(pet) else viewModel.removePet(pet.id)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text("Salvar Pet")
            }
        }
    }
}