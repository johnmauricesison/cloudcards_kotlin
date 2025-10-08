package com.example.cloudcards.quizlist


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cloudcards.database.FileEntity

@Composable
fun FileItem(file: FileEntity, onClick: () -> Unit) { // the quiz file I want to display and the action to perform when I tap this item
    Row(
        modifier = Modifier
            .fillMaxWidth()                                    // the row stretches to full width
            .clickable { onClick() }                           // makes the row tappable, calling onClick when pressed
            .padding(vertical = 28.dp, horizontal = 8.dp),     // padding around the row
        verticalAlignment = Alignment.CenterVertically         // centers icon and text vertically
    ) {
        Icon(
            imageVector = if(file.isFav) Icons.Default.Favorite else Icons.Default.Folder,
            // shows a heart icon if my file is marked favorite, otherwise a folder icon

            contentDescription = null,                      // decorative, so no description
            modifier = Modifier.size(24.dp),                // icon size
            tint = if(file.isFav) Color(0xFFFF8A80) else MaterialTheme.colorScheme.primary
            // color is pinkish if favorite, primary color otherwise
        )

        Spacer(modifier = Modifier.width(12.dp))
        // adds spacing between the icon and the file name text

        Text(
            text = file.name,                              // displays the file's name
            style = MaterialTheme.typography.bodyLarge     // uses the app's large body text style
        )
    }

}