package id.haonlabs.dicodingeventapp.viewmodel.event

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.haonlabs.dicodingeventapp.data.response.EventResponse
import id.haonlabs.dicodingeventapp.data.response.ListEventsItem
import id.haonlabs.dicodingeventapp.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingFragmentViewModel : ViewModel() {
    private val _listEvent = MutableLiveData<List<ListEventsItem>>()
    val listEvent: LiveData<List<ListEventsItem>> = _listEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getEvents(limit: Int = 40) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUpcomingEvents(limit)
        client.enqueue(
            object : Callback<EventResponse> {
                override fun onResponse(
                    call: Call<EventResponse>,
                    response: Response<EventResponse>,
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _listEvent.value = response.body()?.listEvents
                        _errorMessage.value = ""
                    } else {
                        Log.e("UpcomingViewModel", response.message())
                        _errorMessage.value = response.message()
                    }
                }

                override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                    _isLoading.value = false
                    _errorMessage.value = t.message.toString()
                    Log.e("UpcomingViewModel", "onFailure: ${t.message.toString()}")
                }
            }
        )
    }
}
