package com.example.habittracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habittracker.R
import com.example.habittracker.adapters.RecyclerViewAdapter
import com.example.habittracker.databinding.FragmentHabitsBinding
import com.example.habittracker.enums.HabitType
import com.example.habittracker.viewmodels.HabitListViewModel

class HabitsFragment : Fragment(R.layout.fragment_habits) {

    private lateinit var habitListViewModel: HabitListViewModel
    private lateinit var binding: FragmentHabitsBinding

    private lateinit var type: String

    companion object {
        private const val TAG = "bad_habits_fragment"
        private const val HABITS_TYPE = "habits_type"

        fun newInstance(type: String): HabitsFragment {
            val fragment = HabitsFragment()
            val args = Bundle().apply {
                putString(HABITS_TYPE, type)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            type = it.getString(HABITS_TYPE) ?: ""
        }

        habitListViewModel = ViewModelProvider(requireActivity())[HabitListViewModel::class.java]
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

        when (type) {
            HabitType.GoodHabit.description -> {
                binding.listTitle.text = HabitType.GoodHabits.description
            }
            HabitType.BadHabit.description -> {
                binding.listTitle.text = HabitType.BadHabits.description
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = RecyclerViewAdapter { id -> openAddItemFragment(id) }
        binding.recyclerView.adapter = adapter

        habitListViewModel.items.observe(viewLifecycleOwner) { items ->
            adapter.submit(items.filter { item -> item.type == type })
        }

        binding.searchFab.setOnClickListener {
            val filterSortBottomSheet = BottomSheetFragment()
            filterSortBottomSheet.show(parentFragmentManager, BottomSheetFragment::class.java.simpleName)
        }

        binding.addingFab.setOnClickListener {
            openAddItemFragment()
        }
    }

    private fun openAddItemFragment(id: String? = null) {
        val bundle = Bundle().apply {
            putString("id", id)
        }

        findNavController().navigate(R.id.addHabitFragment, bundle)
    }
}