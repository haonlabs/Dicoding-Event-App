package id.haonlabs.dicodingeventapp.viewmodel.search

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

class SearchActivityViewModel : ViewModel() {
    private val _listEvent = MutableLiveData<List<ListEventsItem>>()
    val listEvent: LiveData<List<ListEventsItem>> = _listEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _resultText = MutableLiveData<String>()
    val resultText: LiveData<String> = _resultText

    fun searchEvent(keyword: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchEvents(keyword)
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
                        if (response.body()?.listEvents?.isEmpty() == true) {
                            _resultText.value = "Tidak ada hasil pencarian untuk \"${keyword}\""
                        } else {
                            _resultText.value = ""
                        }
                    } else {
                        Log.e("SearchActivityViewModel", response.message())
                        _errorMessage.value = response.message()
                    }
                }

                override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                    _isLoading.value = false
                    _errorMessage.value = t.message.toString()
                    _resultText.value = ""
                    Log.e("SearchActivityViewModel", "onFailure: ${t.message.toString()}")
                }
            }
        )
    }
}
