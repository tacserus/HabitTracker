package com.example.habittracker.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habittracker.R
import com.example.habittracker.adapters.RecyclerViewAdapter
import com.example.habittracker.databinding.FragmentHabitsBinding
import com.example.habittracker.enums.HabitType
import com.example.habittracker.viewmodels.HabitListViewModel
import com.example.habittracker.viewmodels.ViewModelProvider

class HabitsFragment : Fragment(R.layout.fragment_habits) {

    private lateinit var habitListViewModel: HabitListViewModel
    private lateinit var binding: FragmentHabitsBinding

    private lateinit var type: HabitType

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

        arguments?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                type = it.getParcelable(HABITS_TYPE, HabitType::class.java)!!
            } else {
                @Suppress("DEPRECATION")
                type = it.getParcelable(HABITS_TYPE)!!
            }
        }

        habitListViewModel = ViewModelProvider.instance.getHabitListViewModel(requireActivity())
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
            HabitType.GOODHABIT-> {
                binding.listTitle.text = resources.getString(R.string.good_habits)
            }
            HabitType.BADHABIT -> {
                binding.listTitle.text = resources.getString(R.string.bad_habits)
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = RecyclerViewAdapter { id -> openAddItemFragment(id) }
        binding.recyclerView.adapter = adapter

        habitListViewModel.items.observe(viewLifecycleOwner) { items ->
            adapter.submit(items.filter { it.type == type.description })
        }

        binding.searchFab.setOnClickListener {
            val filterSortBottomSheet = BottomSheetFragment()
            filterSortBottomSheet.show(parentFragmentManager, BottomSheetFragment::class.java.simpleName)
        }

        binding.addingFab.setOnClickListener {
            openAddItemFragment()
        }
    }

    private fun openAddItemFragment(id: Long? = null) {
        val bundle = Bundle().apply {
            putString("id", id.toString())
        }

        findNavController().navigate(R.id.addHabitFragment, bundle)
    }
}