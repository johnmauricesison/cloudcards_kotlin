package com.example.cloudcards.quizlist


import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.cloudcards.viewmodels.FileViewModel

@Composable
fun CreateFileDialog(
    fileViewModel: FileViewModel,               // ViewModel managing files
    onAccept: (String) -> Unit,                 // callback when user confirms, passing quiz name
    onDismiss: () -> Unit                       // callback when user dismisses dialog
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }       // state to hold user-typed quiz name

    AlertDialog(
        onDismissRequest = onDismiss,                 // called if user clicks outside the dialog
        title = { Text("Create Quiz") },             // dialog title
        text = {
            Column {               // container for inputs
                OutlinedTextField(
                    value = name,                  // current text in the input
                    onValueChange = { name = it },       // updates `name` as the user types
                    label = { Text("Name") }          // placeholder label
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (name.isEmpty()) {          // checks if user left the field empty
                    Toast.makeText(context, "Enter a new quiz name", Toast.LENGTH_SHORT).show()
                    return@TextButton             // stops further processing if empty
                }

                onAccept(name)            // pass the entered name to caller
            }) {
                Text("OK")         // button label
            }
        },
        dismissButton = {}      // no dismiss button (user can click outside to dismiss)
    )
}