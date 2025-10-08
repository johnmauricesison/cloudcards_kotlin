package com.example.cloudcards.quizlist


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cloudcards.database.FileEntity
import com.example.cloudcards.viewmodels.FileViewModel

@SuppressLint("MutableCollectionMutableState")
@Composable
fun FileList(
    fileViewModel: FileViewModel,         // the ViewModel managing my files
    onFileClick: (FileEntity) -> Unit     // function to call when a file is clicked
) {
    val files by fileViewModel.files.collectAsState()
    // observes my files StateFlow and updates UI when files change


    if (files.isEmpty()) {
        // Show a centered message if no files exist
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "No quizzes yet",    // friendly empty state message
                fontSize = 20.sp
            )
        }
        return                        // exit early since nothing to list
    }


    // Otherwise, show the file list
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

        itemsIndexed(files) { index, file ->
            FileItem(file) {                // use my reusable FileItem to display
                onFileClick(file)           // pass the clicked file back to the caller
            }

            if (index != files.lastIndex) {
                // add a subtle divider between items, but skip after last item
                HorizontalDivider(Modifier, 1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha=0.5f))
            }

        }

    }
}