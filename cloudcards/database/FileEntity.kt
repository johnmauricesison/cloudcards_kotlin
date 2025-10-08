package com.example.cloudcards.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "files")
data class FileEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    // unique ID for each quiz file, auto-incremented

    val name: String = "<empty>",
    // name of my quiz file, e.g., "General Information Quiz"

    val tags: String = "",
    // optional tags for filtering/searching (e.g., "colors,science")

    val isFav: Boolean = false,
    // marks whether this quiz file is marked as favorite

    val isTrashed: Boolean = false
    // marks whether this quiz file is moved to trash
)