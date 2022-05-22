package hu.bme.aut.eonvis.interfaces

import androidx.lifecycle.LiveData
import hu.bme.aut.eonvis.data.database.PowerConsumeDAO
import hu.bme.aut.eonvis.data.model.PowerConsume
import kotlinx.coroutines.CoroutineScope

interface IMainRepository {
    suspend fun add(data: PowerConsumeDAO)

    fun getAll() : LiveData<List<PowerConsume>>

    fun syncData(scope: CoroutineScope, lastUpdate: Long, callback: (Long) -> Unit)
}