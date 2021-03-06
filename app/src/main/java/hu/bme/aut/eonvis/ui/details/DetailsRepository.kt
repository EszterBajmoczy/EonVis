package hu.bme.aut.eonvis.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import hu.bme.aut.eonvis.converter.DataConverter
import hu.bme.aut.eonvis.data.model.PowerConsume
import hu.bme.aut.eonvis.interfaces.IDetailsRepository
import hu.bme.aut.eonvis.interfaces.IEonVisService
import hu.bme.aut.eonvis.persistence.EonVisDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailsRepository @Inject constructor(
    private val eonVisDao: EonVisDao,
    private val networkService: IEonVisService,
    private val dataConverter: DataConverter) : IDetailsRepository {

    override fun getAllByInterval(firstId: Long, lastId: Long) : LiveData<List<PowerConsume>> {
        return eonVisDao.getDataByInterval(firstId = firstId, lastId = lastId).map {
                item -> item.map { data -> dataConverter.convertToModel(data) }
        }
    }

    override suspend fun addTagByInterval(
        newTag: String,
        data: List<PowerConsume>
    )
    = withContext(Dispatchers.IO) {
        data.forEach(){ model ->
            model.addTag(newTag)
            eonVisDao.updateData(
                id = model.id,
                tags = dataConverter.convertTagsToString(model.getTags()))
            networkService.addTag(id = model.id, tag = newTag)
        }
    }

    override suspend fun removeTagByInterval(
        tagToRemove: String,
        data: List<PowerConsume>)
            = withContext(Dispatchers.IO) {
        data.forEach(){ model ->
            model.removeTag(tagToRemove)
            eonVisDao.updateData(
                id = model.id,
                tags = dataConverter.convertTagsToString(model.getTags()))
            networkService.removeTag(id = model.id, tag = tagToRemove)
        }
    }

    override fun getTags(callback: (ArrayList<String>) -> Unit){
        networkService.getTags(callback = callback)
    }
}