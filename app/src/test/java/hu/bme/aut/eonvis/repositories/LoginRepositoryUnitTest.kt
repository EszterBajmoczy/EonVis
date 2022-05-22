package hu.bme.aut.eonvis.repositories

import hu.bme.aut.eonvis.interfaces.IEonVisService
import hu.bme.aut.eonvis.ui.login.LoginRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class LoginRepositoryUnitTest {
    private val _sut: LoginRepository

    private val _networkService: IEonVisService = mockk<IEonVisService>(relaxed = true)

    init {
        _sut = LoginRepository(_networkService)
    }

    @Test
    fun login() {
        val username = "username"
        val password = "password"
        val booleanMock = mockk<(Boolean) -> Unit>()

        _sut.login(username = username, password = password, loginCallback = booleanMock)

        verify {
            _networkService.login(username, password, booleanMock)
        }
    }

    @Test
    fun isLoggedIn() {
        val booleanMock = mockk<(Boolean) -> Unit>()

        _sut.isLoggedIn(callback = booleanMock)

        verify {
            _networkService.isLoggedIn(callback = booleanMock)
        }
    }
}