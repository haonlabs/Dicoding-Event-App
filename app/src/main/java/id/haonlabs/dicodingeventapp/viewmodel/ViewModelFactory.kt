package id.haonlabs.dicodingeventapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.haonlabs.dicodingeventapp.data.local.preference.SettingPreference
import id.haonlabs.dicodingeventapp.data.local.preference.dataStore
import id.haonlabs.dicodingeventapp.di.Injection
import id.haonlabs.dicodingeventapp.repository.EventRepository
import id.haonlabs.dicodingeventapp.repository.FavoriteEventRepository
import id.haonlabs.dicodingeventapp.repository.FinishedEventRepository
import id.haonlabs.dicodingeventapp.repository.SearchEventRepository
import id.haonlabs.dicodingeventapp.repository.UpcomingEventRepository
import id.haonlabs.dicodingeventapp.viewmodel.detail.DetailActivityViewModel
import id.haonlabs.dicodingeventapp.viewmodel.event.FinishedFragmentViewModel
import id.haonlabs.dicodingeventapp.viewmodel.event.UpcomingFragmentViewModel
import id.haonlabs.dicodingeventapp.viewmodel.favorite.FavoriteFragmentViewModel
import id.haonlabs.dicodingeventapp.viewmodel.search.SearchActivityViewModel
import id.haonlabs.dicodingeventapp.viewmodel.setting.SettingFragmentViewModel

class ViewModelFactory
private constructor(
    private val eventRepository: EventRepository,
    private val upcomingEventRepository: UpcomingEventRepository,
    private val finishedEventRepository: FinishedEventRepository,
    private val searchEventRepository: SearchEventRepository,
    private val favoriteEventRepository: FavoriteEventRepository,
    private val pref: SettingPreference,
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DetailActivityViewModel::class.java) ->
                DetailActivityViewModel(eventRepository, favoriteEventRepository) as T
            modelClass.isAssignableFrom(UpcomingFragmentViewModel::class.java) ->
                UpcomingFragmentViewModel(upcomingEventRepository) as T
            modelClass.isAssignableFrom(FinishedFragmentViewModel::class.java) ->
                FinishedFragmentViewModel(finishedEventRepository) as T
            modelClass.isAssignableFrom(SearchActivityViewModel::class.java) ->
                SearchActivityViewModel(searchEventRepository) as T
            modelClass.isAssignableFrom(FavoriteFragmentViewModel::class.java) ->
                FavoriteFragmentViewModel(favoriteEventRepository) as T
            modelClass.isAssignableFrom(SettingFragmentViewModel::class.java) ->
                SettingFragmentViewModel(pref) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance
                ?: synchronized(this) {
                        instance
                            ?: ViewModelFactory(
                                Injection.provideRepository(),
                                Injection.provideUpcomingEventsRepository(),
                                Injection.provideFinishedEventsRepository(),
                                Injection.provideSearchEventsRepository(),
                                Injection.provideFavoriteEventRepository(context),
                                SettingPreference.getInstance(context.dataStore),
                            )
                    }
                    .also { instance = it }
    }
}
