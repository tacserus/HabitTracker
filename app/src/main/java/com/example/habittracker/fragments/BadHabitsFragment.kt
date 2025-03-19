package com.example.habittracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.R
import com.example.habittracker.adapters.RecyclerViewAdapter
import com.example.habittracker.models.Item
import com.example.habittracker.viewmodels.HabitListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BadHabitsFragment : Fragment() {

    private var habitListViewModel: HabitListViewModel? = null
    private var recyclerView: RecyclerView? = null

    private val TAG = "bad_habits_fragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        habitListViewModel = ViewModelProvider(requireActivity())[HabitListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bad_habits, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        val adapter = RecyclerViewAdapter(mutableListOf()) { item -> onItemClick(item) }
        recyclerView?.adapter = adapter

        habitListViewModel?.items?.observe(viewLifecycleOwner) { items ->
            adapter.submit(items.filter { item -> item.type == "Плохая привычка" })
        }

        val fab: FloatingActionButton = view.findViewById(R.id.fab)
        fab.setOnClickListener {
            openAddItemFragment()
        }
    }

    private fun openAddItemFragment(item: Item? = null) {
        val bundle = Bundle().apply {
            putParcelable("item", item)
        }

        findNavController().navigate(R.id.addHabitFragment, bundle)
    }

    private fun onItemClick(item: Item) {
        openAddItemFragment(item)
    }
}