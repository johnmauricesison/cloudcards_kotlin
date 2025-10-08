package com.example.cloudcards.repository

import com.example.cloudcards.dao.FileDao
import com.example.cloudcards.database.FileEntity

class FileRepository(private val dao: FileDao) {
    suspend fun insert(file: FileEntity): Long = dao.insert(file)
    // inserts a quiz file into my database, returns the inserted ID

    suspend fun getAll(): List<FileEntity> = dao.getAll()
    // fetches all quiz files from my database

    suspend fun update(file: FileEntity) = dao.update(file)
    // updates a quiz file in my database

    suspend fun delete(file: FileEntity) = dao.delete(file)
    // deletes a quiz file from my database

}