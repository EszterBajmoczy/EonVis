package hu.bme.aut.eonvis.viewmodels

import hu.bme.aut.eonvis.data.DataType
import hu.bme.aut.eonvis.interfaces.IDetailsRepository
import hu.bme.aut.eonvis.interfaces.IMainRepository
import hu.bme.aut.eonvis.ui.details.DetailsViewModel
import hu.bme.aut.eonvis.ui.main.MainViewModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class DetailsViewModelUnitTest {
    private val _sut: DetailsViewModel

    private val _repository: IDetailsRepository = mockk<IDetailsRepository>(relaxed = true)

    init {
        _sut = DetailsViewModel(_repository)
    }

    @Test
    fun loadData_daily(){
        val firstId: Long = 1
        val type = DataType.Daily

        _sut.loadData(firstId = firstId, type = type)

        verify { _repository.getAllByInterval(firstId, firstId) }
    }

    @Test
    fun loadData_monthly(){
        val firstId: Long = 1
        val type = DataType.Monthly

        _sut.loadData(firstId = firstId, type = type)

        verify { _repository.getAllByInterval(firstId*100, firstId*100+31) }
    }

    @Test
    fun loadData_yearly(){
        val firstId: Long = 1
        val type = DataType.Yearly

        _sut.loadData(firstId = firstId, type = type)

        verify { _repository.getAllByInterval(firstId*10000, firstId*10000+10000) }
    }
}