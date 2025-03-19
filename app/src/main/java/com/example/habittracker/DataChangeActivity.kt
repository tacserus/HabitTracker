package com.example.habittracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.habittracker.enums.Priority
import com.example.habittracker.models.Item


class DataChangeActivity : AppCompatActivity() {
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
    private var id: Int? = null

    private val TAG = "data_change_activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_change)

        editedItem = intent.getParcelableExtra("item")

        initGUI()
    }

    private fun initGUI() {
        Log.i(TAG, "gui")

        editTextTitle = findViewById(R.id.editTextTitle)
        editTextDescription = findViewById(R.id.editTextDescription)

        initSpinner()
        initHabitTypeGroup()

        buttonSave = findViewById(R.id.buttonSave)
        buttonSave?.setOnClickListener { onSaveClicked() }

        buttonCancel = findViewById(R.id.buttonCancel)
        buttonCancel?.setOnClickListener { onCancelClicked() }

        editTextQuality = findViewById(R.id.editTextQuantity)
        editTextFrequency = findViewById(R.id.editTextFrequency)

        if (editedItem !== null) {
            Log.i(TAG, "fillDataForEditMode")
            fillDataForEditMode()
        }
    }

    private fun initSpinner() {
        spinnerPriority = findViewById(R.id.spinnerPriority)

        val items = Priority.entries.map { priority -> priority.description }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPriority?.adapter = adapter

        spinnerPriority?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedPriority = parent.getItemAtPosition(position).toString()
                Log.d("Spinner Selection", "Выбрано: $selectedPriority")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerPriority?.setSelection(0)
        Log.i(TAG, "fillDataForEditMode")
    }

    private fun initHabitTypeGroup() {
        radioGroupHabitType = findViewById(R.id.radioGroupHabitType)

        radioGroupHabitType?.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton = findViewById<RadioButton>(checkedId)
            selectedType = selectedRadioButton.text.toString()
            Log.i(TAG, "selectedType: $selectedType")
        }

        radioGroupHabitType?.check(R.id.radioButtonGoodHabit)
        val radioButton = findViewById<RadioButton>(R.id.radioButtonGoodHabit)
        selectedType = radioButton.text.toString()
        Log.i(TAG, "selectedType: $selectedType")
    }

    private fun fillDataForEditMode() {
        id = editedItem?.id
        editTextTitle?.setText(editedItem?.title)
        editTextDescription?.setText(editedItem?.description)

        Log.i(TAG, editedItem?.priority!!)

        spinnerPriority?.setSelection(
            Priority.entries.indexOfFirst { priority ->
                Log.i(TAG, priority.description)
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

        Log.i(TAG, "state ok")
    }

    private fun onSaveClicked() {
        if (!checkFieldsForSave()) {
            return
        }

        if (editedItem === null) {
            id = Item.getFreeId()
        }

        Log.i(TAG, "id: $id")
        Log.i(TAG, "title: ${editTextTitle?.text.toString()}")
        Log.i(TAG, "description: ${editTextDescription?.text.toString()}")
        Log.i(TAG, "priority: ${selectedPriority}")
        Log.i(TAG, "type: ${editTextTitle?.text.toString()}")

        val quantity = editTextQuality?.text.toString().toIntOrNull() ?: 0
        val frequency = editTextFrequency?.text.toString().toIntOrNull() ?: 0

        Log.i(TAG, "quantity: $quantity")
        Log.i(TAG, "frequency: $frequency")

        val newItem = Item(
            id = id!!,
            title = editTextTitle?.text.toString(),
            description = editTextDescription?.text.toString(),
            priority = selectedPriority!!,
            type = selectedType!!,
            quantity = quantity,
            frequency = frequency
        )

        Log.i(TAG, "$newItem")
        val resultIntent = Intent()
        resultIntent.putExtra("item", newItem)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun onCancelClicked() {
        finish()
    }

    private fun checkFieldsForSave(): Boolean {
        if (editTextTitle?.text.toString() == "") {
            return false
        }

        if (editTextTitle?.text.toString() == "") {
            return false
        }

        if (editTextTitle?.text.toString() == "") {
            return false
        }

        if (editTextTitle?.text.toString() == "") {
            return false
        }

        return true
    }
}