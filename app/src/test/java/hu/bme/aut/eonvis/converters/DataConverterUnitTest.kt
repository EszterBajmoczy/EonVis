package hu.bme.aut.eonvis.converters

import hu.bme.aut.eonvis.converter.DataConverter
import hu.bme.aut.eonvis.data.database.PowerConsumeDAO
import hu.bme.aut.eonvis.data.model.PowerConsume
import org.junit.Assert
import org.junit.Test

class DataConverterUnitTest {
    private val _sut = DataConverter()

    @Test
    fun convertToDTO() {
        val tag1 = "Tag"
        val tag2 = "TagTag"
        val data = PowerConsume(1, 2.2, 3.3, arrayListOf(tag1, tag2))

        val result = _sut.convertToDTO(data)

        Assert.assertEquals(data.id, result.id)
        Assert.assertEquals(data.incoming, result.incoming, 0.0001)
        Assert.assertEquals(data.outgoing, result.outgoing, 0.0001)
        assert(result.tags.contains(tag1))
        assert(result.tags.contains(tag2))
    }

    @Test
    fun convertToModel() {
        val tag1 = "Tag"
        val tag2 = "TagTag"
        val data = PowerConsumeDAO(1, 2.2, 3.3, "$tag1,$tag2")

        val result = _sut.convertToModel(data)

        Assert.assertEquals(data.id, result.id)
        Assert.assertEquals(data.incoming, result.incoming, 0.0001)
        Assert.assertEquals(data.outgoing, result.outgoing, 0.0001)
        assert(result.tagList.contains(tag1))
        assert(result.tagList.contains(tag2))
    }

    @Test
    fun convertTagsToString() {
        val tag1 = "Tag"
        val tag2 = "TagTag"
        val list = listOf("Tag", "TagTag")

        val result = _sut.convertTagsToString(list)

        assert(result.contains(tag1))
        assert(result.contains(tag2))
    }

    @Test
    fun convertStringToTags() {
        val tag1 = "Tag"
        val tag2 = "TagTag"

        val result = _sut.convertStringToTags("$tag1,$tag2")

        assert(result.contains(tag1))
        assert(result.contains(tag2))
    }
}