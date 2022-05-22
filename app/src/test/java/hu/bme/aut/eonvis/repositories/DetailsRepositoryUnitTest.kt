package hu.bme.aut.eonvis.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hu.bme.aut.eonvis.converter.DataConverter
import hu.bme.aut.eonvis.data.database.PowerConsumeDAO
import hu.bme.aut.eonvis.data.model.PowerConsume
import hu.bme.aut.eonvis.interfaces.IEonVisService
import hu.bme.aut.eonvis.persistence.EonVisDao
import hu.bme.aut.eonvis.ui.details.DetailsRepository
import hu.bme.aut.eonvis.ui.main.MainRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test

class DetailsRepositoryUnitTest {
    private val _sut: DetailsRepository

    private val _eonVisDao: EonVisDao = mockk<EonVisDao>(relaxed = true)
    private val _networkService: IEonVisService = mockk<IEonVisService>(relaxed = true)
    private val _dataConverter: DataConverter = mockk<DataConverter>(relaxed = true)

    init {
        _sut = DetailsRepository(_eonVisDao, _networkService, _dataConverter)
    }

    @Test
    fun getAll() {
        val listCallback = mockk<(ArrayList<String>) -> Unit>()

        _sut.getTags(listCallback)

        verify {
            _networkService.getTags(listCallback)
        }
    }


    @Test
    fun removeTagByInterval() {
        val list = listOf(PowerConsume(0, 2.2, 3.3, arrayListOf("Tag")), PowerConsume(1, 2.2, 3.3, arrayListOf("Tag")))

        every { _dataConverter.convertTagsToString(any()) } returns "Tag"

        runBlocking {
            _sut.removeTagByInterval("Tag", list)
        }

        verify {
            _eonVisDao.updateData(0, "Tag")
            _eonVisDao.updateData(1, "Tag")
            _networkService.removeTag(0, "Tag")
            _networkService.removeTag(1, "Tag")
        }
    }

    @Test
    fun addTagByInterval() {
        val list = listOf(PowerConsume(0, 2.2, 3.3, arrayListOf("Tag")), PowerConsume(1, 2.2, 3.3, arrayListOf("Tag")))

        every { _dataConverter.convertTagsToString(any()) } returns "Tag"

        runBlocking {
            _sut.addTagByInterval("Tag", list)
        }

        verify {
            _eonVisDao.updateData(0, "Tag")
            _eonVisDao.updateData(1, "Tag")
            _networkService.addTag(0, "Tag")
            _networkService.addTag(1, "Tag")
        }
    }
}