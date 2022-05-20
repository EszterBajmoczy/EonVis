package hu.bme.aut.eonvis.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.eonvis.data.model.PowerConsume
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        setContent {
            Greeting("Android")
        }
/*
        mainViewModel.daily.observe(this, Observer {
            mainViewModel.computeMonthlyANdYearlyData(data)
        })

        mainViewModel.monthly.observe(this, Observer {
            var s = 2
        })

 */
    }
}

@Composable
private fun DataRow(data: PowerConsume, onClick: (PowerConsume) -> Unit) {
    Row(modifier = Modifier.clickable(onClick = { onClick(data) }).fillMaxWidth().padding(8.dp)) {



        Column (modifier = Modifier.padding(start = 8.dp)) {
            Text(text = data.id.toString(), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.h6)
            Text(text = data.tagList.toString())

        }
    }
}


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview
@Composable
fun DefaultPreview() {
    Greeting("Android")
}