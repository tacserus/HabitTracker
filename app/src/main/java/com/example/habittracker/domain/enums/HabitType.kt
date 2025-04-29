package com.example.habittracker.domain.enums

import android.os.Parcel
import android.os.Parcelable
import com.example.habittracker.R

enum class HabitType(val id: Int) : Parcelable {
    GoodHabit(R.string.habit_good_type),
    BadHabit(R.string.habit_bad_type);

    constructor(parcel: Parcel) : this(parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HabitType> {
        override fun createFromParcel(parcel: Parcel): HabitType {
            val description = parcel.readInt()
            return entries.first { it.id == description }
        }

        override fun newArray(size: Int): Array<HabitType?> {
            return arrayOfNulls(size)
        }
    }
}