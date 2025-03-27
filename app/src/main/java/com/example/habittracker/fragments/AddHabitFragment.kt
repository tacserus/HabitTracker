package com.example.habittracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.habittracker.R
import com.example.habittracker.databinding.FragmentAddHabitBinding
import com.example.habittracker.enums.Priority
import com.example.habittracker.viewmodels.AddHabitViewModel
import com.example.habittracker.viewmodels.HabitListViewModel

class AddHabitFragment : Fragment(R.layout.fragment_add_habit) {
    private lateinit var binding: FragmentAddHabitBinding

    private lateinit var habitListViewModel: HabitListViewModel
    private lateinit var addHabitViewModel: AddHabitViewModel

    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        habitListViewModel = ViewModelProvider(requireActivity())[HabitListViewModel::class.java]
        addHabitViewModel = ViewModelProvider(requireActivity())[AddHabitViewModel::class.java]

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

        addHabitViewModel.item.observe(viewLifecycleOwner) { item ->
            item?.let { safeItem ->
                binding.editTextTitle.setText(safeItem.title)
                binding.editTextDescription.setText(safeItem.description)

                binding.spinnerPriority.setSelection(
                    Priority.entries.indexOfFirst { priority -> priority.description == safeItem.priority }
                )

                for (i in 0 until binding.radioGroupHabitType.childCount) {
                    val radioButton = binding.radioGroupHabitType.getChildAt(i) as RadioButton
                    if (radioButton.text == safeItem.type) {
                        binding.radioGroupHabitType.check(radioButton.id)
                        break
                    }
                }
                binding.editTextQuantity.setText(safeItem.quantity)
                binding.editTextFrequency.setText(safeItem.frequency)
            } ?: run {
                binding.editTextTitle.setText("")
                binding.editTextDescription.setText("")

                binding.spinnerPriority.setSelection(
                    Priority.entries.indexOfFirst { priority -> priority == Priority.Lite }
                )

                val radioButton = binding.radioGroupHabitType.getChildAt(0) as RadioButton
                binding.radioGroupHabitType.check(radioButton.id)

                binding.editTextQuantity.setText("")
                binding.editTextFrequency.setText("")
            }
        }

        initGUI()

        fillDataForEditMode()
    }

    private fun initGUI() {
        initSpinner()

        binding.buttonSave.setOnClickListener { onSaveClicked() }
        binding.buttonCancel.setOnClickListener { onCancelClicked() }
    }

    private fun initSpinner() {
        val items = Priority.entries.map { priority -> priority.description }
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPriority.adapter = adapter
    }

    private fun fillDataForEditMode() {
        val editedItem = habitListViewModel.getItemById(id)
        if (editedItem != null) {
            addHabitViewModel.fillItem(editedItem)
        }
    }

    private fun onSaveClicked() {
        if (!checkFieldsForSave()) {
            return
        }

        addHabitViewModel.createItem(
            id.ifBlank { null },
            binding.editTextTitle.text.toString(),
            binding.editTextDescription.text.toString(),
            binding.spinnerPriority.selectedItem.toString(),
            when (binding.radioGroupHabitType.checkedRadioButtonId) {
                R.id.radioButtonGoodHabit -> binding.radioButtonGoodHabit.text.toString()
                R.id.radioButtonBadHabit -> binding.radioButtonBadHabit.text.toString()
                else -> ""
            },
            binding.editTextQuantity.text.toString(),
            binding.editTextFrequency.text.toString()
        )

        if (id.isNotBlank()) {
            addHabitViewModel.item.value?.let { habitListViewModel.updateItem(it) }
        } else {
            addHabitViewModel.item.value?.let { habitListViewModel.addItem(it) }
        }

        findNavController().popBackStack()
    }

    private fun onCancelClicked() {
        findNavController().popBackStack()
    }

    private fun checkFieldsForSave(): Boolean {
        return binding.editTextTitle.text.isNotBlank() &&
               binding.editTextDescription.text.isNotBlank() &&
               binding.editTextQuantity.text.isNotBlank() &&
               binding.editTextFrequency.text.isNotBlank()
    }
}