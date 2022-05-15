package hu.bme.aut.eonvis.ui.main

import androidx.lifecycle.LiveData
import hu.bme.aut.eonvis.data.database.PowerConsumeDAO
import hu.bme.aut.eonvis.network.EonVisService
import hu.bme.aut.eonvis.persistence.EonVisDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainRepository @Inject constructor(private val eonVisDao: EonVisDao, private val networkService: EonVisService) {

    suspend fun add(data: PowerConsumeDAO) = withContext(Dispatchers.IO) {
        eonVisDao.insertData(data)
    }

    suspend fun updateTags(id: Long, tags: String) = withContext(Dispatchers.IO) {
        eonVisDao.updateData(id, tags)
    }

    fun getAll() : LiveData<List<PowerConsumeDAO>> {
        return eonVisDao.getAllData()
    }
}