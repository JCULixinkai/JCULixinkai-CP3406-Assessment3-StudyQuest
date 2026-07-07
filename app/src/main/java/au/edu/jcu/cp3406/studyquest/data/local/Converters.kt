package au.edu.jcu.cp3406.studyquest.data.local

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun listToString(values: List<String>): String = values.joinToString(separator = SEPARATOR) {
        it.replace(SEPARATOR, " ")
    }

    @TypeConverter
    fun stringToList(value: String): List<String> = if (value.isBlank()) {
        emptyList()
    } else {
        value.split(SEPARATOR)
    }

    private companion object {
        const val SEPARATOR = "\u001F"
    }
}

