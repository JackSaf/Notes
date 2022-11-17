package com.jacksafblaze.notes.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NoteDbDto::class], version = 1)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao
}