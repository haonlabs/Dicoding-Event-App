package id.haonlabs.dicodingeventapp.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import id.haonlabs.dicodingeventapp.R
import id.haonlabs.dicodingeventapp.data.response.EventResponse
import id.haonlabs.dicodingeventapp.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReminderWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    companion object {
        private val TAG = ReminderWorker::class.java.simpleName
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "dicoding channel"
    }

    private var resultStatus: Result? = null

    override fun doWork(): Result {
        return getUpcomingEvent()
    }

    private fun getUpcomingEvent(): Result {
        Looper.prepare()
        val apiService = ApiConfig.getApiService()
        val client = apiService.getOneUpcomingEvent()
        client.enqueue(
            object : Callback<EventResponse> {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<EventResponse>,
                    response: Response<EventResponse>,
                ) {
                    if (response.isSuccessful) {
                        val listEvents = response.body()?.listEvents
                        val title = listEvents?.get(0)?.name
                        val time = listEvents?.get(0)?.beginTime
                        showNotification(title, time)
                        Log.d(TAG, "onSuccess: Selesai.....")
                        resultStatus = Result.success()
                    } else {
                        showNotification("Event sedang tidak ada", response.message())
                        resultStatus = Result.failure()
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                    showNotification("Gagal mendapatkan event terdekat", t.message.toString())
                    Log.d(TAG, "onSuccess: Gagal.....")
                    resultStatus = Result.failure()
                }
            }
        )
        return resultStatus ?: Result.failure()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(title: String?, description: String?) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_upcoming_black)
                .setSubText("Event yang akan datang")
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
        val channel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        notification.setChannelId(CHANNEL_ID)
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }
}
