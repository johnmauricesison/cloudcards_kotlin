package com.example.cloudcards

// imports for Application, Room, migrations, coroutine background setup
import android.app.Application
import androidx.core.content.edit
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.cloudcards.database.CardEntity
import com.example.cloudcards.database.FileEntity
import com.example.cloudcards.database.QuizDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class App : Application() { // this makes my App class the base for the entire app

    lateinit var database: QuizDatabase
        private set // exposes my Room database to other parts of the app safely

    private val appScope = CoroutineScope(Dispatchers.IO) // sets up a background coroutine scope using IO dispatcher

    override fun onCreate() {
        super.onCreate() // calls Application's onCreate
        // defines migration from version 2 to 3, adding columns to FileEntity table
        val migration2to3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE FileEntity ADD COLUMN tags TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE FileEntity ADD COLUMN isFav INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE FileEntity ADD COLUMN isTrashed INTEGER NOT NULL DEFAULT 0")
            }
        }

        // initializes my Room database with migration support
        database = Room.databaseBuilder(
            applicationContext,
            QuizDatabase::class.java,
            "quiz_db"
        )
            .addMigrations(migration2to3)
            .build()

        // checks if this is my app's first launch
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val isFirstLaunch = prefs.getBoolean("isFirstLaunch", true)

        if (isFirstLaunch) {
            // if first launch, populate my database with default data in the background
            appScope.launch {
                val fileDao = database.fileDao()
                val file = FileEntity(
                    name = "General Information Quiz",
                )
                val fileId = fileDao.insert(file).toInt()
                // inserts a quiz file and gets its ID
                val cardDao = database.cardDao()
                // gets the card DAO

                val cards = listOf<List<String>>(
                    listOf("What's the color of the cherry?", "Red"),
                    listOf("What's the color of the lava?", "Orange"),
                    listOf("What's the color of the sand?", "Yellow"),
                    listOf("What's the color of the grass?", "Green"),
                    listOf("What's the color of the sky?", "Cyan"),
                    listOf("What's the color of the sea?", "Blue"),
                    listOf("What's the rarest color for the flags?", "Purple"),
                    listOf("What's the color of rose?", "Pink"),
                    listOf("What's the color of black pants?", "Black"),
                    listOf("What's the color of sadness", "Gray"),
                    listOf("What's the color of the paper?", "White"),
                    listOf("What's the color of the ground?", "Brown")
                )
                // defines default flashcards to insert

                cards.forEach {cardDao.insert(CardEntity(
                    side1 = it[0], // question
                    side2 = it[1], // answer
                    fileId = fileId, // link to my quiz file
                    fixedOptions = it.size > 2, // if there are fixed options
                    incorrectOption1 = it.getOrNull(2) ?: "",
                    incorrectOption2 = it.getOrNull(3) ?: "",
                    incorrectOption3 = it.getOrNull(4) ?: "",
                ))}
                prefs.edit { putBoolean("isFirstLaunch", false) }
                // marks that the app has launched once, so this data doesn't insert again
            }.start() // starts the coroutine
        }
    }
}