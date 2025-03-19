package com.example.habittracker.models

import android.os.Parcel
import android.os.Parcelable
import java.util.LinkedList
import java.util.Queue

data class Item(
    val id: Int,
    val title: String,
    val description: String,
    val priority: String,
    val type: String,
    val quantity: Int,
    val frequency: Int
) : Parcelable {
    companion object {
        private var maxId: Int = 0
        private val freeIdQueue: LinkedList<Int> = LinkedList()

        fun getFreeId(): Int {
            return if (freeIdQueue.isNotEmpty()) {
                freeIdQueue.pop()
            } else {
                ++maxId
            }
        }

        fun releaseId(id: Int) {
            freeIdQueue.push(id)
        }

        @JvmField
        val CREATOR = object : Parcelable.Creator<Item> {
            override fun createFromParcel(parcel: Parcel): Item {
                return Item(
                    parcel.readInt(),
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
        parcel.writeInt(id)
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

    override fun toString(): String {
        return "$id $title $description $priority $type $quantity $frequency"
    }
}