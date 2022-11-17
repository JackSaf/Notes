package com.jacksafblaze.notes.domain.model

data class Note(val id: Int,
                val title: String,
                val description: String,
                val lastChangeDate: String,
                val isMadeToday: Boolean)
