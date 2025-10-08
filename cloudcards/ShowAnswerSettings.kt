package com.example.cloudcards

// imports for context, shared prefs, ViewModel, StateFlow
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// manages my SharedPreferences for saving simple app settings
class PrefManager(context: Context) {
    private val prefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    // initializes my shared preferences file "my_prefs"
    fun getShowAnswer(): Boolean = prefs.getBoolean("show_answer", false)
    // reads whether I want to show the answer, default false
    fun setShowAnswer(state: Boolean) = prefs.edit { putBoolean("show_answer", state) }
    // saves whether I want to show the answer
}

// ViewModel that manages my preference state reactively
class PrefViewModel(private val prefManager: PrefManager) : ViewModel() {
    private val _showAnswer = MutableStateFlow(prefManager.getShowAnswer())
    // mutable state flow holding my "show_answer" state initially from prefs

    val showAnswer: StateFlow<Boolean> = _showAnswer
    // read-only StateFlow for observing in Compose screens

    fun setShowAnswer(state: Boolean) {
        _showAnswer.value = state // updates the current state
        prefManager.setShowAnswer(state) // saves the state in SharedPreferences
    }
}

// Factory to provide PrefViewModel with PrefManager dependency injection
@Suppress("UNCHECKED_CAST")
class PrefViewModelFactory(
    private val prefManager: PrefManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrefViewModel::class.java)) {
            return PrefViewModel(prefManager) as T // returns my PrefViewModel with prefManager
        }
        throw IllegalArgumentException("Unknown ViewModel class") // safe guard for unknown ViewModels
    }
}