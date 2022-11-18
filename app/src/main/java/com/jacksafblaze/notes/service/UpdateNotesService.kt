package com.jacksafblaze.notes.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.jacksafblaze.notes.domain.repository.NoteRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class UpdateNotesService: Service() {
    @Inject
    lateinit var noteRepository: NoteRepository
    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }
    fun updateNotes() = scope.launch{

    }
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}