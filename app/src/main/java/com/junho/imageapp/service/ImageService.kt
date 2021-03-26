package com.junho.imageapp.service

import android.app.*
import android.app.AlarmManager.INTERVAL_FIFTEEN_MINUTES
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LifecycleService
import com.junho.imageapp.R
import com.junho.imageapp.common.CommonConst.CHANNEL_ID
import com.junho.imageapp.database.format.ImageData
import com.junho.imageapp.receiver.AlarmReceiver
import com.junho.imageapp.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinApiExtension
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

@KoinApiExtension
class ImageService: LifecycleService() {
    private lateinit var remoteViews: RemoteViews
    private lateinit var builder: NotificationCompat.Builder
    private lateinit var imageList: ArrayList<ImageData>
    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent
    lateinit var mContext: Context
    private var mBound: Boolean = false

    private val binder = MainBinder()
    inner class MainBinder : Binder() {
        fun getService(): ImageService = this@ImageService
    }
    override fun onCreate() {
        super.onCreate()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        mContext = this
        imageList = intent!!.getSerializableExtra("imageDataList") as ArrayList<ImageData>
        remoteViews = RemoteViews(packageName, R.layout.noti_view)

        alarmMgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(this, 0, intent, 0)
        }
        // Set the alarm to start at 8:30 a.m.
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.MINUTE, get(Calendar.MINUTE) + 15)

        }

        // setRepeating() lets you specify a precise custom interval--in this case,
        // 20 minutes.
        alarmMgr?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis ,
            INTERVAL_FIFTEEN_MINUTES,
            alarmIntent
        )

        isInit = true
        makeWidget()
        return START_STICKY
    }

    val random = Random()
    fun rand(from: Int, to: Int) : Int {
        return random.nextInt(to - from) + from
    }

    fun makeRandThreeNum(size: Int , hashSet: HashSet<Int>) : HashSet<Int> {
        hashSet.add(rand(0, size))
        return hashSet
    }

    private fun makeRemoteViews() {
        if (imageList.size >= 3) {
            var imageQueue: HashSet<Int> = java.util.HashSet()
            while (true) {
                imageQueue =  makeRandThreeNum(imageList.size, imageQueue)
                if (imageQueue.size == 3) break
            }

            val imageArray = imageQueue.toArray()
            for (image in imageArray) {
                Log.d("image", image.toString())
            }
            remoteViews.setImageViewUri(R.id.first_image, Uri.parse(imageList[imageArray[0] as Int].imageUri))
            remoteViews.setImageViewUri(R.id.second_image, Uri.parse(imageList[imageArray[1] as Int].imageUri))
            remoteViews.setImageViewUri(R.id.third_image, Uri.parse(imageList[imageArray[2] as Int].imageUri))

        } else if (imageList.size == 2){
            remoteViews.setImageViewUri(R.id.first_image, Uri.parse(imageList[0].imageUri))
            remoteViews.setImageViewUri(R.id.second_image, Uri.parse(imageList[1].imageUri))
        } else if (imageList.size == 1) {
            remoteViews.setImageViewUri(R.id.first_image, Uri.parse(imageList[0].imageUri))
        }

    }

    fun refreshImage() {
        makeRemoteViews()
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, builder.build())
    }

    private fun makeWidget() {
        val appName = "Image 서비스"
        makeRemoteViews()
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
        alarmMgr?.cancel(alarmIntent)
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        mBound = true
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        mBound = false
        return super.onUnbind(intent)
    }



    companion object {
        var isInit = false
    }
}