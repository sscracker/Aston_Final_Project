package org.grigiorev.rickandmortyproject.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    internal fun fromStringList(list: List<String>): String {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().toJson(list, listType)
    }

    @TypeConverter
    internal fun toStringList(json: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(json, listType)
    }
}