package id.haonlabs.dicodingeventapp.viewmodel.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import id.haonlabs.dicodingeventapp.data.response.ListEventsItem
import id.haonlabs.dicodingeventapp.repository.SearchEventRepository
import id.haonlabs.dicodingeventapp.utils.Result

class SearchActivityViewModel(
    private val listEventsRepository: SearchEventRepository,
) : ViewModel() {
    lateinit var listEvents: LiveData<Result<List<ListEventsItem>>>

    fun searchEvents(keyword: String): LiveData<Result<List<ListEventsItem>>> {
        listEvents = listEventsRepository.searchEvents(keyword)
        return listEvents
    }
}
