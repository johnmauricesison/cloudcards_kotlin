package com.example.cloudcards


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.example.cloudcards.repository.CardRepository
import com.example.cloudcards.viewmodels.CardViewModel
import com.example.cloudcards.viewmodels.CardViewModelSettings
import com.example.cloudcards.repository.FileRepository
import com.example.cloudcards.viewmodels.FileViewModel
import com.example.cloudcards.viewmodels.FileViewModelSettings
import com.example.cloudcards.ui.theme.CloudcardsTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter") // disables unused scaffold padding warning
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            val fileDao = (this.applicationContext as App).database.fileDao()
            // gets the DAO for files my Room database
            val fileRepo = FileRepository(fileDao)
            // creates a repository for my file operations
            val fileViewModel: FileViewModel = viewModel(
                factory = FileViewModelSettings(fileRepo)
            )
            // initializes the FileViewModel using my custom factory

            val cardDao = (this.applicationContext as App).database.cardDao()
            // gets the DAO for cards from my Room database
            val cardRepo = CardRepository(cardDao)
            // creates a repository for my card operations
            val cardViewModel: CardViewModel = viewModel(
                factory = CardViewModelSettings(cardRepo)
            )
            // initializes the CardViewModel using my custom factory


            CloudcardsTheme(darkTheme = true) {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = false
                val backgroundColor = MaterialTheme.colorScheme.background
                //optional, most likely i did not use dark color theme

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = backgroundColor,
                        darkIcons = useDarkIcons
                    )
                    // sets the system bar colors to match my app background
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.clouds),
                        // loads my app clouds background image
                        contentDescription = "App Background",
                        modifier = Modifier.fillMaxSize(), // image fills the entire screen
                        contentScale = ContentScale.Crop // crop to fill while maintaining aspect ratio
                    )

                    Scaffold(modifier = Modifier.fillMaxSize(),  // scaffold to hold my app's navigation and screens
                        containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.7f)) { it ->
                        NavApp(fileViewModel, cardViewModel)
                        // semi-transparent background over my image for readability
                        // calls my NavApp composable, passing both viewmodels for navigation
                    }
                }
            }
        }
    }
}








