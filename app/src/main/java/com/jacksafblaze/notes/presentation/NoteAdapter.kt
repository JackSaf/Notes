package com.jacksafblaze.notes.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.jacksafblaze.notes.databinding.NoteListItemBinding
import com.jacksafblaze.notes.domain.model.Note

class NoteAdapter(
    val onItemClickCallback: (Int) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    private val itemCallback = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, itemCallback)

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
            binding.lastChangeDate.text = note.date
            binding.rootItemLayout.setOnClickListener{
                onItemClickCallback.invoke(note.id)
            }
        }
    }
}