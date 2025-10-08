package com.example.cloudcards.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.cloudcards.database.FileEntity

@Dao
interface FileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(file: FileEntity): Long
    // inserts a FileEntity (quiz file) into my database
    // replaces the file if a conflict on primary key occurs
    // returns the inserted row ID as Long

    @Query("SELECT * FROM files")
    suspend fun getAll(): List<FileEntity>
    // fetches all quiz files stored in my "files" table

    @Update
    suspend fun update(file: FileEntity)
    // updates the provided FileEntity in my database

    @Delete
    suspend fun delete(file: FileEntity)
    // deletes the provided FileEntity from my database
}