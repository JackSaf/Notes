package com.jacksafblaze.notes.presentation.notelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.jacksafblaze.notes.databinding.NoteListItemBinding
import com.jacksafblaze.notes.domain.model.Note
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class NoteAdapter(
    val onItemClickCallback: (Int) -> Unit,
    val scrollCallback: (Int) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val itemCallback = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, itemCallback).apply {
        addListListener { previousList, currentList ->
            if (currentList.size > previousList.size) {           //слушатель для скрола на первый элемент списка
                scrollCallback.invoke(currentList.size - 1)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NoteListItemBinding.inflate(inflater, parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setList(list: List<Note>) {
        differ.submitList(list)
    }

    inner class NoteViewHolder(private val binding: NoteListItemBinding) :
        ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.title.text = note.title
            binding.description.text = note.description
            val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val dt = LocalDateTime.parse(note.lastChangesDate, dateTimeFormatter)
            val dateString: String = formatDate(dt)
            binding.lastChangeDate.text = dateString
            binding.rootItemLayout.setOnClickListener {
                onItemClickCallback.invoke(note.id)
            }
        }

        private fun formatDate(dt: LocalDateTime): String {
            val currentDt = LocalDateTime.now()
            val yearIsCurrent = dt.year == currentDt.year
            val monthIsCurrent =
                dt.month == currentDt.month                //Проверяем или записка сегодняшняя
            val dayIsCurrent = dt.dayOfMonth == currentDt.dayOfMonth
            val isMadeToday = yearIsCurrent && monthIsCurrent && dayIsCurrent
            return if (isMadeToday) {
                val hourString = if(dt.hour < 10){
                    "0${dt.hour}"
                }
                else{
                    dt.hour.toString()
                }
                val minuteString: String = if (dt.minute < 10) {
                    "0${dt.minute}"
                } else {
                    dt.minute.toString()
                }
                "$hourString:$minuteString"
            } else {                                                        //Форматируем соответственно
                val dayOfMonthString: String = if (dt.dayOfMonth < 10) {
                    "0${dt.dayOfMonth}"
                } else {
                    dt.dayOfMonth.toString()
                }
                val monthString = if (dt.monthValue < 10) {
                    "0${dt.monthValue}"
                } else {
                    dt.monthValue.toString()
                }
                val yearString = dt.year.toString()
                "$dayOfMonthString.$monthString.$yearString"
            }
        }
    }
}