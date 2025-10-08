package com.example.cloudcards

// imports for Compose Navigation and screens
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// importing my ViewModels and screens
import com.example.cloudcards.viewmodels.CardViewModel
import com.example.cloudcards.viewmodels.FileViewModel
import com.example.cloudcards.editor.EditorScreen
import com.example.cloudcards.game.cardgame.CardGameScreen
import com.example.cloudcards.quizlist.FileScreen
import com.example.cloudcards.game.truefalsegame.TrueFalseGameScreen


@Composable
fun NavApp(fileViewModel: FileViewModel, cardViewModel: CardViewModel) {
    val navController = rememberNavController()
    // creates and remembers my navigation controller

    NavHost(navController = navController, startDestination = "main") {
        // sets up my navigation graph with "main" as my start screen
        composable("main") {
            FileScreen(cardViewModel, fileViewModel, navController)
            // shows my FileScreen, passing my ViewModels and navController
        }


        composable(
            "editor/{fileId}",
            arguments = listOf(navArgument("fileId") { type = NavType.StringType })
        ) { backStackEntry ->
            val fileId = backStackEntry.arguments?.getString("fileId")?.toInt()!!
            // gets the fileId from the navigation arguments
            EditorScreen(navController, fileId, cardViewModel, fileViewModel)
            // shows my EditorScreen with the extracted fileId and my ViewModels

        }


        composable(
            "card-game/{fileId}/{taskCount}",
            arguments = listOf(
                navArgument("fileId") { type = NavType.IntType },
                navArgument("taskCount") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val fileId = backStackEntry.arguments?.getInt("fileId") ?: 0
            val taskCount = backStackEntry.arguments?.getInt("taskCount") ?: -1
            // safely get fileId and taskCount for my card game

            CardGameScreen(cardViewModel, fileId, taskCount, navController)
            // shows my CardGameScreen using my ViewModel, fileId, and taskCount
        }

        composable(
            "true-false-game/{fileId}/{taskCount}",
            arguments = listOf(
                navArgument("fileId") { type = NavType.IntType },
                navArgument("taskCount") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val fileId = backStackEntry.arguments?.getInt("fileId") ?: 0
            val taskCount = backStackEntry.arguments?.getInt("taskCount") ?: -1
            // safely get fileId and taskCount for my true/false game
            TrueFalseGameScreen(cardViewModel, fileId, taskCount, navController)
            // shows my TrueFalseGameScreen with needed parameters
        }
    }
}