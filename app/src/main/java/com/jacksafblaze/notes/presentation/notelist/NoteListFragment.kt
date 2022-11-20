package com.jacksafblaze.notes.presentation.notelist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jacksafblaze.notes.R
import com.jacksafblaze.notes.databinding.FragmentNoteListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

private const val dataStoreKey = "IS_LAUNCHED_FOR_FIRST_TIME"

@AndroidEntryPoint
class NoteListFragment : Fragment() {
    @Inject
    lateinit var dataStore: DataStore<Preferences>
    private var _binding: FragmentNoteListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NoteListViewModel by viewModels()

    private lateinit var adapter: NoteAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var smoothScroller: RecyclerView.SmoothScroller
    private lateinit var itemTouchCallback : ItemTouchHelper.SimpleCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSmoothScroller()
        initItemTouchCallback()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteListBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateDataAtTwelveOclock()
        setupRecyclerView()
        setupFab()
        appLaunchedForTheFirstTime()
        bindState()
    }

    private fun setupRecyclerView() {
        initAdapter()
        initLinearLayoutManager()
        binding.noteList.layoutManager = linearLayoutManager
        binding.noteList.adapter = adapter
        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(binding.noteList)
    }

    private fun setupFab() {
        binding.addFab.setOnClickListener {
            viewModel.addDefaultNote()
        }
    }

    private fun appLaunchedForTheFirstTime() = viewLifecycleOwner.lifecycleScope.launch {
        val key = booleanPreferencesKey(dataStoreKey)
        val preferences = dataStore.data.first()
        val firstTime = preferences[key]
        viewModel.setLaunchedForTheFirstTime(firstTime == null || firstTime == false)
    }

    private suspend fun updateFirstTimeStatus() {
        val key = booleanPreferencesKey(dataStoreKey)
        dataStore.edit { preferences ->
            preferences[key] = true
        }
    }

    private fun bindState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if (state.isLaunchedForTheFirstTime) {
                        binding.addFab.isEnabled = false
                        binding.firstTimeProgressBar.visibility =
                            if (state.isLoading) View.VISIBLE else View.GONE
                        binding.message.visibility =
                            if (state.networkMessage != null) View.VISIBLE else View.GONE
                        binding.message.text = state.networkMessage
                    } else {
                        updateFirstTimeStatus()
                        binding.firstTimeProgressBar.visibility = View.GONE
                        binding.addFab.isEnabled = true
                        binding.fetchingProgressBar.visibility =
                            if (state.isLoading) View.VISIBLE else View.GONE
                        binding.message.visibility =
                            if (state.dataMessage != null) View.VISIBLE else View.GONE
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

    @SuppressLint("NotifyDataSetChanged")
    private fun updateDataAtTwelveOclock() = viewLifecycleOwner.lifecycleScope.launch{
        val calendar = Calendar.getInstance()
        val currentTime = System.currentTimeMillis()
        calendar.timeInMillis = currentTime
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val timeShift = calendar.timeInMillis - currentTime
        delay(timeShift)
        adapter.notifyDataSetChanged()
    }

    private fun initAdapter(){
        adapter = NoteAdapter(
            { itemId ->
                val navController = findNavController()
                if (navController.currentDestination!!.id == R.id.noteListFragment) {
                    val action =
                        NoteListFragmentDirections.actionNoteListFragmentToNoteDetailsFragment(
                            itemId
                        )
                    navController.navigate(action)
                }
            },
            { scrollPosition ->
                smoothScroller.targetPosition = scrollPosition
                linearLayoutManager.startSmoothScroll(smoothScroller)
            }
        )
    }
    private fun initLinearLayoutManager(){
        linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
    }
    private fun initItemTouchCallback(){
        itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            0, (ItemTouchHelper.LEFT.or(
                ItemTouchHelper.RIGHT
            ))
        ) {
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
    }
    private fun initSmoothScroller(){
        smoothScroller = object : LinearSmoothScroller(requireContext()) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
    }

}