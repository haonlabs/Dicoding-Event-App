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

class SearchEventRepository private constructor(private val apiService: ApiService) {

    private val result = MediatorLiveData<Result<List<ListEventsItem>>>()

    fun searchEvents(keyword: String): LiveData<Result<List<ListEventsItem>>> {
        result.value = Result.Loading
        val client = apiService.searchEvents(keyword)
        client.enqueue(
            object : Callback<EventResponse> {
                override fun onResponse(
                    call: Call<EventResponse>,
                    response: Response<EventResponse>,
                ) {
                    if (response.isSuccessful) {
                        result.value = Result.Success(response.body()!!.listEvents)
                        if (response.body()?.listEvents?.isEmpty() == true) {
                            result.value =
                                Result.Error("Tidak ada hasil pencarian untuk \"${keyword}\"")
                        }
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
        @Volatile private var instance: SearchEventRepository? = null

        fun getInstance(apiService: ApiService): SearchEventRepository {
            return instance
                ?: synchronized(this) {
                    instance ?: SearchEventRepository(apiService).also { instance = it }
                }
        }
    }
}
