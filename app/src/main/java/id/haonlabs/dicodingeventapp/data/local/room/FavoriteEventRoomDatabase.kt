package id.haonlabs.dicodingeventapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.haonlabs.dicodingeventapp.data.local.entity.FavoriteEvent
import id.haonlabs.dicodingeventapp.data.local.entity.FavoriteEventDao

@Database(entities = [FavoriteEvent::class], version = 1)
abstract class FavoriteEventRoomDatabase : RoomDatabase() {
    abstract fun favoriteEventDao(): FavoriteEventDao

    companion object {
        @Volatile private var INSTANCE: FavoriteEventRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavoriteEventRoomDatabase {
            if (INSTANCE == null) {
                synchronized(FavoriteEventRoomDatabase::class.java) {
                    INSTANCE =
                        Room.databaseBuilder(
                                context,
                                FavoriteEventRoomDatabase::class.java,
                                "favoriteevent_database",
                            )
                            .build()
                }
            }
            return INSTANCE as FavoriteEventRoomDatabase
        }
    }
}
