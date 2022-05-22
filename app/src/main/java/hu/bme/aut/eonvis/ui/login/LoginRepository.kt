package hu.bme.aut.eonvis.ui.login

import hu.bme.aut.eonvis.interfaces.IEonVisService
import hu.bme.aut.eonvis.interfaces.ILoginRepository
import hu.bme.aut.eonvis.network.EonVisService
import javax.inject.Inject

class LoginRepository @Inject constructor(private val networkService: IEonVisService) :
    ILoginRepository {
    override fun login(username: String, password: String, loginCallback: (Boolean) -> Unit) {
        networkService.login(username, password, loginCallback);
    }

    override fun isLoggedIn(callback: (Boolean) -> Unit) {
        networkService.isLoggedIn(callback)
    }
}