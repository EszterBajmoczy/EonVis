package hu.bme.aut.eonvis.interfaces

import androidx.lifecycle.LiveData
import hu.bme.aut.eonvis.data.model.PowerConsume

interface IDetailsRepository {
    fun getAllByInterval(firstId: Long, lastId: Long) : LiveData<List<PowerConsume>>

    suspend fun addTagByInterval(
        newTag: String,
        data: List<PowerConsume>
    )

    suspend fun removeTagByInterval(
        tagToRemove: String,
        data: List<PowerConsume>)

    fun getTags(callback: (ArrayList<String>) -> Unit)
}