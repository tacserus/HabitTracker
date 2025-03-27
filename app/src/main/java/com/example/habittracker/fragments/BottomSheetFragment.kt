package com.example.habittracker.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.fragment.app.activityViewModels
import com.example.habittracker.R
import com.example.habittracker.databinding.FragmentBottomSheetBinding
import com.example.habittracker.enums.FilterType
import com.example.habittracker.enums.SortingType
import com.example.habittracker.viewmodels.HabitListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment : BottomSheetDialogFragment(R.layout.fragment_bottom_sheet) {
    private lateinit var binding: FragmentBottomSheetBinding
    private val habitListViewModel: HabitListViewModel by activityViewModels()

    private val TAG: String = "bsFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false)

        binding.applyButton.setOnClickListener {
            apply()
            dismiss()
        }

        binding.resetButton.setOnClickListener {
            habitListViewModel.reset()
            dismiss()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sortRadioGroup.check(binding.sortTitle.id)

        fillUI()
    }

    private fun fillUI() {
        val filters = habitListViewModel.getCurrentFilters()

        binding.frequencyEditText.setText(filters[FilterType.FREQUENCY] ?: "")
        binding.quantityEditText.setText(filters[FilterType.QUANTITY] ?: "")

        val sortingType = habitListViewModel.getCurrentSortingType()

        for (i in 0 until binding.sortRadioGroup.childCount) {
            val radioButton = binding.sortRadioGroup.getChildAt(i) as RadioButton
            if (radioButton.text == sortingType.description) {
                binding.sortRadioGroup.check(radioButton.id)
                break
            }
        }

        binding.searchEditText.setText(habitListViewModel.getCurrentSearchingWord())
    }

    private fun apply() {
        val filters: MutableMap<FilterType, String> = mutableMapOf()

        val frequency = binding.frequencyEditText.text.toString()
        val quantity = binding.quantityEditText.text.toString()

        val searchingWord = binding.searchEditText.text.toString()

        Log.i(TAG, "data: $frequency $quantity")
        if (frequency.isNotBlank()) {
            filters[FilterType.FREQUENCY] = frequency
        }

        if (quantity.isNotBlank()) {
            filters[FilterType.QUANTITY] = quantity
        }

        val selectedSortingType = getSelectedSortingType()

        habitListViewModel.sortByField(selectedSortingType)
        habitListViewModel.applyFilters(filters)
        if (searchingWord.isNotBlank()) {
            habitListViewModel.findByWord(searchingWord)
        }
    }

    private fun getSelectedSortingType(): SortingType {
        return when (binding.sortRadioGroup.checkedRadioButtonId) {
            binding.sortQuantity.id -> SortingType.QUANTITY
            binding.sortFrequency.id -> SortingType.FREQUENCY
            else -> SortingType.DEFAULT
        }
    }
}