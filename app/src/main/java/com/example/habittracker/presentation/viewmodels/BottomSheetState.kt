package com.example.habittracker.presentation.viewmodels

import com.example.habittracker.presentation.enums.SortingType


data class BottomSheetState(
    val searchText: String = "",
    val countFilter: String = "",
    val frequencyFilter: String = "",
    val selectedSortOption: SortingType = SortingType.DEFAULT
)