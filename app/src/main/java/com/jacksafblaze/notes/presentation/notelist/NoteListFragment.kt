package com.jacksafblaze.notes.presentation.notelist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class NoteListFragment : Fragment() {
    private var _binding: FragmentNoteListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NoteListViewModel by viewModels()

    private lateinit var adapter: NoteAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var smoothScroller: RecyclerView.SmoothScroller
    private lateinit var itemTouchCallback : ItemTouchHelper.SimpleCallback


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
        bindState()
    }

    private fun setupRecyclerView() {
        initAdapter()
        initLinearLayoutManager()
        initItemTouchCallback()
        binding.noteList.layoutManager = linearLayoutManager
        binding.noteList.adapter = adapter
        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(binding.noteList)
    }

    private fun setupFab() {
        binding.addFab.setOnClickListener {
            viewModel.addDefaultNote()
        }
    }


    private fun bindState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    state.errorMessage?.let { message ->
                        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()  //если случилась непредвиденная ошибка, показываем ее в снек баре
                        viewModel.errorMessageShown()
                    }
                    if (state.firstTimeLaunch) {                                              //Проверка или была уже успешно завершена первая загрузка
                        binding.fetchingProgressBar.visibility = View.GONE                    //убираем горизонтальный прогресс
                        binding.addFab.isEnabled = false                                      //фаб тыкать нельзя

                        binding.firstTimeProgressBar.visibility =                             //у первой загрузки круглый прогресс
                            if (state.isLoading) View.VISIBLE else View.GONE
                        binding.message.visibility =

                            if (state.noNetworkMessage != null) View.VISIBLE else View.GONE   //при прерывании первой загрузки сообщение об интернете высвечивается посередине
                        binding.message.text = state.noNetworkMessage
                    } else {
                        binding.firstTimeProgressBar.visibility = View.GONE                   //убираем круглый прогресс
                        binding.addFab.isEnabled = true                                       //фаб теперь можно тыкать

                        binding.fetchingProgressBar.visibility =                              //теперь прогресс бар у нас сверху, с ним и работаем
                            if (state.isLoading) View.VISIBLE else View.GONE
                        binding.message.visibility =

                            if (state.noDataMessage != null) View.VISIBLE else View.GONE      //посередине высвечивается уже сообщение о данных, а не об интернете
                        binding.message.text = state.noDataMessage

                        state.noNetworkMessage?.let { message ->
                            Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()  //сообщение об интернете подсвечивается в снекбаре
                            viewModel.networkMessageShown()                                     //вьюмодели сообщается, что сообщение показано
                        }

                        state.noteList?.let { list ->
                            adapter.setList(list)          //обновляем список, хотя бы тут все понятно :)
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateDataAtTwelveOclock() = viewLifecycleOwner.lifecycleScope.launch{
        val calendar = Calendar.getInstance()
        val currentTime = System.currentTimeMillis()        //узнаем теперешнее время
        calendar.timeInMillis = currentTime
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val timeShift = calendar.timeInMillis - currentTime //считаем разницу с 00:00 следующего дня
        delay(timeShift)                                    //делаем задержку в эту разницу
        adapter.notifyDataSetChanged()                      //по окончании обновляем список
    }

    private fun initAdapter(){
        initSmoothScroller()
        adapter = NoteAdapter(
            { itemId ->                                     //при клике на записку переходим на нее, передавая фрагменту ее айди
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
    private fun initLinearLayoutManager(){  //переворачиваем список и делаем, чтобы записки добавлялись наверх
        linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
    }
    private fun initItemTouchCallback(){    //для удаления взмахом
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
    private fun initSmoothScroller(){     //для плавного скрола списка наверх при добавлении нового элемента
        smoothScroller = object : LinearSmoothScroller(requireContext()) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
    }

}