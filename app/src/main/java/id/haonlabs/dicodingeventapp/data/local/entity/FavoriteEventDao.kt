package id.haonlabs.dicodingeventapp.data.local.entity

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteEventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteEvent: FavoriteEvent)

    @Query("SELECT * FROM FavoriteEvent WHERE id = :id")
    fun getFavoriteEventById(id: Int): LiveData<FavoriteEvent>

    @Delete suspend fun delete(favoriteEvent: FavoriteEvent)

    @Query("SELECT * FROM FavoriteEvent") fun getFavoriteEvents(): LiveData<List<FavoriteEvent>>
}
