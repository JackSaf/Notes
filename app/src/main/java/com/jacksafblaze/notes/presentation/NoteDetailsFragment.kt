package com.jacksafblaze.notes.presentation

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
import com.jacksafblaze.notes.R
import com.jacksafblaze.notes.databinding.FragmentNoteDetailsBinding
import kotlinx.coroutines.launch

class NoteDetailsFragment : Fragment() {
    private val viewModel: NoteDetailsViewModel by viewModels()
    private var _binding: FragmentNoteDetailsBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        val noteId = arguments?.get("noteId")
        viewModel.setNote(1)
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
                    R.id.update_item-> {
                        viewModel.updateCurrentNote()
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
    private fun setupTitleEditText(){
        binding.title.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                viewModel.setTitle(title = p0.toString())
            }

        })
    }

    private fun setupDescriptionEditText(){
        binding.title.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                viewModel.setDescription(description = p0.toString())
            }
        })
    }
    private fun bindState(){
        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect{state ->
                    binding.title.setText(state.noteTitle)
                    binding.description.setText(state.noteDescription)
                }
            }
        }
    }
}