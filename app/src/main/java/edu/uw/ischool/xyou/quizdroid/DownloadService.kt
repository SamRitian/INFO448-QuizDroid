package edu.uw.ischool.xyou.quizdroid

import android.app.AlertDialog
import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import java.io.IOException

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
class DownloadService : IntentService("DownloadService") {
    private val TAG = "DownloadService"

    private lateinit var mHandler: Handler
    private var isRunning = false
    private var url = ""
    private var frequency = 20

    override fun onCreate() {
        Log.i(TAG, "Download service created")
        super.onCreate()
        mHandler = Handler()

        // set up notification channel
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("Download_Chanel", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        url = intent?.getStringExtra("url")!!
        frequency = intent.getIntExtra("frequency", 10)
        isRunning = true

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onHandleIntent(intent: Intent?) {
        while (isRunning) {
            Log.i(TAG, "Downloading data from $url")
            mHandler.post {
                Toast.makeText(this, "Downloading data from $url", Toast.LENGTH_SHORT).show()
            }

            downloadData(url)

            try {
                Thread.sleep((frequency * 1000 * 60).toLong())
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
    }

    private fun downloadData(url: String) {
        val notificationId = System.currentTimeMillis().toInt()
        val builder = Notification.Builder(this, "Download_Chanel")
            .setContentTitle("Downloading data")
            .setContentText("from $url")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(Notification.PRIORITY_DEFAULT)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, builder.build())

        var jsonString = ""
        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                jsonString = response.toString()
                // save this json string to a file in the app's internal storage
                val fileName = "new_questions.json"

                try {
                    openFileOutput(fileName, MODE_PRIVATE).use { outputStream ->
                        outputStream.write(jsonString.toByteArray())

                        val successNotificationId = System.currentTimeMillis().toInt() // Unique ID for this notification
                        val successBuilder = Notification.Builder(this, "Download_Chanel").apply {
                            setContentTitle("Download Complete")
                            setContentText("Data has been successfully downloaded.")
                            setSmallIcon(android.R.drawable.stat_sys_download_done) // Use an actual icon in your drawable resources
                            setAutoCancel(true)
                        }

                        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        notificationManager.notify(successNotificationId, successBuilder.build())
                    }
                } catch (e: IOException) {
                    showRetryQuitDialog()
                }
            },
            { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            })

        VolleyService.getInstance(this).add(request)
    }

    override fun onDestroy() {
        isRunning = false
        super.onDestroy()
    }

    private fun showRetryQuitDialog() {
        AlertDialog.Builder(this)
            .setTitle("Download Failed")
            .setMessage("An error occurred while saving the data. Would you like to retry?")
            .setPositiveButton("Retry") { _, _ ->
                downloadData(url)
            }
            .setNegativeButton("Quit", null)
            .show()
    }
}