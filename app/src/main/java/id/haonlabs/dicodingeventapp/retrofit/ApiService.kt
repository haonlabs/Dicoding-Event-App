package id.haonlabs.dicodingeventapp.retrofit

import id.haonlabs.dicodingeventapp.data.response.EventDetailResponse
import id.haonlabs.dicodingeventapp.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events?active=1")
    fun getUpcomingEvents(@Query("limit") limit: Int = 40): Call<EventResponse>

    @GET("events?active=0")
    fun getFinishedEvents(@Query("limit") limit: Int = 40): Call<EventResponse>

    @GET("events?active=-1") fun searchEvents(@Query("q") q: String): Call<EventResponse>

    @GET("events/{id}") fun getEventDetails(@Path("id") id: Int): Call<EventDetailResponse>
}
