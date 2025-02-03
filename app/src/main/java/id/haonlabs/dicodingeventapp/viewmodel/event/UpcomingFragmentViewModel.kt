package id.haonlabs.dicodingeventapp.viewmodel.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import id.haonlabs.dicodingeventapp.data.response.ListEventsItem
import id.haonlabs.dicodingeventapp.repository.UpcomingEventRepository
import id.haonlabs.dicodingeventapp.utils.Result

class UpcomingFragmentViewModel(
    private val upcomingEventsRepository: UpcomingEventRepository,
) : ViewModel() {
    lateinit var listEvents: LiveData<Result<List<ListEventsItem>>>

    fun getUpcomingEvent(limit: Int): LiveData<Result<List<ListEventsItem>>> {
        listEvents = upcomingEventsRepository.getUpcomingEvent(limit)
        return listEvents
    }
}
