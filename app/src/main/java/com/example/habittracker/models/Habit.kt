package com.example.habittracker.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val priority: String,
    val type: String,
    val quantity: String,
    val frequency: String
) : Parcelable {
    companion object {
        fun getRandomId(): String {
            return UUID.randomUUID().toString()
        }

        @JvmField
        val CREATOR = object : Parcelable.Creator<Habit> {
            override fun createFromParcel(parcel: Parcel): Habit {
                return Habit(
                    parcel.readString() ?: "",
                    parcel.readString() ?: "",
                    parcel.readString() ?: "",
                    parcel.readString() ?: "",
                    parcel.readString() ?: "",
                    parcel.readString() ?: "",
                    parcel.readString() ?: "",
                )
            }

            override fun newArray(size: Int): Array<Habit?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(priority)
        parcel.writeString(type)
        parcel.writeString(quantity)
        parcel.writeString(frequency)
    }

    override fun describeContents(): Int {
        return 0
    }
}