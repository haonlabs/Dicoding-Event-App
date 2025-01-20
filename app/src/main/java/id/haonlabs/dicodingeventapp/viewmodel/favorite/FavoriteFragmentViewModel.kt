package id.haonlabs.dicodingeventapp.viewmodel.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import id.haonlabs.dicodingeventapp.data.local.entity.FavoriteEvent
import id.haonlabs.dicodingeventapp.repository.FavoriteEventRepository

class FavoriteFragmentViewModel(private val favoriteEventRepository: FavoriteEventRepository) :
    ViewModel() {
    lateinit var favoriteEvent: LiveData<List<FavoriteEvent>>

    fun getFavoriteEvents(): LiveData<List<FavoriteEvent>> {
        favoriteEvent = favoriteEventRepository.getFavoriteEvents()
        return favoriteEvent
    }
}
