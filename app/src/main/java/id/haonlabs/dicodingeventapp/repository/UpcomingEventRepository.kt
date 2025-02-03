package id.haonlabs.dicodingeventapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import id.haonlabs.dicodingeventapp.data.response.EventResponse
import id.haonlabs.dicodingeventapp.data.response.ListEventsItem
import id.haonlabs.dicodingeventapp.retrofit.ApiService
import id.haonlabs.dicodingeventapp.utils.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingEventRepository private constructor(
    private val apiService: ApiService,
) {
    private val result = MediatorLiveData<Result<List<ListEventsItem>>>()

    fun getUpcomingEvent(limit: Int): LiveData<Result<List<ListEventsItem>>> {
        result.value = Result.Loading
        val client = apiService.getUpcomingEvents(limit)
        client.enqueue(
            object : Callback<EventResponse> {
                override fun onResponse(
                    call: Call<EventResponse>,
                    response: Response<EventResponse>,
                ) {
                    if (response.isSuccessful) {
                        result.value = Result.Success(response.body()!!.listEvents)
                    } else {
                        result.value = Result.Error(response.message())
                    }
                }

                override fun onFailure(
                    call: Call<EventResponse>,
                    t: Throwable,
                ) {
                    result.value = Result.Error(t.message.toString())
                }
            },
        )
        return result
    }

    companion object {
        @Volatile private var instance: UpcomingEventRepository? = null

        fun getInstance(apiService: ApiService): UpcomingEventRepository =
            instance
                ?: synchronized(this) {
                    instance ?: UpcomingEventRepository(apiService).also { instance = it }
                }
    }
}
