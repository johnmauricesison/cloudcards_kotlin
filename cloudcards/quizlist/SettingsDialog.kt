package com.example.cloudcards.quizlist


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cloudcards.PrefManager
import com.example.cloudcards.PrefViewModel
import com.example.cloudcards.PrefViewModelFactory

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,            // function to call when dialog is closed
) {
    val context = LocalContext.current
    val prefManager = remember { PrefManager(context) }

    val prefViewModel: PrefViewModel = viewModel(
        factory = PrefViewModelFactory(prefManager)
    )

    val showAnswer by prefViewModel.showAnswer.collectAsState() // observe current preference value reactively

    AlertDialog(
        onDismissRequest = onDismiss,              // called when user clicks outside dialog
        title = { Text("Settings") },              // dialog title
        text = {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically         // align checkbox + text vertically
                ) {
                    Checkbox(
                        checked = showAnswer,                             // current state from PrefViewModel
                        onCheckedChange = { prefViewModel.setShowAnswer(it) }   // update preference immediately when user toggles
                    )

                    Text("Show answer after each task")       // label for the checkbox
                }
            }
        },
        confirmButton = {},    // no confirm button, user just toggles
        dismissButton = {        // "OK" button to close the dialog
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}