package com.jacksafblaze.notes.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteDbDto(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    @ColumnInfo(name = "last_changes_date")val lastChangesDate: String
)