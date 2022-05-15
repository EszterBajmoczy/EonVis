package hu.bme.aut.eonvis.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import hu.bme.aut.eonvis.data.database.PowerConsumeDAO

@Dao
interface EonVisDao {
    @Insert
    fun insertData(data: PowerConsumeDAO)

    @Query("SELECT * FROM powerConsume")
    fun getAllData(): LiveData<List<PowerConsumeDAO>>

    @Query("UPDATE powerConsume SET tags = :tags WHERE id = :id")
    fun updateData(id: Long, tags: String)
}