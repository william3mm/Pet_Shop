package com.example.adocao_pet.Navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.adocao_pet.ViewModel.PetViewModel
import com.example.adocao_pet.Routes.Routes
import com.example.adocao_pet.ui.theme.Layout.AdoptedPetsScreen
import com.example.adocao_pet.ui.theme.Layout.LoginScreen
import com.example.adocao_pet.ui.theme.Layout.PetDetailScreen
import com.example.adocao_pet.ui.theme.Layout.PetHomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val petViewModel: PetViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(navController)
        }
        composable(Routes.HOME) {
            PetHomeScreen(navController, petViewModel)
        }
        composable(Routes.ADOPTED) {
            AdoptedPetsScreen(navController, petViewModel)
        }
        composable(
            route = Routes.DETAILS,
            arguments = listOf(navArgument("petId") { type = NavType.StringType })
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getString("petId") ?: ""
            PetDetailScreen(petId, navController, petViewModel)
        }
    }
}