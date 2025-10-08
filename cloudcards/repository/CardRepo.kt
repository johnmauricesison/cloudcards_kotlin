package com.example.cloudcards.repository

import com.example.cloudcards.dao.CardDao
import com.example.cloudcards.database.CardEntity

class CardRepository(private val dao: CardDao) {
    suspend fun insert(card: CardEntity): Long = dao.insert(card)
    // inserts a card into my database using CardDao, returns the inserted ID

    suspend fun getAll(): List<CardEntity> = dao.getAll()
    // fetches all cards stored in my database

    suspend fun update(card: CardEntity) = dao.update(card)
    // updates a card in my database

    suspend fun delete(card: CardEntity) = dao.delete(card)
    // deletes a card from my database

    suspend fun getByFileId(fileId: Int): List<CardEntity> = dao.getByFileId(fileId)
    // fetches all cards belonging to a specific quiz file by fileId

}