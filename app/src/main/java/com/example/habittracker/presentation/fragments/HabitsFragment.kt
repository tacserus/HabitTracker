package com.example.habittracker.presentation.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habittracker.R
import com.example.habittracker.data.database.App
import com.example.habittracker.databinding.FragmentHabitsBinding
import com.example.habittracker.domain.enums.HabitType
import com.example.habittracker.presentation.adapters.RecyclerViewAdapter
import com.example.habittracker.presentation.viewmodels.HabitListViewModel
import javax.inject.Inject

class HabitsFragment : Fragment(R.layout.fragment_habits) {
    private lateinit var binding: FragmentHabitsBinding

    private lateinit var habitType: HabitType

    @Inject
    lateinit var habitListViewModel: HabitListViewModel

    companion object {
        private const val TAG = "bad_habits_fragment"
        private const val HABITS_TYPE = "habits_type"

        fun newInstance(type: HabitType): HabitsFragment {
            val fragment = HabitsFragment()
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
        val adapter = RecyclerViewAdapter { id -> openAddItemFragment(id) }
        binding.recyclerView.adapter = adapter

        when (habitType) {
            HabitType.GoodHabit -> habitListViewModel.goodHabits.observe(viewLifecycleOwner) {
                adapter.submit(it)
            }
            HabitType.BadHabit -> habitListViewModel.badHabits.observe(viewLifecycleOwner) {
                adapter.submit(it)
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
    }

    private fun openAddItemFragment(id: String? = null) {
        val bundle = Bundle().apply {
            putString("id", id)
        }

        findNavController().navigate(R.id.addHabitFragment, bundle)
    }
}