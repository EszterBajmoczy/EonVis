package hu.bme.aut.eonvis.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "powerConsume")
data class PowerConsumeDAO(
    @PrimaryKey
    val id: Long = 0,
    val incoming: Double,
    val outgoing: Double,
    val tags: String
)
