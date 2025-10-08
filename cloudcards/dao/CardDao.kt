package com.example.cloudcards.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.cloudcards.database.CardEntity

@Dao
interface CardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(card: CardEntity): Long
    // inserts a card into my database
    // replaces existing card with the same primary key if it already exists
    // returns the new row ID as Long

    @Query("SELECT * FROM cards")
    suspend fun getAll(): List<CardEntity>
    // retrieves all cards stored in my "cards" table

    @Update
    suspend fun update(card: CardEntity)
    // updates the details of an existing card in my database

    @Delete
    suspend fun delete(card: CardEntity)
    // deletes the provided card from my database

    @Query("SELECT * FROM cards WHERE fileId = :fileId")
    suspend fun getByFileId(fileId: Int): List<CardEntity>
    // retrieves all cards linked to a specific quiz file (by its fileId)
}