package com.jacksafblaze.notes.database

import androidx.room.Database

@Database(entities = [NoteDbDto::class], version = 1)
abstract class NoteDatabase {
    abstract fun noteDao(): NoteDao
}