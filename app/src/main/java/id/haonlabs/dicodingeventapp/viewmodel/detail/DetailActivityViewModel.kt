package id.haonlabs.dicodingeventapp.viewmodel.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.haonlabs.dicodingeventapp.data.local.entity.FavoriteEvent
import id.haonlabs.dicodingeventapp.data.response.Event
import id.haonlabs.dicodingeventapp.repository.EventRepository
import id.haonlabs.dicodingeventapp.repository.FavoriteEventRepository
import id.haonlabs.dicodingeventapp.utils.Result
import kotlinx.coroutines.launch

class DetailActivityViewModel(
    private val eventRepository: EventRepository,
    private val favoriteEventRepository: FavoriteEventRepository,
) : ViewModel() {
    lateinit var event: LiveData<Result<Event>>
    private lateinit var favoriteEvent: LiveData<FavoriteEvent>

    fun getDetailEvent(id: Int): LiveData<Result<Event>> {
        event = eventRepository.getDetailEvent(id)
        return event
    }

    fun insert(favoriteEvent: FavoriteEvent) {
        viewModelScope.launch { favoriteEventRepository.insert(favoriteEvent) }
    }

    fun getFavoriteEventById(id: Int): LiveData<FavoriteEvent> {
        favoriteEvent = favoriteEventRepository.getFavoriteEventById(id)
        return favoriteEvent
    }

    fun delete(favoriteEvent: FavoriteEvent) {
        viewModelScope.launch { favoriteEventRepository.delete(favoriteEvent) }
    }
}
