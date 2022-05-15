package hu.bme.aut.eonvis.data.database

import androidx.room.TypeConverter

class ArrayTypeConverter {

    @TypeConverter
    fun fromArray(strings: List<String>?): String {
        var string = ""
        for (s in strings!!) {
            string += "$s,"
        }
        return string.substring(0, string.length - 1)
    }

    @TypeConverter
    fun toArray(concatenatedStrings: String): ArrayList<String> {
        val myStrings = ArrayList<String>()
        for (s in concatenatedStrings.split(",").toTypedArray()) {
            myStrings.add(s)
        }
        return myStrings
    }
}