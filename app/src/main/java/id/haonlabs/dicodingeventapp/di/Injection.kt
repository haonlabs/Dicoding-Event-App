package id.haonlabs.dicodingeventapp.di

import android.content.Context
import id.haonlabs.dicodingeventapp.repository.EventRepository
import id.haonlabs.dicodingeventapp.repository.FavoriteEventRepository
import id.haonlabs.dicodingeventapp.repository.FinishedEventRepository
import id.haonlabs.dicodingeventapp.repository.SearchEventRepository
import id.haonlabs.dicodingeventapp.repository.UpcomingEventRepository
import id.haonlabs.dicodingeventapp.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        return EventRepository.getInstance(apiService)
    }

    fun provideUpcomingEventsRepository(context: Context): UpcomingEventRepository {
        val apiService = ApiConfig.getApiService()
        return UpcomingEventRepository.getInstance(apiService)
    }

    fun provideFinishedEventsRepository(context: Context): FinishedEventRepository {
        val apiService = ApiConfig.getApiService()
        return FinishedEventRepository.getInstance(apiService)
    }

    fun provideSearchEventsRepository(context: Context): SearchEventRepository {
        val apiService = ApiConfig.getApiService()
        return SearchEventRepository.getInstance(apiService)
    }

    fun provideFavoriteEventRepository(context: Context): FavoriteEventRepository {
        return FavoriteEventRepository(context)
    }
}
