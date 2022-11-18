package com.jacksafblaze.notes.presentation.notelist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jacksafblaze.notes.R
import com.jacksafblaze.notes.databinding.FragmentNoteListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NoteListFragment : Fragment() {
    private var _binding: FragmentNoteListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NoteAdapter
    private val viewModel: NoteListViewModel by viewModels()
    private val itemTouchCallback = object: ItemTouchHelper.SimpleCallback(0, (ItemTouchHelper.LEFT.or(
        ItemTouchHelper.RIGHT))){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val pos = viewHolder.layoutPosition
            val item = adapter.differ.currentList[pos]
            viewModel.deleteNote(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupFab()
        bindState()
    }

    fun setupRecyclerView(){
        adapter = NoteAdapter({itemId ->

        })
        binding.noteList.layoutManager = LinearLayoutManager(requireContext())
        binding.noteList.adapter = adapter
        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(binding.noteList)
    }

    fun setupFab(){
        binding.addFab.setOnClickListener{
            viewModel.addDefaultNote()
        }
    }

    fun bindState(){
        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect{state ->
                    if(state.isLaunchedForTheFirstTime){
                        binding.firstTimeProgressBar.visibility = if(state.isLoading) View.VISIBLE else View.GONE
                        binding.message.visibility = if(state.networkMessage != null) View.VISIBLE else View.GONE
                        binding.message.text = state.networkMessage

                    }
                    else{
                        binding.fetchingProgressBar.visibility = if(state.isLoading) View.VISIBLE else View.GONE
                        binding.message.visibility = if(state.dataMessage != null) View.VISIBLE else View.GONE
                        binding.message.text = state.dataMessage
                        state.networkMessage?.let { message ->
                            Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
                            viewModel.networkMessageShown()
                        }
                        state.noteList?.let { list ->
                            adapter.setList(list)
                        }
                    }
                }
            }
        }
    }
}