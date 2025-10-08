package com.example.cloudcards.quizlist

// imports for Compose, navigation, viewmodels, and UI components
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cloudcards.viewmodels.CardViewModel
import com.example.cloudcards.viewmodels.FileViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import com.example.cloudcards.database.FileEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileScreen(cardViewModel: CardViewModel, fileViewModel: FileViewModel, navController: NavController) {

    // UI state for dialog visibility
    var showCreateFileDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showClearTrashDialog by remember { mutableStateOf(false) }
    var showRestoreDialog by remember { mutableStateOf(false) }

    // stores the currently selected tag ("all", "favorite", "trash", or a custom tag)
    var chosenTag by remember { mutableStateOf("all") }

    // stores the currently selected file for editing or restoring
    var chosenFile by remember { mutableStateOf<FileEntity>(FileEntity(-1, "")) }

    // drawer (side menu) state and coroutine scope for open/close actions
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // on first render, fetch all cards and files for display
    LaunchedEffect(Unit) {
        cardViewModel.getAll()
        fileViewModel.getFiles("all")
        fileViewModel.refreshTags()
    }

    // observe tags state for filter buttons in the drawer
    val tags by fileViewModel.tags.collectAsState()

    ModalNavigationDrawer(
        drawerContent = {
            // Drawer UI containing filter options and logout
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(260.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(12.dp)
            ) {
                // App header
                item {
                    Text(
                        "☁️CloudCards",
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(12.dp)
                    )

                    // Standard filters: All, Favorite, Trash
                    listOf(
                        "all" to "All",
                        "favorite" to "Favorite",
                        "trash" to "Trash"
                    ).forEach { (tag, label) ->
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            onClick = {
                                chosenTag = tag
                                fileViewModel.getFiles(tag)
                                scope.launch { drawerState.close() }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            ),
                            shape = RectangleShape,
                        ) {
                            Text(
                                label,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.weight(1f))
                        }
                    }
                }

                // Section title for custom tags
                item {
                    Text(
                        "My Card Tags",
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                //List of Tags
                items(tags) {
                    if (it.isEmpty()) {
                        return@items
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RectangleShape,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary,
                            containerColor = MaterialTheme.colorScheme.secondary
                        ),
                        onClick = {
                            chosenTag = it
                            fileViewModel.getFiles(it)
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    ) {
                        Text(
                            "#$it",
                            textAlign = TextAlign.Start,
                            modifier = Modifier.weight(1f)
                        )
                    }

                }

                // Footer with credits and logout
                item {

                    Spacer(Modifier.padding(50.dp))

                    Text(
                        text = "Created by John Maurice P. Sison",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Logout Button
                    Button(
                        onClick = {
                            // Navigate to login and clear backstack on logout
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = Color.White
                        ),
                        shape = RectangleShape
                    ) {
                        Text("Logout")
                    }
                }
            }
        },
        drawerState = drawerState
    ) {
        // Main Scaffold
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("$chosenTag - ☁️CloudCards") },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .padding(start = 12.dp, end = 8.dp)
                            )
                        }

                    },
                    actions = {
                        IconButton(onClick = {
                            showSettingsDialog = true
                        }) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    }
                )
            },

            floatingActionButton = {
                FloatingActionButton(
                    onClick =  {
                        if (chosenTag == "trash") {
                            showClearTrashDialog = true
                        } else {
                            showCreateFileDialog = true
                        }

                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    shape = CircleShape,
                ) {
                    Icon(
                        imageVector = if(chosenTag == "trash") Icons.Filled.Delete else Icons.Filled.Add,
                        contentDescription = "Add",
                        tint = MaterialTheme.colorScheme.secondary,
                    )
                }
            },
            content = {
                Box(Modifier.padding(it)) {
                    FileList(fileViewModel) {
                        chosenFile = it
                        if (chosenTag == "trash") {
                            showRestoreDialog = true
                        } else {
                            navController.navigate("editor/${it.id}")
                        }
                    }
                }

            }
        )
    }


    val coroutineScope = rememberCoroutineScope()

    // Create File Dialog
    if (showCreateFileDialog) {
        CreateFileDialog(
            fileViewModel,
            onAccept = { name ->
                coroutineScope.launch {
                    fileViewModel.insert(FileEntity(
                        name = name,
                        tags = if(chosenTag in listOf("all", "trash", "favorite")) "" else chosenTag
                    ))

                    fileViewModel.getFiles(chosenTag)
                    showCreateFileDialog = false
                }
            },
            onDismiss = {
                showCreateFileDialog = false
            }
        )
    }

    // Settings Dialog
    if (showSettingsDialog) {
        SettingsDialog {
            showSettingsDialog = false
        }
    }

    // Clear Trash Dialog
    if (showClearTrashDialog) {
        ClearTrashDialog(
            onConfirm = {
                coroutineScope.launch {
                    fileViewModel.clearTrash()
                    fileViewModel.getFiles(chosenTag)
                }
                showClearTrashDialog = false
            },
            onDismiss = {
                showClearTrashDialog = false
            }
        )
    }

    // Restore File Dialog
    if(showRestoreDialog) {
        RestoreDialog(
            onConfirm = {
                coroutineScope.launch {
                    fileViewModel.set(FileEntity(
                        id = chosenFile.id,
                        name = chosenFile.name,
                        tags = chosenFile.tags,
                        isFav = chosenFile.isFav,
                        isTrashed = false,
                    ))
                    fileViewModel.getFiles(chosenTag)
                }

                showRestoreDialog = false
            },

            onDismiss = {
                showRestoreDialog = false
            }
        )
    }
}