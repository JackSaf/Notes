package com.jacksafblaze.notes.database.model

import androidx.room.Database

@Database(entities = [NoteDbDto::class], version = 1)
abstract class NoteDatabase {
    abstract fun noteDao(): NoteDao
}