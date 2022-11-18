package com.jacksafblaze.notes.presentation.notedetails

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.jacksafblaze.notes.R
import com.jacksafblaze.notes.databinding.FragmentNoteDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NoteDetailsFragment : Fragment() {
    private val viewModel: NoteDetailsViewModel by viewModels()
    private var _binding: FragmentNoteDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteDetailsBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTitleEditText()
        setupDescriptionEditText()
        val noteId = arguments?.getInt("noteId")
        noteId?.let { id ->
            viewModel.setNote(id)
        }
        prepareMenu()
        bindState()
    }

    private fun prepareMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.note_details_fragment_toolbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.update_item -> {
                        viewModel.updateCurrentNote()
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupTitleEditText() {
        binding.title.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                viewModel.setTitle(title = p0.toString())
            }

        })
    }

    private fun setupDescriptionEditText() {
        binding.description.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                viewModel.setDescription(description = p0.toString())
            }
        })
    }

    private fun bindState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if(!state.noteIsShown) {
                        state.note?.let { note ->
                            binding.title.setText(note.title)
                            binding.description.setText(note.description)
                            viewModel.noteShown()
                        }
                    }
                    if (state.isSuccessfullyUpdated) {
                        val navController = findNavController()
                        if (navController.currentDestination!!.id == R.id.noteDetailsFragment) {
                            navController.navigateUp()
                        }
                    }
                }
            }
        }
    }
}