package id.haonlabs.dicodingeventapp.viewmodel.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.haonlabs.dicodingeventapp.data.response.Event
import id.haonlabs.dicodingeventapp.data.response.EventDetailResponse
import id.haonlabs.dicodingeventapp.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivityViewModel : ViewModel() {
    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event> = _event

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getDetailEvent(id: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventDetails(id)
        client.enqueue(
            object : Callback<EventDetailResponse> {
                override fun onResponse(
                    call: Call<EventDetailResponse>,
                    response: Response<EventDetailResponse>,
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _event.value = response.body()?.event
                        _errorMessage.value = ""
                    } else {
                        Log.e("DetailActivityViewModel", response.message())
                        _errorMessage.value = response.message()
                    }
                }

                override fun onFailure(call: Call<EventDetailResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e("DetailActivityViewModel", "onFailure: ${t.message.toString()}")
                    _errorMessage.value = t.message.toString()
                }
            }
        )
    }
}
