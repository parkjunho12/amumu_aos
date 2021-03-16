package com.junho.imageapp.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.junho.imageapp.R
import com.junho.imageapp.common.CommonConst.CHANNEL_ID
import com.junho.imageapp.database.format.ImageData
import com.junho.imageapp.view.MainActivity
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ImageService: LifecycleService() {
    private lateinit var remoteViews: RemoteViews
    private lateinit var builder: NotificationCompat.Builder
    private lateinit var imageList: ArrayList<ImageData>
    override fun onCreate() {
        super.onCreate()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        imageList = intent!!.getSerializableExtra("imageDataList") as ArrayList<ImageData>
        remoteViews = RemoteViews(packageName, R.layout.noti_view)
        isInit = true
        makeWidget()
        return START_STICKY
    }

    private fun makeWidget() {
        val appName = "Image 서비스"
        if (imageList.size >= 3) {
            remoteViews.setImageViewUri(R.id.first_image, Uri.parse(imageList[0].imageUri))
            remoteViews.setImageViewUri(R.id.second_image, Uri.parse(imageList[1].imageUri))
            remoteViews.setImageViewUri(R.id.third_image, Uri.parse(imageList[2].imageUri))
        } else if (imageList.size == 2){
            remoteViews.setImageViewUri(R.id.first_image, Uri.parse(imageList[0].imageUri))
            remoteViews.setImageViewUri(R.id.second_image, Uri.parse(imageList[1].imageUri))
        } else if (imageList.size == 1) {
            remoteViews.setImageViewUri(R.id.first_image, Uri.parse(imageList[0].imageUri))
        }
        if (Build.VERSION.SDK_INT >= 26) {

            builder = NotificationCompat.Builder(this, CHANNEL_ID)

            val notificationIntent = Intent(this, MainActivity::class.java)
                .apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
            val pendingIntents = PendingIntent.getActivity(this, 0, notificationIntent, 0)
            val channel = NotificationChannel(
                CHANNEL_ID,
                appName,
                NotificationManager.IMPORTANCE_LOW
            )


            val style = NotificationCompat.DecoratedCustomViewStyle()


            builder.apply {
                setSmallIcon(R.mipmap.ic_launcher)
                setOngoing(true)
                setCustomContentView(remoteViews)
                setCustomBigContentView(remoteViews)
                setCustomHeadsUpContentView(remoteViews)
                setWhen(0)
                setShowWhen(false)
                setContentIntent(pendingIntents)
                priority = NotificationCompat.PRIORITY_HIGH
                setVisibility(NotificationCompat.VISIBILITY_SECRET)
            }


            val notificationManager =
                (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
            notificationManager.createNotificationChannel(
                channel
            )

            startForeground(1, builder.build())
        } else {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification: Notification.Builder = Notification.Builder(this)
            val notificationIntent = Intent(this, MainActivity::class.java)
                .apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
            val pendingIntents = PendingIntent.getActivity(this, 0, notificationIntent, 0)
            notification.apply {
                setSmallIcon(R.mipmap.ic_launcher)
                setContentText(null)
                setContentTitle(null)
                setOngoing(true)
                setWhen(0)
                setContentIntent(pendingIntents)
            }
            startForeground(1, notification.build())

        }
    }

    override fun onDestroy() {
        isInit = false
        super.onDestroy()
    }



    companion object {
        var isInit = false
    }
}