package hu.bme.aut.eonvis.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import hu.bme.aut.eonvis.data.model.PowerConsume

@Composable
fun Diff(incoming: Double, outgoing: Double) {
    if(incoming - outgoing > 0) {
        Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = "KeyboardArrowDown")
    } else {
        Icon(Icons.Rounded.KeyboardArrowUp, contentDescription = "KeyboardArrowUp")
    }
}

@Composable
fun MainListItem(data: PowerConsume, onClick: (PowerConsume) -> Unit) {
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

fun normalize(data: Double): String {
    val dataString = data.toString()
    val point = dataString.indexOf('.')
    return if(dataString.length > point + 4){
        dataString.substring(0, point+4)
    } else {
        dataString
    }
}

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