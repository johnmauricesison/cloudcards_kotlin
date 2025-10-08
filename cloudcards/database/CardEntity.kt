package com.example.cloudcards.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cards", // names my table "cards" in the database
    foreignKeys = [
        ForeignKey(
            entity = FileEntity::class,     // links to my FileEntity table
            parentColumns = ["id"],         // parent column in FileEntity
            childColumns = ["fileId"],      // child column in this CardEntity
            onDelete = ForeignKey.CASCADE   // if the parent FileEntity is deleted, all related cards are also deleted automatically
        )
    ],
    indices = [Index(value = ["fileId"])] // creates an index on fileId for faster queries filtering by fileId
)
data class CardEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    // unique ID for each card, auto-incremented

    val side1: String, // the front of the flashcard (question or term)
    val side2: String, // the back of the flashcard (answer)

    val fileId: Int, // ID of the quiz file this card belongs to (foreign key)

    val fixedOptions: Boolean = false, // true if the card has fixed multiple-choice options

    val incorrectOption1: String = "",
    val incorrectOption2: String = "",
    val incorrectOption3: String = "",
    // stores up to 3 incorrect options for multiple-choice quizzes
)