package hu.bme.aut.eonvis.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import hu.bme.aut.eonvis.data.DataType
import hu.bme.aut.eonvis.data.model.PowerConsume
import hu.bme.aut.eonvis.ui.main.getDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject


class DetailsViewModel @Inject constructor(private val repository: DetailsRepository) : ViewModel(){
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Default + viewModelJob)

    private var first: Long = 0
    private var last: Long = 0

    var data: LiveData<List<PowerConsume>>? = null

    private val _tagList = MutableLiveData<ArrayList<String>>()
    var tagList: LiveData<ArrayList<String>> = _tagList

    fun dataSets(incomingColor: Int, outgoingColor: Int): ArrayList<ILineDataSet> {
        val dataSets: ArrayList<ILineDataSet> = ArrayList()

        val labels = ArrayList<String>()
        val incomingList = ArrayList<Entry>()
        val outgoingList = ArrayList<Entry>()

        data?.value?.forEach { value ->
            labels.add(value.id.getDate())
            incomingList.add(Entry(value.id.toFloat(), value.incoming.toFloat()))
            outgoingList.add(Entry(value.id.toFloat(), value.outgoing.toFloat()))
        }
        val incomingLineDataSet = LineDataSet(
            incomingList,
            "incoming"
        )
        incomingLineDataSet.setDrawCircles(true)
        incomingLineDataSet.circleRadius = 4f
        incomingLineDataSet.setDrawValues(false)
        incomingLineDataSet.lineWidth = 3f
        incomingLineDataSet.color = incomingColor
        incomingLineDataSet.setCircleColor(incomingColor)
        dataSets.add(incomingLineDataSet)

        val outgoingLineDataSet = LineDataSet(
            outgoingList,
            "outgoing"
        )
        outgoingLineDataSet.setDrawCircles(true)
        outgoingLineDataSet.circleRadius = 4f
        outgoingLineDataSet.setDrawValues(false)
        outgoingLineDataSet.lineWidth = 3f
        outgoingLineDataSet.color = outgoingColor
        outgoingLineDataSet.setCircleColor(outgoingColor)
        dataSets.add(outgoingLineDataSet)


        return dataSets
    }

    fun loadData(firstId: Long, type: DataType) {
        when (type) {
            DataType.Daily -> {
                first = firstId
                last = first

            }
            DataType.Monthly -> {
                first = firstId*100
                last = first + 31
            }
            DataType.Yearly -> {
                first = firstId*10000
                last = first + 10000
            }
        }

        data = repository.getAllByInterval(firstId = first, lastId = last)
    }

    fun addTag(tagToAdd: String) {
        data?.value?.let {
            uiScope.launch {
                repository.addTagsByInterval(newTag = tagToAdd, it)
            }
        }
    }

    fun removeTag(tagToRemove: String) {
        data?.value?.let {
            _tagList.value?.remove(tagToRemove)
            uiScope.launch {
                repository.removeTagByInterval(tagToRemove = tagToRemove, it)
            }
        }
    }

    fun getTags() {
        repository.getTags { tags ->
            if (_tagList.value == null) {
                _tagList.value = tags
            }

        }
    }
}