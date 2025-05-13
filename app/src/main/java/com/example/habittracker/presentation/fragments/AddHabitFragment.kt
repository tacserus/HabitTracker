package com.example.habittracker.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.habittracker.R
import com.example.habittracker.dagger.App
import com.example.habittracker.databinding.FragmentAddHabitBinding
import com.example.habittracker.domain.enums.HabitType
import com.example.habittracker.domain.enums.Priority
import com.example.habittracker.domain.models.AddHabitEvent
import com.example.habittracker.presentation.viewmodels.AddHabitViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddHabitFragment : Fragment(R.layout.fragment_add_habit) {
    private val TAG = "add_habit_fragment"
    private lateinit var binding: FragmentAddHabitBinding

    @Inject
    lateinit var addHabitViewModel: AddHabitViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (requireActivity().application as App).appComponent.inject(this)

        addHabitViewModel.initState(
            arguments?.getString("id") ?: "",
            requireContext().getString(Priority.Lite.id),
            requireContext().getString(HabitType.GoodHabit.id)
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
            addHabitViewModel.onCountChanged(it.toString())
        }

        binding.editTextFrequency.addTextChangedListener {
            addHabitViewModel.onFrequencyChanged(it.toString())
        }

        initSpinner()

        binding.buttonSave.setOnClickListener {
            if (checkFieldsForSave()) {

                val priority = Priority.entries.find {
                    requireContext().getString(it.id) == addHabitViewModel.stateLiveData.value?.priority
                } ?: Priority.Lite
                val type = HabitType.entries.find {
                    requireContext().getString(it.id) == addHabitViewModel.stateLiveData.value?.type
                } ?: HabitType.GoodHabit
                addHabitViewModel.onSaveClicked(priority, type)
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