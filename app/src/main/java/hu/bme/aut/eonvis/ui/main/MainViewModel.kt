package hu.bme.aut.eonvis.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import hu.bme.aut.eonvis.data.model.PowerConsume
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Default + viewModelJob)

    private var _data = repository.getAll()
    val data: LiveData<List<PowerConsume>> = _data

    init {
        sync(20220501) {
            var s = 102
        }
        _data = repository.getAll()
    }

    fun sync(lastUpdate: Long, callback: (Long) -> Unit) {
        repository.syncData(uiScope,lastUpdate) {
            _data = repository.getAll()
            callback(it)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}