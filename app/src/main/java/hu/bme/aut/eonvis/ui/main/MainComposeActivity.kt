package hu.bme.aut.eonvis.ui.main

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        })

    }

    override fun onResume() {
        super.onResume()
        switchButtonTo("Daily")
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
                switchButtonTo("Daily")
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
                switchButtonTo("Monthly")
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
                switchButtonTo("Yearly")
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
                    MainListItem(data = it, onClick = onClick)
                })
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

    private fun switchButtonTo(to: String) {
        when (to) {
            "Daily" -> {
                dButtonSelected.value = selectedColor
                mButtonSelected.value = notSelectedColor
                yButtonSelected.value = notSelectedColor
            }
            "Monthly" -> {
                mButtonSelected.value = selectedColor
                dButtonSelected.value = notSelectedColor
                yButtonSelected.value = notSelectedColor
            }
            "Yearly" -> {
                yButtonSelected.value = selectedColor
                dButtonSelected.value = notSelectedColor
                mButtonSelected.value = notSelectedColor
            }
        }
        mainViewModel.changeList(to)
    }

}

