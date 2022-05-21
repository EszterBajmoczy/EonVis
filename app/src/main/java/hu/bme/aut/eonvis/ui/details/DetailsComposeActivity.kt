package hu.bme.aut.eonvis.ui.details

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
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

    private var newTag: String = "Create new tag"

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
                            .background(Color.White)
                    ) {
                        MyList { tag ->
                            val isNew = !tags.contains(tag)
                            if (isNew) {
                                tags.add(tag)
                                detailsViewModel.addTag(tagToAdd = tag)
                            }
                            setDetailsContent()
                        }
                        SimpleTextField()
                        Button(colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White),
                            onClick = {
                                if(newTag != "Create new tag"){
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

                Incoming()
                Outgoing()
                Difference()

                Tags()
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
    fun SimpleTextField() {
        var textValue by remember { mutableStateOf(newTag) }
        BasicTextField(
            value = textValue,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .background(Color.LightGray),
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
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier.scrollEnabled(
                enabled = true, //provide a mutable state boolean here
            )
        ) {
            items(
                items = tagStrings,
                itemContent = {
                    ListItem(tag = it, onClick = onClick)
                })
        }
    }

    @Composable
    fun ListItem(tag: String, onClick: (String) -> Unit) {
        Card(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .clickable { onClick(tag) },
            elevation = 2.dp,
            backgroundColor = Color.Blue,
            shape = RoundedCornerShape(corner = CornerSize(16.dp))
        ) {
            Text(text = tag, style = MaterialTheme.typography.h6, modifier = Modifier
                .padding(6.dp))
        }
    }

    @Composable
    fun Incoming() {
        Row(modifier = Modifier.padding(top = 24.dp)) {
            Column {
                Row {
                    Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = "KeyboardArrowDown")
                    Text(text = "incoming", style = MaterialTheme.typography.h6)
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 80.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = normalize(incoming) + " kWh",
                        style = MaterialTheme.typography.h6,
                        color = Color.Red
                    )
                }
            }
        }
    }

    @Composable
    fun Outgoing() {
        Row {
            Column {
                Row {
                    Icon(Icons.Rounded.KeyboardArrowUp, contentDescription = "KeyboardArrowUp")
                    Text(text = "outgoing", style = MaterialTheme.typography.h6)
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 80.dp),
                        horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = normalize(outgoing) + " kWh",
                        style = MaterialTheme.typography.h6,
                        color = Color.Green
                    )
                }
            }
        }
    }

    @Composable
    fun Difference() {
        Row(modifier = Modifier.padding(bottom = 24.dp, top = 16.dp)) {
            Column {
                Row {
                    Diff(incoming, outgoing)
                    Text(text = "difference", style = MaterialTheme.typography.h6)
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 80.dp),
                        horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = normalize(incoming - outgoing) + " kWh",
                        style = MaterialTheme.typography.h6
                    )
                }
            }
        }

    }

    private fun getViewTitle(): String {
        return when {
            id.getDataType() == DataType.Daily -> {
                "Daily - " + id.getDate()
            }
            id.getDataType() == DataType.Monthly -> {
                "Monthly - " + id.getMonth()
            }
            else -> {
                "Yearly - $id"
            }
        }
    }
}
