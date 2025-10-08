package com.example.cloudcards.editor


import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.cloudcards.database.CardEntity
import com.example.cloudcards.viewmodels.CardViewModel
import com.example.cloudcards.database.FileEntity
import com.example.cloudcards.viewmodels.FileViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(navController: NavController, fileId: Int, cardViewModel: CardViewModel, fileViewModel: FileViewModel) {
    val context = LocalContext.current


    // === State variables to control modal visibility ===
    var showCreateCardDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showRenameDialog by remember {mutableStateOf(false)}
    var showPlayDialog by remember {mutableStateOf(false)}
    var showPlaySettingsDialog by remember {mutableStateOf(false)}
    var showDeleteDialog by remember {mutableStateOf(false)}
    var showTagDialog by remember {mutableStateOf(false)}

    var selectedCard by remember { mutableStateOf<CardEntity?>(null) } // currently selected card for editing
    var selectedMode by remember {mutableStateOf("")}  // stores selected play mode for quiz play


    // Get current file info
    val files by fileViewModel.files.collectAsState()
    val file = files.find {it.id == fileId} ?: return  // fetch current FileEntity using fileId; return if not found

    val coroutineScope = rememberCoroutineScope()


    // Scaffold provides topBar, FAB, and content structure
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editor") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("main")
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go Back")
                    }
                },
                actions = {
                    // Tag editor
                    IconButton(onClick = {
                        showTagDialog = true
                    }) {
                        Icon(Icons.Default.Tag, contentDescription = "Tags")
                    }

                    // Delete confirmation
                    IconButton(onClick = {
                        showDeleteDialog = true
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }

                    // Rename quiz
                    IconButton(onClick = {
                        showRenameDialog = true
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    // Toggle favorite status
                    IconButton(onClick = {
                        coroutineScope.launch {
                            fileViewModel.set(file.copy(isFav = !file.isFav))
                            fileViewModel.getAllNonTrashed()
                        }
                    }) {
                        Icon(if(file.isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder, contentDescription = "Favorite")
                    }
                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = {
                    cardViewModel.getAll()

                    // Require at least 4 cards to play a quiz
                    if (cardViewModel.cards.value.filter { it.fileId == fileId }.size < 4) {
                        Toast.makeText(context, "You need at least 4 cards", Toast.LENGTH_SHORT).show()
                        return@FloatingActionButton
                    }
                    showPlayDialog = true  // open play mode selection dialog
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Play",
                    tint = MaterialTheme.colorScheme.secondary,
                )
            }
        }
    ) { paddingValues ->

        // Main content: display card list for editing/adding
        EditorCardList(
            modifier = Modifier.padding(paddingValues),
            viewModel = cardViewModel,
            fileId = fileId,
            onCardClick = {
                selectedCard = it
                showEditDialog = true
            },
            onAddClick = {
                showCreateCardDialog = true
            }
        )

        if (showCreateCardDialog) {
            CreateCardDialog(
                onDismiss = {showCreateCardDialog = false}
            ) { side1, side2 ->
                // Ensure no duplicate answers within the same quiz
                if ((cardViewModel.cards.value.filter {it.fileId == fileId}.map {it.side2}).contains(side2.trim())) {
                    Toast.makeText(context, "2 cards can't have the same answer", Toast.LENGTH_SHORT).show()
                    return@CreateCardDialog
                }
                cardViewModel.insert(CardEntity(
                    side1 = side1,
                    side2 = side2,
                    fileId = fileId,
                    fixedOptions = false,
                    incorrectOption1 = "",
                    incorrectOption2 = "",
                    incorrectOption3 = "",
                ))
                showCreateCardDialog = false
            }
        }

        if (showEditDialog) {
            EditCardDialog(
                currentCard = selectedCard!!,
                onDismiss = {showEditDialog = false},
                onDelete = {
                    cardViewModel.delete(selectedCard!!)
                    showEditDialog = false
                },
                onConfirm = { fixedOptions, side1, side2, incorrectOption1, incorrectOption2, incorrectOption3 ->
                    if ((cardViewModel.cards.value.filter {it.fileId == fileId}.map {it.side2}).contains(side2.trim()) &&
                        selectedCard!!.side2 != side2) {
                        Toast.makeText(context, "2 cards can't have the same answer", Toast.LENGTH_SHORT).show()
                        return@EditCardDialog
                    }
                    cardViewModel.set(CardEntity(
                        id = selectedCard!!.id,
                        side1 = side1,
                        side2 = side2,
                        fileId = fileId,
                        fixedOptions = fixedOptions,
                        incorrectOption1 = incorrectOption1,
                        incorrectOption2 = incorrectOption2,
                        incorrectOption3 = incorrectOption3,
                    ))
                    showEditDialog = false
                }
            )
        }

        val coroutineScope = rememberCoroutineScope()
        if (showRenameDialog) {
            RenameDialog(
                onConfirm = {
                    if (it.isEmpty()) {
                        Toast.makeText(context, "Enter a new quiz name", Toast.LENGTH_SHORT).show()
                        return@RenameDialog
                    }

                    coroutineScope.launch {
                        fileViewModel.set(FileEntity(
                            id = fileId,
                            name = it
                        ))
                    }

                    showRenameDialog = false
                },
                onDismiss = {
                    showRenameDialog = false
                }
            )
        }

        if (showPlayDialog) {
            PlayDialog(
                onDismiss = {
                    showPlayDialog = false
                },
                onPlay = {
                    selectedMode = it   // save chosen mode (quiz type)
                    showPlayDialog = false
                    showPlaySettingsDialog = true
                }
            )
        }

        if (showPlaySettingsDialog) {
            PlaySettingsDialog(
                onChooseAllCards = {
                    navController.navigate("$selectedMode/$fileId/-1")
                },
                onConfirm = {
                    navController.navigate("$selectedMode/$fileId/$it")
                }
            )
        }

        if (showDeleteDialog) {
            DeleteDialog(
                onConfirm = {
                    coroutineScope.launch {
                        fileViewModel.set(FileEntity(
                            id = file.id,
                            name = file.name,
                            tags = file.tags,
                            isFav = file.isFav,
                            isTrashed = true
                        ))
                    }

                    // return to main after deletion
                    navController.navigate("main")
                },
                onDismiss = {
                    showDeleteDialog = false
                }
            )
        }

        if (showTagDialog) {
            TagDialog(
                file = file,
                onConfirm = {
                    coroutineScope.launch {
                        fileViewModel.set(FileEntity(
                            file.id,
                            file.name,
                            it,
                            file.isFav,
                            file.isTrashed
                        ))
                    }
                    showTagDialog = false
                },
                onDismiss = {
                    showTagDialog = false
                }
            )
        }
    }
}