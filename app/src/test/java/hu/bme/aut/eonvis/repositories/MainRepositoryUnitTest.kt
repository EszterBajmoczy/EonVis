package hu.bme.aut.eonvis.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hu.bme.aut.eonvis.converter.DataConverter
import hu.bme.aut.eonvis.data.database.PowerConsumeDAO
import hu.bme.aut.eonvis.interfaces.IEonVisService
import hu.bme.aut.eonvis.persistence.EonVisDao
import hu.bme.aut.eonvis.ui.main.MainRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test

class MainRepositoryUnitTest {
    private val _sut: MainRepository

    private val _eonVisDao: EonVisDao = mockk<EonVisDao>(relaxed = true)
    private val _networkService: IEonVisService = mockk<IEonVisService>(relaxed = true)
    private val _dataConverter: DataConverter = mockk<DataConverter>()

    init {
        _sut = MainRepository(_eonVisDao, _networkService, _dataConverter)
    }

    @Test
    fun getAll() {
        val list = listOf(PowerConsumeDAO(1, 2.2, 3.3, "Tag"))
        val data: LiveData<List<PowerConsumeDAO>> = MutableLiveData(list)

        every { _eonVisDao.getAllData() } returns data

        _sut.getAll()

        verify {
            _eonVisDao.getAllData()
        }
    }

    @Test
    fun add() {
        val data = PowerConsumeDAO(1, 2.2, 3.3, "Tag")

        runBlocking {
            _sut.add(data)
        }

        verify {
            _eonVisDao.insertData(any())
        }
    }

    @Test
    fun syncData() {
        val scope = mockk<CoroutineScope>()
        val longMock = mockk<(Long) -> Unit>()

        _sut.syncData(scope, 0, longMock)

        verify {
            _networkService.getPowerConsumes(any())
        }
    }
}