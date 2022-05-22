package hu.bme.aut.eonvis.interfaces

interface ILoginRepository {
    fun login(username: String, password: String, loginCallback: (Boolean) -> Unit)

    fun isLoggedIn(callback: (Boolean) -> Unit)
}