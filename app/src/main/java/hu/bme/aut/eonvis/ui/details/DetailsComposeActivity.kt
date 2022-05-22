package hu.bme.aut.eonvis.ui.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Observer
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.eonvis.R
import hu.bme.aut.eonvis.data.DataType
import hu.bme.aut.eonvis.ui.details.ui.theme.EonVisTheme
import hu.bme.aut.eonvis.ui.main.*
import javax.inject.Inject


@AndroidEntryPoint
class DetailsComposeActivity : ComponentActivity() {
    @Inject
    lateinit var detailsViewModel: DetailsViewModel

    private var id: Long = 0
    private var incoming: Double = 0.0
    private var outgoing: Double = 0.0
    private var tags = ArrayList<String>()
    private var type = DataType.Daily

    private var tagList = mutableStateOf(ArrayList<String>())

    private var newTag: String = "New tag"

    private var lineChart: LineChart? = null
    private var lineChartData = ArrayList<ILineDataSet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        id = intent.getLongExtra("id", 0)
        incoming = intent.getDoubleExtra("incoming", 0.0)
        outgoing = intent.getDoubleExtra("outgoing", 0.0)
        val t = intent.getStringArrayListExtra("tags")
        t?.let {
            tags.addAll(t)
            tags.remove("")
        }
        val dataType = intent.getStringExtra("type")
        dataType?.let {
            type = DataType.valueOf(dataType)
        }

        detailsViewModel.loadData(id, type)
        detailsViewModel.getTags()

        detailsViewModel.data?.observe(this, Observer {
            if(!it.isNullOrEmpty()){
                setDetailsContent()
            }
        })

        detailsViewModel.tagList.observe(this, Observer { list ->
            tagList.value = list
        })

        setContentView(R.layout.activity_details)

    }

    private fun configureLineChart() {
        val desc = Description()
        desc.textSize = 28F
        lineChart!!.description = desc
        val xAxis = lineChart!!.xAxis
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                if(type == DataType.Yearly)
                    return value.toLong().getMonthAndDay()
                return value.toLong().getDay()
            }
        }
    }

    private fun setDetailsContent() {
        setContent {
            EonVisTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    Details()
                }
            }
        }
    }

    private fun setTagContent() {
        setContent {
            EonVisTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(Color.LightGray)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        )  {
                            MyList { tag ->
                                val isNew = !tags.contains(tag)
                                if (isNew) {
                                    tags.add(tag)
                                    detailsViewModel.addTag(tagToAdd = tag)
                                }
                                setDetailsContent()
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth().background(Color.White),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            EditTextField()
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.White),
                                onClick = {
                                    if(newTag != "New tag"){
                                        tags.add(newTag)
                                        detailsViewModel.addTag(tagToAdd = newTag)
                                        setDetailsContent()
                                    }
                                }) {
                                Text(text = "Add")
                            }
                        }
                    }
                }
            }
        }
    }


    @Composable()
    fun Details() {
        Scaffold(content = {
            Column(modifier = Modifier.fillMaxHeight(1F)) {
                Row(modifier = Modifier
                    .background(Color.LightGray)
                    .fillMaxWidth(1F)
                    .align(Alignment.CenterHorizontally)
                    .padding(24.dp),
                    horizontalArrangement = Arrangement.Center) {
                    Text(text = getViewTitle(), style = MaterialTheme.typography.h4)
                }

                Incoming(incoming)
                Outgoing(outgoing)
                Difference(incoming, outgoing)

                Tags()

                if(type != DataType.Daily){

                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { context: Context ->
                            val view = LayoutInflater.from(context)
                                .inflate(R.layout.activity_details, null, false)

                            lineChart = view.findViewById(R.id.activity_main_linechart)

                            configureLineChart()
                            lineChartData = detailsViewModel.dataSets(resources.getColor(R.color.red, null), resources.getColor(R.color.green, null))
                            val lineData = LineData(lineChartData)
                            lineChart!!.data = lineData
                            lineChart!!.invalidate()
                            view
                        }
                    )
                }
            }
        })
    }

    @Composable
    fun Tags() {
        Row(modifier = Modifier
            .padding(top = 24.dp)
            .background(Color.LightGray),
            horizontalArrangement = Arrangement.Center) {
            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                Row {
                    Text(text = "Tags", style = MaterialTheme.typography.h6)
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White),
                        onClick = {
                            setTagContent()
                        }) {
                        Text(text = "+")
                    }
                }
            }
        }
        tags.forEach { tag ->
            Row(modifier = Modifier.background(Color.Gray)) {
                Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                    Row {
                        Text(text = tag, style = MaterialTheme.typography.h6)
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White),
                            onClick = {
                                tags.remove(tag)
                                detailsViewModel.removeTag(tagToRemove = tag)
                            }) {
                            Text(text = "-")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun EditTextField() {
        var textValue by remember { mutableStateOf(newTag) }
        BasicTextField(
            value = textValue,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            // Update value of textValue with the latest value of the text field
            onValueChange = {
                textValue = it
                newTag = it
            }
        )
    }

    @Composable
    fun MyList(onClick: (String) -> Unit) {
        val tagStrings by tagList
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier.scrollEnabled(
                enabled = true, //provide a mutable state boolean here
            )
        ) {
            items(
                items = tagStrings,
                itemContent = {
                    TagListItem(tag = it, onClick = onClick)
                })
        }
    }

    private fun getViewTitle(): String {
        return when {
            id.getDataType() == DataType.Daily -> {
                "Daily - " + id.getDate()
            }
            id.getDataType() == DataType.Monthly -> {
                "Monthly - " + id.getMonthAndYear()
            }
            else -> {
                "Yearly - $id"
            }
        }
    }
}
