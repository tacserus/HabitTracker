package com.example.data.database

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromList(value: List<Long>?): String? {
        return value?.joinToString(",")
    }

    @TypeConverter
    fun toList(value: String?): List<Long>? {
        if (value == "") {
            return listOf()
        }

        return value?.split(",")?.map { it.toLong() }
    }
}