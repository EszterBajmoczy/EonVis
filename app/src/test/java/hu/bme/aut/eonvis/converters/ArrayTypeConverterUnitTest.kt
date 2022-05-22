package hu.bme.aut.eonvis.converters

import androidx.room.TypeConverter
import hu.bme.aut.eonvis.data.database.ArrayTypeConverter
import org.junit.Test

class ArrayTypeConverterUnitTest {
    private val _sut = ArrayTypeConverter()

    @Test
    fun fromArray(){
        val tag1 = "Tag"
        val tag2 = "TagTag"
        val list = listOf(tag1, tag2)

        val result = _sut.fromArray(list)

        assert(result =="$tag1,$tag2")
    }

    @Test
    fun fromEmptyArray(){
        val result = _sut.fromArray(ArrayList())

        assert(result == "")
    }

    @TypeConverter
    fun toArray(concatenatedStrings: String): ArrayList<String> {
        val myStrings = ArrayList<String>()
        for (s in concatenatedStrings.split(",").toTypedArray()) {
            myStrings.add(s)
        }
        return myStrings
    }

    @Test
    fun fromEmptyString(){
        val result = _sut.toArray("")

        assert(result.size == 1)
        assert(result.contains(""))
    }

    @Test
    fun toArray(){
        val tag1 = "Tag"
        val tag2 = "TagTag"

        val result = _sut.toArray("$tag1,$tag2")

        assert(result.contains(tag1))
        assert(result.contains(tag2))
    }
}