package com.example.habittracker.presentation.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habittracker.R
import com.example.habittracker.dagger.App
import com.example.habittracker.databinding.FragmentHabitsBinding
import com.example.habittracker.domain.enums.HabitType
import com.example.habittracker.presentation.adapters.HabitDecoration
import com.example.habittracker.presentation.adapters.RecyclerViewAdapter
import com.example.habittracker.presentation.viewmodels.HabitListViewModel
import com.example.habittracker.presentation.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class HabitFragment : Fragment(R.layout.fragment_habits) {
    private lateinit var binding: FragmentHabitsBinding

    private lateinit var habitType: HabitType

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val habitListViewModel: HabitListViewModel by activityViewModels { viewModelFactory }

    companion object {
        private const val TAG = "bad_habits_fragment"
        private const val HABITS_TYPE = "habits_type"

        fun newInstance(type: HabitType): HabitFragment {
            val fragment = HabitFragment()
            val args = Bundle().apply {
                putParcelable(HABITS_TYPE, type)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (requireActivity().application as App).appComponent.inject(this)


        arguments?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                habitType = it.getParcelable(HABITS_TYPE, HabitType::class.java)!!
            } else {
                @Suppress("DEPRECATION")
                habitType = it.getParcelable(HABITS_TYPE)!!
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHabitsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = habitListViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (habitType) {
            HabitType.GoodHabit-> {
                binding.listTitle.text = resources.getString(R.string.good_habits)
            }
            HabitType.BadHabit -> {
                binding.listTitle.text = resources.getString(R.string.bad_habits)
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = RecyclerViewAdapter(
            onItemClicked  = { id -> openAddItemFragment(id) },
            onCompleteClicked = { id -> complete(id) },
            onDeleteClicked = { id -> delete(id) }
        )
        binding.recyclerView.addItemDecoration(HabitDecoration(16))
        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            when (habitType) {
                HabitType.GoodHabit -> habitListViewModel.goodHabits.collect { habits ->
                    adapter.submit(habits)
                }
                HabitType.BadHabit -> habitListViewModel.badHabits.collect { habits ->
                    adapter.submit(habits)
                }
            }
        }

        binding.searchFab.setOnClickListener {
            val filterSortBottomSheet = BottomSheetFragment()
            filterSortBottomSheet.show(
                parentFragmentManager,
                BottomSheetFragment::class.java.simpleName
            )
        }

        binding.addingFab.setOnClickListener {
            openAddItemFragment()
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            habitListViewModel.sync()
            viewLifecycleOwner.lifecycleScope.launch {
                habitListViewModel.syncComplete.collect { isComplete ->
                    if (isComplete) {
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                }
            }
        }
    }

    private fun complete(id: String) {
        habitListViewModel.saveCompletedDate(id, habitType, requireContext())
    }

    private fun delete(id: String) {
        habitListViewModel.deleteHabit(id)
    }

    private fun openAddItemFragment(id: String? = null) {
        val bundle = Bundle().apply {
            putString("id", id)
        }

        findNavController().navigate(R.id.addHabitFragment, bundle)
    }
}