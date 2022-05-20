package hu.bme.aut.eonvis.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hu.bme.aut.eonvis.data.model.PowerConsume
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Default + viewModelJob)

    var daily: LiveData<List<PowerConsume>> = repository.getAll()

    private val _list = MutableLiveData<ArrayList<PowerConsume>>()
    var list: LiveData<ArrayList<PowerConsume>> = _list

    private val _lastUpdate = MutableLiveData<Long>()
    var lastUpdate: LiveData<Long> = _lastUpdate

    private var _dailyData = ArrayList<PowerConsume>()

    private var _monthlyData = ArrayList<PowerConsume>()

    private var _yearlyData = ArrayList<PowerConsume>()

    fun initializeList(){
        _list.value = daily.value as ArrayList<PowerConsume>?
        computeMonthlyAndYearlyData()
    }

    private fun computeMonthlyAndYearlyData() {
        daily.value?.let{
            _dailyData = daily.value as ArrayList<PowerConsume>
        }
        val monthIds = ArrayList<Long>()
        val yearIds = ArrayList<Long>()

        daily.value?.forEach { data ->
            if (!monthIds.contains(data.id.withoutLastTwo())){
                _monthlyData.add(
                    PowerConsume(data.id.withoutLastTwo(), data.incoming, data.outgoing, data.tagList.clone() as ArrayList<String>)
                )
                monthIds.add(data.id.withoutLastTwo())
            } else {
                val m = _monthlyData.find { m -> m.id == data.id.withoutLastTwo() }
                m?.let {
                    m.incoming = m.incoming.plus(data.incoming)
                    m.outgoing = m.outgoing.plus(data.outgoing)

                    //tags
                    val tagsToRemove = ArrayList<String>()
                    m.tagList.forEach {
                        if (!data.tagList.contains(it)){
                            tagsToRemove.add(it)
                        }
                    }
                    m.tagList.removeAll(tagsToRemove)
                }

            }
        }

        _monthlyData.forEach { data ->
            if (!yearIds.contains(data.id.withoutLastTwo())){
                _yearlyData.add(
                    PowerConsume(data.id.withoutLastTwo(), data.incoming, data.outgoing, data.tagList.clone() as ArrayList<String>)
                )
                yearIds.add(data.id.withoutLastTwo())
            } else {
                val m = _yearlyData.find { m -> m.id == data.id.withoutLastTwo() }
                m?.let {
                    m.incoming = m.incoming.plus(data.incoming)
                    m.outgoing = m.outgoing.plus(data.outgoing)

                    //tags
                    val tagsToRemove = ArrayList<String>()
                    m.tagList.forEach {
                        if (!data.tagList.contains(it)){
                            tagsToRemove.add(it)
                        }
                    }
                    m.tagList.removeAll(tagsToRemove)
                }
            }
        }
    }

    fun sync(lastUpdate: Long) {
        repository.syncData(uiScope,lastUpdate) {
            _lastUpdate.value = it
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun changeList(state: String) {
        if(state == "Daily"){
            _list.value = _dailyData
        } else if (state == "Monthly") {
            _list.value = _monthlyData
        } else if (state == "Yearly") {
            _list.value = _yearlyData
        }
    }
}

fun Long.withoutLastTwo():Long = this/100
fun Long.getDate():String = this.toString().substring(0,3) + "." + this.toString().substring(4,5) + "." + this.toString().substring(6,8)
