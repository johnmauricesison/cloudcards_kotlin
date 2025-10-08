package com.example.cloudcards.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.cloudcards.repository.CardRepository
import com.example.cloudcards.database.CardEntity

class CardViewModel(private val repository: CardRepository) : ViewModel() {
    private val _cards = MutableStateFlow<List<CardEntity>>(emptyList())
    // private mutable state flow holding my list of cards

    val cards: StateFlow<List<CardEntity>> = _cards
    // public read-only state flow exposed to my UI for observing cards reactively


    fun getAll() {
        viewModelScope.launch {
            _cards.value = repository.getAll()
            // fetches all cards from my repository and updates the state flow
        }
    }

    fun clear() {
        _cards.value = emptyList()
        // clears the current list in memory (does not delete from the database)
    }

    fun insert(card: CardEntity) {
        viewModelScope.launch {
            repository.insert(card)
            getAll()
            // inserts a new card into my database, then refreshes the list
        }
    }

    fun set(card: CardEntity) {
        viewModelScope.launch {
            repository.update(card)
            getAll()
            // updates an existing card, then refreshes the list
        }
    }

    fun delete(card: CardEntity) {
        viewModelScope.launch {
            repository.delete(card)
            getAll()
            // deletes a card, then refreshes the list
        }
    }

    fun getByFileId(fileId: Int) {
        viewModelScope.launch {
            _cards.value = repository.getByFileId(fileId)
            // fetches cards belonging to a specific fileId and updates the state flow
        }
    }
}