package com.example.habittracker.enums

import android.os.Parcel
import android.os.Parcelable
import com.example.habittracker.R

enum class HabitType(val description: String) : Parcelable {
    GOODHABIT("Хорошая привычка"),
    BADHABIT("Плохая привычка");

    constructor(parcel: Parcel) : this(parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HabitType> {
        override fun createFromParcel(parcel: Parcel): HabitType {
            val description = parcel.readString()
            return entries.first { it.description == description }
        }

        override fun newArray(size: Int): Array<HabitType?> {
            return arrayOfNulls(size)
        }
    }
}