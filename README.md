# ☁️ CloudCards – Kotlin Flashcards App

**Author:** John Maurice P. Sison  
**Project:** Final Project – Kotlin Application Development  
**Date:** 2025  

---

## 📘 Overview

**CloudCards** is a Kotlin-based flashcards app designed to help users **memorize and review topics interactively**.  
Each flashcard contains a **question** and its corresponding **answer**, allowing users to study using two engaging modes:

- 🧠 **Training Mode:** Presents a question and lets the user recall the answer.  
- ✅ **True/False Mode:** Presents a statement and asks the user to judge if it’s correct.  

The app uses **Room Database** for local data persistence, allowing users to create, edit, and organize flashcards while tracking their learning progress.  

---

## 🧭 Features

### 🏠 Main Screen
- Displays all quizzes (flashcard sets)
- Floating Action Button (FAB) to **add new quizzes**

### ✏️ Card Creation Screen
- Form fields for **question** and **answer**
- Options to **save** or **cancel**
- FAB to start quiz gameplay

### 🎮 Quiz/Game Screen
- Choose between **Training Mode** and **True/False Mode**
- Option to set the **number of questions** or play with all cards
- Displays current question and interactive answer/true-false buttons
- Shows **final score** after completion

### 🧩 Quiz & Card Editor
- Edit, rename, tag (e.g. `#androidcourse`), delete, or favorite quizzes
- Edit or delete cards within a quiz
- Clean top bar UI for editing actions

### 📂 Sidebar
- Filter quizzes: **All Cards**, **Favorites**, **Tags**, or **Recently Deleted**
- **Logout** button for session management

### 🗑 Trash Management
- Restore deleted quizzes
- Permanently clear trash using FAB

### ⚙️ App Settings
- Checkbox to show answers automatically in True/False mode

---

## 🗂 Packages & Key Files

### 🗄 DAO Package
**`CardDao.kt`**  
- Defines CRUD operations for flashcards.  
- Uses coroutines (`suspend` functions) for async operations.  
- Includes `getByFileId()` to fetch cards belonging to a specific quiz.  

**`FileDao.kt`**  
- Manages quiz files with conflict-safe operations using `OnConflictStrategy.REPLACE`.  
- Supports insert, update, delete, and fetch methods.

---

### 🧩 Database Package
**`Database.kt`**  
- Defines the Room database (version 3).  
- Uses `CardEntity` and `FileEntity` as tables.  
- Provides DAOs for card and file management.

**`CardEntity.kt`**  
- Represents individual flashcards linked to a quiz (`fileId`).  
- Fields include `question`, `answer`, and optional multiple-choice options.

**`FileEntity.kt`**  
- Represents a quiz (flashcard file).  
- Includes metadata such as name, tags, favorite status, and trash status.

---

### 💾 Repository Package
Acts as a bridge between the **DAO layer** and **ViewModels**, managing clean data access.

- **`CardRepo.kt`** – Handles all card-related data operations.  
- **`FileRepo.kt`** – Handles quiz-level data operations.  

---

### 🧠 ViewModels Package
Implements **MVVM architecture** using **StateFlow** and **coroutines**.

- **`CardViewModel.kt`** – Manages flashcard CRUD and UI state.  
- **`FileViewModel.kt`** – Manages quizzes, filtering, favorites, and trash.  
- **`CardViewModelSettings.kt`** & **`FileViewModelSettings.kt`** – Provide factory initialization for dependency injection.

---

### 🪄 Quizlist Package
Contains all **Compose UI screens** and dialogs for managing and displaying quizzes.

- **`FileScreen.kt`** – Main quiz list screen.  
- **`FileItem.kt`** – Displays each quiz as a clickable item.  
- **`CreateFileDialog.kt`** – Dialog for creating new quizzes.  
- **`SettingsDialog.kt`** – Manages app-level preferences.  
- **`ClearTrashDialog.kt`**, **`RestoreDialog.kt`** – Trash management dialogs.  

---

### ✏️ Editor Package
Contains screens for **creating, editing, and playing** cards within a quiz.

- **`EditorScreen.kt`** – Main card editing screen.  
- **`CreateCardDialog.kt`**, **`EditCardDialog.kt`**, **`DeleteDialog.kt`** – Manage flashcards.  
- **`PlayDialog.kt`**, **`PlaySettingsDialog.kt`** – Configure game settings and play modes.  
- **`TagDialog.kt`**, **`RenameDialog.kt`** – Manage quiz metadata.  
- **`EditorCardList.kt`**, **`EditorCard.kt`** – Display cards within a quiz.  

---

### ⚙️ Core Application Files

- **`MainActivity.kt`** – Initializes app UI, theme, and navigation.  
- **`Navigation.kt`** – Handles Jetpack Compose navigation between screens.  
- **`App.kt`** – Initializes Room database, inserts demo data on first launch.  
- **`ShowAnswerSettings.kt`** – Manages app preferences via `SharedPreferences` and `PrefViewModel`.

---

## 🧠 Architecture Summary

**Architecture Pattern:** MVVM (Model-View-ViewModel)  
**UI Framework:** Jetpack Compose  
**Local Storage:** Room Database  
**Dependency Management:** ViewModel Factories + Repositories  
**Reactive State Management:** Kotlin Coroutines + StateFlow  

---

## 🧰 Tech Stack

- **Language:** Kotlin  
- **Framework:** Android Jetpack Compose  
- **Database:** Room  
- **Architecture:** MVVM  
- **Async Handling:** Kotlin Coroutines  
- **UI Components:** Material 3 Design  
- **Navigation:** Jetpack Navigation Compose  
- **Persistence:** SharedPreferences (for settings)  

---

## 🚀 Future Enhancements

- Add online cloud sync for flashcards  
- Implement user authentication with Firebase  
- Include image/audio cards for multimedia learning  
- Add spaced-repetition algorithms for optimized memorization  
- Introduce quiz difficulty levels and progress tracking  

---

## 🧾 Source Code

You can access the complete CloudCards project source here:  
🔗 [Google Drive Folder](https://drive.google.com/drive/folders/1tE58CyK2aogNOzvP48cC_2owBPlukW_8?usp=sharing)

---


## 🙏 Acknowledgments

Developed as part of the **Final Kotlin Project**.  
Special thanks to **Android Jetpack**, **Kotlin Coroutines**, and **Room Database** for powering the app’s clean and responsive design.

