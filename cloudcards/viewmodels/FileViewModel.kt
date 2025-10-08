package com.example.cloudcards.viewmodels

import com.example.cloudcards.repository.FileRepository
import com.example.cloudcards.database.FileEntity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FileViewModel(private val repository: FileRepository) : ViewModel() {
    private val _files = MutableStateFlow<List<FileEntity>>(emptyList())
    val files: StateFlow<List<FileEntity>> = _files
    // StateFlow holding my current filtered list of quiz files for my UI to observe

    private val _tags = MutableStateFlow<List<String>>(emptyList())
    val tags: StateFlow<List<String>> = _tags
    // StateFlow holding my unique tags for filters in the UI

    init {
        viewModelScope.launch {
            loadAll()
            // on ViewModel init, load all files immediately for the UI
        }
    }

    suspend fun getAll(): List<FileEntity> = repository.getAll() // fetches all quiz files from my database

    fun loadAll() {
        viewModelScope.launch {
            _files.value = getAll()
            // updates my StateFlow with all quiz files
        }
    }

    suspend fun insert(file: FileEntity) {
        repository.insert(file)
        // inserts a new quiz file into my database
    }

    suspend fun set(file: FileEntity) {
        repository.update(file)
        // updates an existing quiz file in my database
    }

    fun delete(file: FileEntity) {
        viewModelScope.launch {
            repository.delete(file)
            // deletes a quiz file from my database
        }
    }

    fun containsTag(file: FileEntity, tag: String): Boolean {
        val tags = file.tags.split("|")
        return tag in tags
        // checks if a quiz file contains a specific tag
    }

    fun getAllNonTrashed() {
        viewModelScope.launch {
            val files = getAll()
            _files.value = files
                .filter { !it.isTrashed }
                .sortedByDescending { it.isFav }
            // filters to show only non-trashed files, sorted with favorites on top
        }
    }

    fun getByTag(tag: String) {
        viewModelScope.launch {
            val files = getAll()
            _files.value = files
                .filter { containsTag(it, tag) && !it.isTrashed }
                .sortedByDescending { it.isFav }
            // filters files by tag while excluding trashed files, sorted with favorites on top
        }
    }

    fun getAllFav() {
        viewModelScope.launch {
            val files = getAll()
            _files.value = files.filter { it.isFav && !it.isTrashed }
            // filters to show only favorite, non-trashed quiz files
        }
    }

    fun getAllTrashed() {
        viewModelScope.launch {
            val files = getAll()
            _files.value = files
                .filter { it.isTrashed }
                .sortedByDescending { it.isFav }
            // filters to show only trashed quiz files, sorted with favorites on top
        }
    }

    suspend fun getAllTags(): MutableList<String> {
        val files = getAll().filter {!it.isTrashed}
        var result = mutableListOf<String>()

        files.forEach { file ->
            if (!file.isTrashed) {
                for (tag in file.tags.split("|")) {
                    if (tag !in result) {
                        result.add(tag)
                    }
                }
            }
        }

        return result
        // returns a unique list of all tags from non-trashed files for filtering
    }

    suspend fun clearTrash() {
        val files = repository.getAll()
        val trashed = files.filter { it.isTrashed }
        trashed.forEach { file ->
            repository.delete(file)
        }
        // permanently deletes all trashed quiz files from my database
    }

    suspend fun refreshTags() {
        _tags.value = getAllTags()
        // refreshes the tags StateFlow with current tags from the database
    }

    fun getFiles(tag: String) {
        when (tag) {
            "all" -> getAllNonTrashed()
            "favorite" -> getAllFav()
            "trash" -> getAllTrashed()
            else -> getByTag(tag)
            // decides what files to load based on the tag filter selected in my UI
        }
    }
}