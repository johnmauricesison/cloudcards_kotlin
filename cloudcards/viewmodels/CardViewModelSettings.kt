package com.example.cloudcards.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cloudcards.repository.CardRepository

@Suppress("UNCHECKED_CAST")
class CardViewModelSettings(
    private val repository: CardRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardViewModel::class.java)) {
            return CardViewModel(repository) as T
            // returns my CardViewModel instance with repository injected
        }
        throw IllegalArgumentException("Unknown ViewModel class")
        // safety check to prevent providing unsupported ViewModels
    }
}