package com.example.adocao_pet.ui.theme.Layout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.adocao_pet.ViewModel.PetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetHomeScreen(navController: NavController, viewModel: PetViewModel) {
    var selectedCategory by remember { mutableStateOf("Dog") }

    val availablePets by viewModel.availablePets.collectAsState()


    val filteredPets by remember(selectedCategory, availablePets, viewModel.searchQuery) {
        derivedStateOf {
            availablePets.filter { pet ->
                val matchesCategory = pet.category.equals(selectedCategory, ignoreCase = true)
                val matchesSearch = pet.name.contains(viewModel.searchQuery, ignoreCase = true) ||
                        pet.breed.contains(viewModel.searchQuery, ignoreCase = true)
                matchesCategory && matchesSearch
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("adopted") },
                containerColor = Color(0xFFFF9800),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Favorite, contentDescription = "Ver Adotados")
            }
        }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)) {

            Text(
                text = "Encontre seu novo amigo",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.searchQuery,
                onValueChange = { viewModel.searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Pesquisar nome ou raça...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (viewModel.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpar")
                        }
                    }
                },
                singleLine = true,
                shape = MaterialTheme.shapes.medium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF9800),
                    focusedLabelColor = Color(0xFFFF9800),
                    cursorColor = Color(0xFFFF9800)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Dog", "Cat").forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFFF9800).copy(alpha = 0.2f),
                            selectedLabelColor = Color(0xFFFF9800)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (availablePets.isEmpty() && viewModel.searchQuery.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    // Se estiver vazio, talvez você queira mostrar um botão para popular o banco
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color(0xFFFF9800))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Buscando pets...", color = Color.Gray)
                    }
                }
            } else if (filteredPets.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhum pet encontrado.", color = Color.Gray)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredPets, key = { it.id }) { pet ->
                        PetCard(pet) {
                            navController.navigate("details/${pet.id}")
                        }
                    }
                }
            }
        }
    }
}