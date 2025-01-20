package id.haonlabs.dicodingeventapp.viewmodel.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import id.haonlabs.dicodingeventapp.data.response.ListEventsItem
import id.haonlabs.dicodingeventapp.repository.FinishedEventRepository
import id.haonlabs.dicodingeventapp.utils.Result

class FinishedFragmentViewModel(private val listEventsRepository: FinishedEventRepository) :
    ViewModel() {
    lateinit var listEvents: LiveData<Result<List<ListEventsItem>>>

    fun getFinishedEvent(limit: Int): LiveData<Result<List<ListEventsItem>>> {
        listEvents = listEventsRepository.getFinishedEvent(limit)
        return listEvents
    }
}
