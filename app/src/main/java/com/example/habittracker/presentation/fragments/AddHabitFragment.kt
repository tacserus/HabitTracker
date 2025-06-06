package com.example.habittracker.presentation.fragments

import android.os.Bundle
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
import com.example.domain.models.AddHabitEvent
import com.example.domain.models.Priority
import com.example.domain.models.Type
import com.example.habittracker.R
import com.example.habittracker.dagger.App
import com.example.habittracker.databinding.FragmentAddHabitBinding
import com.example.habittracker.presentation.viewmodels.AddHabitViewModel
import com.example.habittracker.presentation.viewmodels.AddHabitViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddHabitFragment : Fragment(R.layout.fragment_add_habit) {
    private val TAG = "add_habit_fragment"
    private lateinit var binding: FragmentAddHabitBinding

    @Inject
    lateinit var addHabitViewModelFactory: AddHabitViewModelFactory
    private val addHabitViewModel: AddHabitViewModel by viewModels { addHabitViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (requireActivity().application as App).presentationSubcomponent.inject(this)

        addHabitViewModel.initState(
            arguments?.getString("id") ?: "",
            Priority.Lite.description,
            Type.GoodHabit.description
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
                R.id.radioButtonGoodHabit -> Type.GoodHabit.description
                R.id.radioButtonBadHabit -> Type.BadHabit.description
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
                    it.description == addHabitViewModel.stateFlow.value?.priority
                } ?: Priority.Lite
                val type = Type.entries.find {
                    it.description == addHabitViewModel.stateFlow.value?.type
                } ?: Type.GoodHabit
                addHabitViewModel.onSaveClicked(priority, type)
            }
        }
        binding.buttonCancel.setOnClickListener {
            addHabitViewModel.onNavigateButtonClicked()
        }
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

    private fun checkFieldsForSave(): Boolean {
        return binding.editTextTitle.text.isNotBlank() &&
               binding.editTextDescription.text.isNotBlank() &&
               binding.editTextQuantity.text.isNotBlank() &&
               binding.editTextFrequency.text.isNotBlank()
    }
}