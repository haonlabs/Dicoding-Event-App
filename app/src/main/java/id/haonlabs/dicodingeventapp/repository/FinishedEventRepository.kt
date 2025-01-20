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

class FinishedEventRepository private constructor(private val apiService: ApiService) {

    private val result = MediatorLiveData<Result<List<ListEventsItem>>>()

    fun getFinishedEvent(limit: Int): LiveData<Result<List<ListEventsItem>>> {
        result.value = Result.Loading
        val client = apiService.getFinishedEvents(limit)
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

                override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                    result.value = Result.Error(t.message.toString())
                }
            }
        )
        return result
    }

    companion object {
        @Volatile private var instance: FinishedEventRepository? = null

        fun getInstance(apiService: ApiService): FinishedEventRepository {
            return instance
                ?: synchronized(this) {
                    instance ?: FinishedEventRepository(apiService).also { instance = it }
                }
        }
    }
}
