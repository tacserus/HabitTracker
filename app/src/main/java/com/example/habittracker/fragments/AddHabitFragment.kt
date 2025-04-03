package com.example.habittracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.habittracker.R
import com.example.habittracker.databinding.FragmentAddHabitBinding
import com.example.habittracker.enums.HabitType
import com.example.habittracker.enums.Priority
import com.example.habittracker.viewmodels.AddHabitViewModel
import com.example.habittracker.viewmodels.ViewModelProvider

class AddHabitFragment : Fragment(R.layout.fragment_add_habit) {
    private lateinit var binding: FragmentAddHabitBinding

    private lateinit var addHabitViewModel: AddHabitViewModel

    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addHabitViewModel = ViewModelProvider.instance.getAddHabitViewModel(requireActivity())

        arguments?.let {
            id = it.getString("id") ?: ""
        }
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

        addHabitViewModel.fillStateForEditMode(id)

        initGUI()
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
                R.id.radioButtonGoodHabit -> HabitType.GOODHABIT.description
                R.id.radioButtonBadHabit -> HabitType.BADHABIT.description
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

        binding.buttonSave.setOnClickListener { onSaveClicked() }
        binding.buttonCancel.setOnClickListener { onCancelClicked() }
    }

    private fun initSpinner() {
        val items = Priority.entries.map { priority -> priority.description }
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

    private fun onSaveClicked() {
        if (!checkFieldsForSave()) {
            return
        }

        if (id != "null") {
            addHabitViewModel.updateHabit()
        } else {
            addHabitViewModel.addHabit()
        }

        addHabitViewModel.clearState()
        findNavController().popBackStack()
    }

    private fun onCancelClicked() {
        addHabitViewModel.clearState()
        findNavController().popBackStack()
    }

    private fun checkFieldsForSave(): Boolean {
        return binding.editTextTitle.text.isNotBlank() &&
               binding.editTextDescription.text.isNotBlank() &&
               binding.editTextQuantity.text.isNotBlank() &&
               binding.editTextFrequency.text.isNotBlank()
    }
}