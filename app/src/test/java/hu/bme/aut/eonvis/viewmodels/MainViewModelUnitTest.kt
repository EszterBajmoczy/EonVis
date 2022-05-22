package hu.bme.aut.eonvis.viewmodels

import hu.bme.aut.eonvis.interfaces.IMainRepository
import hu.bme.aut.eonvis.ui.main.MainViewModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class MainViewModelUnitTest {
    private val _sut: MainViewModel

    private val _mainRepository: IMainRepository = mockk<IMainRepository>(relaxed = true)

    init {
        _sut = MainViewModel(_mainRepository)
    }

    @Test
    fun sync(){
        val lastUpdate: Long = 0

        _sut.sync(lastUpdate = lastUpdate)

        verify { _mainRepository.syncData(scope = any(), lastUpdate = lastUpdate, callback = any()) }
    }
}