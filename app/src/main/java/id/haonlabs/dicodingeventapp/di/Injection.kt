package id.haonlabs.dicodingeventapp.di

import android.content.Context
import id.haonlabs.dicodingeventapp.repository.EventRepository
import id.haonlabs.dicodingeventapp.repository.FavoriteEventRepository
import id.haonlabs.dicodingeventapp.repository.FinishedEventRepository
import id.haonlabs.dicodingeventapp.repository.SearchEventRepository
import id.haonlabs.dicodingeventapp.repository.UpcomingEventRepository
import id.haonlabs.dicodingeventapp.retrofit.ApiConfig

object Injection {
    fun provideRepository(): EventRepository {
        val apiService = ApiConfig.getApiService()
        return EventRepository.getInstance(apiService)
    }

    fun provideUpcomingEventsRepository(): UpcomingEventRepository {
        val apiService = ApiConfig.getApiService()
        return UpcomingEventRepository.getInstance(apiService)
    }

    fun provideFinishedEventsRepository(): FinishedEventRepository {
        val apiService = ApiConfig.getApiService()
        return FinishedEventRepository.getInstance(apiService)
    }

    fun provideSearchEventsRepository(): SearchEventRepository {
        val apiService = ApiConfig.getApiService()
        return SearchEventRepository.getInstance(apiService)
    }

    fun provideFavoriteEventRepository(context: Context): FavoriteEventRepository = FavoriteEventRepository(context)
}
