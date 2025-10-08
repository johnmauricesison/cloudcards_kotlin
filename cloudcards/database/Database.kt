package com.example.cloudcards.database

import com.example.cloudcards.dao.CardDao
import com.example.cloudcards.dao.FileDao

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CardEntity::class, FileEntity::class],  // my tables for this database
    version = 3 // current database version, used for migrations
)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao // exposes my CardDao for accessing cards in the database
    abstract fun fileDao(): FileDao // exposes my FileDao for accessing quiz files in the database
}
