package com.example.habittracker.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.habittracker.viewmodels.HabitListViewModel

import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.habittracker.R
import com.example.habittracker.enums.Priority
import com.example.habittracker.models.Item
import java.util.UUID

class AddHabitFragment : Fragment(R.layout.fragment_add_habit) {
    private var editTextTitle: EditText? = null
    private var editTextDescription: EditText? = null
    private var spinnerPriority: Spinner? = null
    private var radioGroupHabitType: RadioGroup? = null
    private var editTextQuality: EditText? = null
    private var editTextFrequency: EditText? = null
    private var buttonSave: Button? = null
    private var buttonCancel: Button? = null

    private var selectedPriority: String? = null
    private var selectedType: String? = null
    private var editedItem: Item? = null
    private var id: UUID? = null

    private var view: View? = null
    private var habitListViewModel: HabitListViewModel? = null

    private val TAG = "add_item_fragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                editedItem = it.getParcelable("item", Item::class.java)
            } else {
                @Suppress("DEPRECATION")
                editedItem = it.getParcelable("item")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.view = view
        habitListViewModel = ViewModelProvider(requireActivity())[HabitListViewModel::class.java]

        initGUI()
    }

    private fun initGUI() {
        editTextTitle = view?.findViewById(R.id.editTextTitle)
        editTextDescription = view?.findViewById(R.id.editTextDescription)

        initSpinner()
        initHabitTypeGroup()

        buttonSave = view?.findViewById(R.id.buttonSave)
        buttonSave?.setOnClickListener { onSaveClicked() }

        buttonCancel = view?.findViewById(R.id.buttonCancel)
        buttonCancel?.setOnClickListener { onCancelClicked() }

        editTextQuality = view?.findViewById(R.id.editTextQuantity)
        editTextFrequency = view?.findViewById(R.id.editTextFrequency)

        if (editedItem !== null) {
            fillDataForEditMode()
        }
    }

    private fun initSpinner() {
        spinnerPriority = view?.findViewById(R.id.spinnerPriority)

        val items = Priority.entries.map { priority -> priority.description }
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPriority?.adapter = adapter

        spinnerPriority?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedPriority = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerPriority?.setSelection(0)
    }

    private fun initHabitTypeGroup() {
        radioGroupHabitType = view?.findViewById(R.id.radioGroupHabitType)

        radioGroupHabitType?.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton = view?.findViewById<RadioButton>(checkedId)
            selectedType = selectedRadioButton?.text.toString()
        }

        radioGroupHabitType?.check(R.id.radioButtonGoodHabit)
        val radioButton = view?.findViewById<RadioButton>(R.id.radioButtonGoodHabit)
        selectedType = radioButton?.text.toString()
    }

    private fun fillDataForEditMode() {
        id = editedItem?.id
        editTextTitle?.setText(editedItem?.title)
        editTextDescription?.setText(editedItem?.description)

        spinnerPriority?.setSelection(
            Priority.entries.indexOfFirst { priority ->
                priority.description == editedItem?.priority
            }
        )

        for (i in 0 until radioGroupHabitType?.childCount!!) {
            val radioButton = radioGroupHabitType?.getChildAt(i) as RadioButton
            if (radioButton.text == editedItem?.type) {
                radioGroupHabitType?.check(radioButton.id)
                selectedType = editedItem?.type
                break
            }
        }

        val quantity = editedItem?.quantity.toString()
        val frequency = editedItem?.frequency.toString()

        editTextQuality?.setText(quantity)
        editTextFrequency?.setText(frequency)
    }

    private fun onSaveClicked() {
        if (!checkFieldsForSave()) {
            return
        }

        val quantity = editTextQuality?.text.toString().toIntOrNull() ?: 0
        val frequency = editTextFrequency?.text.toString().toIntOrNull() ?: 0

        findNavController().popBackStack()

        if (editedItem !== null) {
            editItem(quantity, frequency)
            return
        }

        addItem(quantity, frequency)
    }

    private fun addItem(quantity: Int, frequency: Int) {
        id = Item.generateId()

        val newItem = Item(
            id = id!!,
            title = editTextTitle?.text.toString(),
            description = editTextDescription?.text.toString(),
            priority = selectedPriority!!,
            type = selectedType!!,
            quantity = quantity,
            frequency = frequency
        )

        habitListViewModel?.addItem(newItem)
    }

    private fun editItem(quantity: Int, frequency: Int) {
        val newItem = Item(
            id = id!!,
            title = editTextTitle?.text.toString(),
            description = editTextDescription?.text.toString(),
            priority = selectedPriority!!,
            type = selectedType!!,
            quantity = quantity,
            frequency = frequency
        )

        habitListViewModel?.updateItem(newItem)
    }

    private fun onCancelClicked() {
        findNavController().popBackStack()
    }

    private fun checkFieldsForSave(): Boolean {
        return editTextTitle!!.text.isNotBlank() &&
                editTextDescription!!.text.isNotBlank() &&
                editTextQuality!!.text.isNotBlank() &&
                editTextFrequency!!.text.isNotBlank() &&
                selectedPriority != null &&
                selectedType != null
    }
}