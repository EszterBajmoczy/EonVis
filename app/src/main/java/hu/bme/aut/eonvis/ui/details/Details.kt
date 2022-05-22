package hu.bme.aut.eonvis.ui.details

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import hu.bme.aut.eonvis.ui.main.Diff
import hu.bme.aut.eonvis.ui.main.normalize

@Composable
fun Difference(incoming: Double, outgoing: Double) {
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

@Composable
fun Outgoing(outgoing: Double) {
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
fun Incoming(incoming: Double) {
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
fun TagListItem(tag: String, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clickable { onClick(tag) },
        elevation = 2.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(corner = CornerSize(16.dp))
    ) {
        Text(text = tag, style = MaterialTheme.typography.h6, modifier = Modifier
            .padding(6.dp))
    }
}