package hu.bme.aut.eonvis.ui.main

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.eonvis.R
import hu.bme.aut.eonvis.data.model.PowerConsume
import hu.bme.aut.eonvis.ui.details.DetailsComposeActivity
import hu.bme.aut.eonvis.ui.main.ui.theme.EonVisTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainComposeActivity : ComponentActivity() {
    @Inject
    lateinit var mainViewModel: MainViewModel

    private var selectedColor = Color.White
    private var notSelectedColor = Color.LightGray

    private val dButtonSelected = mutableStateOf(selectedColor)
    private val mButtonSelected = mutableStateOf(notSelectedColor)
    private val yButtonSelected = mutableStateOf(notSelectedColor)

    private val dataList = mutableStateOf(ArrayList<PowerConsume>())

    private var internetAvailableMsg = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        val lastUpdate = sharedPref.getLong(getString(R.string.last_update), 0)

        if(isNetworkAvailable()){
            mainViewModel.sync(lastUpdate = lastUpdate)
        } else {
            internetAvailableMsg = "No internet connection, last update: " + lastUpdate.getDate()
        }

        mainViewModel.lastUpdate.observe(this, Observer {
            with (sharedPref.edit()) {
                putLong(getString(R.string.last_update), it)
                apply()
            }
        })

        mainViewModel.daily.observe(this, Observer { data ->
            data?.let{
                mainViewModel.initializeList()
                mainViewModel.list.observe(this, Observer {
                    dataList.value = it
                })
            }
        })
        setContent {
            EonVisTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    MyApp {
                        val intent = Intent(this@MainComposeActivity, DetailsComposeActivity::class.java)
                        intent.putExtra("id", it.id)
                        intent.putExtra("incoming", it.incoming)
                        intent.putExtra("outgoing", it.outgoing)
                        intent.putExtra("tags", it.tagList)
                        intent.putExtra("type", it.id.getDataType().name)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        try {
            val cm = applicationContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cm.activeNetworkInfo
            if (networkInfo != null && networkInfo.isConnected) {
                return true
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return false
    }

    @Composable
    fun MyApp(onClick: (PowerConsume) -> Unit) {
        Scaffold(
            content = {
                Column {
                    Row(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        DayButton()
                        MonthButton()
                        YearButton()
                    }
                    if(internetAvailableMsg.isNotEmpty()){
                        Text(text = internetAvailableMsg, modifier = Modifier.align(Alignment.CenterHorizontally), color = Color.Red)
                    }
                    MyList(onClick)
                }

            }
        )
    }

    @Composable
    fun DayButton() {
        val color by dButtonSelected
        Button(colors = ButtonDefaults.buttonColors(
            backgroundColor = color),
            onClick = {
                dButtonSelected.value = selectedColor
                mButtonSelected.value = notSelectedColor
                yButtonSelected.value = notSelectedColor
                mainViewModel.changeList("Daily")
        }, modifier = Modifier.padding(2.dp)) {
            Text(text = "Daily")
        }
    }

    @Composable
    fun MonthButton() {
        val color by mButtonSelected
        Button(colors = ButtonDefaults.buttonColors(
            backgroundColor = color),
            onClick = {
                mButtonSelected.value = selectedColor
                dButtonSelected.value = notSelectedColor
                yButtonSelected.value = notSelectedColor
                mainViewModel.changeList("Monthly")
            }, modifier = Modifier.padding(2.dp)) {
            Text(text = "Monthly")
        }
    }

    @Composable
    fun YearButton() {
        val color by yButtonSelected
        Button(colors = ButtonDefaults.buttonColors(
            backgroundColor = color),
            onClick = {
                yButtonSelected.value = selectedColor
                dButtonSelected.value = notSelectedColor
                mButtonSelected.value = notSelectedColor
                mainViewModel.changeList("Yearly")
            }, modifier = Modifier.padding(2.dp)) {
            Text(text = "Yearly")
        }
    }


    @Composable
    fun MyList(onClick: (PowerConsume) -> Unit) {
        val data by dataList
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier.scrollEnabled(
                enabled = true, //provide a mutable state boolean here
            )
        ) {
            items(
                items = data,
                itemContent = {
                    ListItem(data = it, onClick = onClick)
                })
        }
    }

    @Composable
    fun ListItem(data: PowerConsume, onClick: (PowerConsume) -> Unit) {
        Card(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth()
                .clickable { onClick(data) },
            elevation = 2.dp,
            backgroundColor = Color.White,
            shape = RoundedCornerShape(corner = CornerSize(16.dp))
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                ) {
                    Text(text = data.id.getDate(), style = MaterialTheme.typography.h6)
                    Text(text = list(data.tagList), style = MaterialTheme.typography.caption)
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxWidth()
                        .fillMaxHeight()
                ){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ){
                        Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = "KeyboardArrowDown")
                        Text(text = normalize(data.incoming) + " kWh", style = MaterialTheme.typography.caption, color = Color.Red)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ){
                        Icon(Icons.Rounded.KeyboardArrowUp, contentDescription = "KeyboardArrowUp")
                        Text(text = normalize(data.outgoing) + " kWh", style = MaterialTheme.typography.caption, color = Color.Green)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ){
                        Diff(data.incoming, data.outgoing)
                        Text(text = normalize(data.incoming - data.outgoing) + " kWh", style = MaterialTheme.typography.caption)
                    }
                }
            }
        }
    }
}

fun normalize(data: Double): String {
    val dataString = data.toString()
    val point = dataString.indexOf('.')
    return if(dataString.length > point + 4){
        dataString.substring(0, point+4)
    } else {
        dataString
    }
}

@Composable
fun Diff(incoming: Double, outgoing: Double) {
    if(incoming - outgoing > 0) {
        Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = "KeyboardArrowDown")
    } else {
        Icon(Icons.Rounded.KeyboardArrowUp, contentDescription = "KeyboardArrowUp")
    }
}

fun Modifier.scrollEnabled(
    enabled: Boolean,
) = nestedScroll(
    connection = object : NestedScrollConnection {
        override fun onPreScroll(
            available: Offset,
            source: NestedScrollSource
        ): Offset = if(enabled) Offset.Zero else available
    }
)

fun list(list: ArrayList<String>): String {
    var result = ""
    list.forEach { element ->
        result += if (result.isEmpty()){
            element
        } else {
            " $element"
        }
    }
    return result
}

