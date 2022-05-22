package hu.bme.aut.eonvis.interfaces

import hu.bme.aut.eonvis.data.model.PowerConsume

interface IEonVisService {
    fun login(username: String, password: String, loginCallback: (Boolean) -> Unit)

    fun isLoggedIn(callback: (Boolean) -> Unit)

    fun getPowerConsumes(callback: (PowerConsume) -> Unit)

    fun addTag(id: Long, tag: String)

    fun removeTag(id: Long, tag: String)

    fun getTags(callback: (ArrayList<String>) -> Unit)
}