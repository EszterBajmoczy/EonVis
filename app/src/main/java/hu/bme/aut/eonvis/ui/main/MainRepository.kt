package hu.bme.aut.eonvis.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import hu.bme.aut.eonvis.converter.DataConverter
import hu.bme.aut.eonvis.data.database.PowerConsumeDAO
import hu.bme.aut.eonvis.data.model.PowerConsume
import hu.bme.aut.eonvis.interfaces.IEonVisService
import hu.bme.aut.eonvis.interfaces.IMainRepository
import hu.bme.aut.eonvis.persistence.EonVisDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class MainRepository @Inject constructor(
    private val eonVisDao: EonVisDao,
    private val networkService: IEonVisService,
    private val dataConverter: DataConverter) : IMainRepository {

    override suspend fun add(data: PowerConsumeDAO) = withContext(Dispatchers.IO) {
        eonVisDao.insertData(data)
    }

    override fun getAll() : LiveData<List<PowerConsume>> {
        return eonVisDao.getAllData().map {
                item -> item.map {
                    data -> dataConverter.convertToModel(data)
                }
        }
    }

    override fun syncData(scope: CoroutineScope, lastUpdate: Long, callback: (Long) -> Unit) {
        networkService.getPowerConsumes { result ->
            var lastDataId = lastUpdate
            if(result.id > lastUpdate){
                val data = dataConverter.convertToDTO(result)
                scope.launch {
                    try{
                        add(data)
                    } catch (e: Exception){
                        Log.d("SyncError", "Unique key error")
                    }
                }
                if(lastDataId < result.id){
                    lastDataId = result.id
                }
            }

            callback(lastDataId)
        }
    }
}