package hu.bme.aut.eonvis.viewmodels

import hu.bme.aut.eonvis.interfaces.ILoginRepository
import hu.bme.aut.eonvis.ui.login.LoginViewModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class LoginViewModelUnitTest {
    private val _sut: LoginViewModel

    private val _loginRepository: ILoginRepository = mockk<ILoginRepository>(relaxed = true)

    init {
        _sut = LoginViewModel(_loginRepository)
    }

    @Test
    fun isLoggedIn(){
        verify { _loginRepository.isLoggedIn(any()) }
    }

    @Test
    fun login(){
        val username = "username"
        val password = "password"

        _sut.login(username = username, password = password)

        verify { _loginRepository.login(username = username, password = password, loginCallback = any()) }
    }
}