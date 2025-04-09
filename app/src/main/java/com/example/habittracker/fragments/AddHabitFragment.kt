package com.example.habittracker.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.habittracker.R
import com.example.habittracker.database.App
import com.example.habittracker.database.HabitsRepository
import com.example.habittracker.databinding.FragmentAddHabitBinding
import com.example.habittracker.enums.HabitType
import com.example.habittracker.enums.Priority
import com.example.habittracker.models.AddHabitEvent
import com.example.habittracker.viewmodels.AddHabitViewModel
import com.example.habittracker.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch

class AddHabitFragment : Fragment(R.layout.fragment_add_habit) {
    private val TAG = "add_habit_fragment"
    private lateinit var binding: FragmentAddHabitBinding

    private val addHabitViewModel: AddHabitViewModel by viewModels {
        val id = arguments?.getString("id") ?: ""

        ViewModelFactory(
            habitsRepository = HabitsRepository(
                (requireActivity().application as App).database
            ),
            application = requireActivity().application,
            id = id
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddHabitBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = addHabitViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initGUI()

        lifecycleScope.launch {
            addHabitViewModel.events.collect { event ->
                Log.i(TAG, "clicked")
                when (event) {
                    is AddHabitEvent.NavigateBack -> {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    private fun initGUI() {
        binding.editTextTitle.addTextChangedListener {
            addHabitViewModel.onTitleChanged(it.toString())
        }

        binding.editTextDescription.addTextChangedListener {
            addHabitViewModel.onDescriptionChanged(it.toString())
        }

        binding.radioGroupHabitType.setOnCheckedChangeListener { _, checkedId ->
            val newType = when (checkedId) {
                R.id.radioButtonGoodHabit -> requireContext().getString(HabitType.GoodHabit.id)
                R.id.radioButtonBadHabit -> requireContext().getString(HabitType.BadHabit.id)
                else -> ""
            }

            addHabitViewModel.onTypeChanged(newType)
        }

        binding.editTextQuantity.addTextChangedListener {
            addHabitViewModel.onQuantityChanged(it.toString())
        }

        binding.editTextFrequency.addTextChangedListener {
            addHabitViewModel.onFrequencyChanged(it.toString())
        }

        initSpinner()

        binding.buttonSave.setOnClickListener {
            if (checkFieldsForSave()) {
                addHabitViewModel.onSaveClicked()
            }
        }
        binding.buttonCancel.setOnClickListener {
            addHabitViewModel.onNavigateButtonClicked()
        }
    }

    private fun initSpinner() {
        val items = Priority.entries.map { priority -> requireContext().getString(priority.id) }
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPriority.adapter = adapter
        binding.spinnerPriority.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val newPriority = parent.getItemAtPosition(position).toString()
                addHabitViewModel.onPriorityChanged(newPriority)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun checkFieldsForSave(): Boolean {
        return binding.editTextTitle.text.isNotBlank() &&
               binding.editTextDescription.text.isNotBlank() &&
               binding.editTextQuantity.text.isNotBlank() &&
               binding.editTextFrequency.text.isNotBlank()
    }
}