package id.haonlabs.dicodingeventapp.repository

import android.content.Context
import id.haonlabs.dicodingeventapp.data.local.entity.FavoriteEvent
import id.haonlabs.dicodingeventapp.data.local.entity.FavoriteEventDao
import id.haonlabs.dicodingeventapp.data.local.room.FavoriteEventRoomDatabase

class FavoriteEventRepository(
    context: Context,
) {
    private val mFavoriteEventDao: FavoriteEventDao

    init {
        val db = FavoriteEventRoomDatabase.getDatabase(context)
        mFavoriteEventDao = db.favoriteEventDao()
    }

    suspend fun insert(favoriteEvent: FavoriteEvent) {
        mFavoriteEventDao.insert(favoriteEvent)
    }

    fun getFavoriteEventById(id: Int) = mFavoriteEventDao.getFavoriteEventById(id)

    suspend fun delete(favoriteEvent: FavoriteEvent) {
        mFavoriteEventDao.delete(favoriteEvent)
    }

    fun getFavoriteEvents() = mFavoriteEventDao.getFavoriteEvents()
}
