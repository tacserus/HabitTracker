package com.example.habittracker.models

import android.os.Parcel
import android.os.Parcelable
import java.util.UUID

data class Item(
    val id: UUID,
    val title: String,
    val description: String,
    val priority: String,
    val type: String,
    val quantity: Int,
    val frequency: Int
) : Parcelable {
    companion object {
        fun generateId(): UUID {
            return UUID.randomUUID()
        }

        @JvmField
        val CREATOR = object : Parcelable.Creator<Item> {
            override fun createFromParcel(parcel: Parcel): Item {
                return Item(
                    UUID.fromString(parcel.readString()),
                    parcel.readString() ?: "",
                    parcel.readString() ?: "",
                    parcel.readString() ?: "",
                    parcel.readString() ?: "",
                    parcel.readInt(),
                    parcel.readInt(),
                )
            }

            override fun newArray(size: Int): Array<Item?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id.toString())
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(priority)
        parcel.writeString(type)
        parcel.writeInt(quantity)
        parcel.writeInt(frequency)
    }

    override fun describeContents(): Int {
        return 0
    }
}