package hu.bme.aut.eonvis.ui.login

import hu.bme.aut.eonvis.network.EonVisService
import javax.inject.Inject

class LoginRepository @Inject constructor(private val networkService: EonVisService) {
    fun login(username: String, password: String, loginCallback: (Boolean) -> Unit) {
        networkService.login(username, password, loginCallback);
    }

    fun isLoggedIn(callback: (Boolean) -> Unit) {
        networkService.isLoggedIn(callback)
    }
}