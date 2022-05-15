package hu.bme.aut.eonvis.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hu.bme.aut.eonvis.data.database.ArrayTypeConverter
import hu.bme.aut.eonvis.data.database.PowerConsumeDAO

@Database(
    version = 1,
    exportSchema = false,
    entities = [PowerConsumeDAO::class]
)
@TypeConverters(
    ArrayTypeConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eonVisDao(): EonVisDao
}