package com.junho.imageapp.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.*
import com.junho.imageapp.BuildConfig
import com.junho.imageapp.R
import com.junho.imageapp.database.format.ImageData
import com.junho.imageapp.databinding.ActivityMainBinding
import com.junho.imageapp.global.GlobalAppication
import com.junho.imageapp.service.ImageService
import com.junho.imageapp.view.adapter.MainAdapter
import com.junho.imageapp.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension


@KoinApiExtension
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {


    override val layoutResourceId: Int
        get() = R.layout.activity_main

    private val adSize: AdSize
        get() {
            val display = windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)

            val density = outMetrics.density

            var adWidthPixels = findViewById<FrameLayout>(R.id.ad_view_container).width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }

            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
        }
    private lateinit var mService: ImageService
    private var mBound: Boolean = false
    private val mainConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            mBound = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

            val binder = service as ImageService.MainBinder
            mService = binder.getService()
            mService.imageList = viewModel.imageDataList.value!!
            mBound = true
        }

    }

    override val viewModel: MainViewModel by viewModel()
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var mAdapter: MainAdapter
    private lateinit var mAdView: AdView
    private lateinit var imageAddText: TextView
    private lateinit var navHowTo: LinearLayout
    private lateinit var navSetting: LinearLayout
    private var initialLayoutComplete = false
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var switchNoti: Switch
    private val requestActivity: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult() // ◀ StartActivityForResult 처리를 담당
    ) { activityResult ->
        // action to do something
        if (activityResult == null) {
            return@registerForActivityResult
        } else {
            if (activityResult.data != null) {
                val selectedImage = activityResult.data!!.data
                val imageData= ImageData(imageUri = selectedImage.toString())
                viewModel.insertImageData(imageData)
            }
        }
    }


    override fun initStartView() {
        setStatusBar(this@MainActivity, R.color.colorPrimary)
        mRecyclerView =  findViewById<RecyclerView>(R.id.recyclerview_main)
        gridLayoutManager = GridLayoutManager(applicationContext, 2)
        switchNoti = findViewById(R.id.switch_noti)
        mRecyclerView.layoutManager = gridLayoutManager
        MobileAds.initialize(this) {}
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("ABCDEF012345"))
                .build()
        )

        mAdView = AdView(this)
        val viewContainer = findViewById<FrameLayout>(R.id.ad_view_container)
        viewContainer.addView(mAdView)
        viewContainer.viewTreeObserver.addOnGlobalLayoutListener {
            if (!initialLayoutComplete) {
                initialLayoutComplete = true
                loadBanner()
            }
        }
        imageAddText = findViewById(R.id.add_image_ment)
        navHowTo = findViewById(R.id.nav_lost_guide)
        navSetting = findViewById(R.id.nav_setting)
    }


    private fun loadBanner() {
        mAdView.adUnitId = if (BuildConfig.DEBUG)  {
            AD_UNIT_ID
        } else {
            AD_REAL_ID
        }

        mAdView.adSize = adSize

        // Create an ad request.
        val adRequest = AdRequest.Builder().build()

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest)
    }

    override fun initDataBinding() {
        mAdapter = MainAdapter(this@MainActivity, viewModel.imageDataList.value!!)
        viewModel.imageDataList.observe(this, {
            if (it.isEmpty()) {
                imageAddText.visibility = View.VISIBLE
            } else {
                imageAddText.visibility = View.GONE
            }
            mAdapter = MainAdapter(this@MainActivity, it)
            mAdapter.itemClick = object : MainAdapter.ItemClick {
                override fun onClick(view: View, position: Int) {
                    startActivity(Intent(this@MainActivity, InfoActivity::class.java).apply {
                        putExtra("imageData", it[position].imageUri)
                    })
                }

                override fun onLongClick(view: View, position: Int): Boolean {

                    return true
                }

                override fun deleteItem(image: ImageData) {
                    viewModel.deleteItem(image)
                }
            }
            mRecyclerView.adapter = mAdapter
            mRecyclerView.adapter!!.notifyDataSetChanged()
        })
        viewModel.fetchDataList()
    }

    override fun initAfterBinding() {
        setNavigationDrawer()

    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        requestActivity.launch(intent)
    }

    private fun setNavigationDrawer() {
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)

        val button = findViewById<ImageButton>(R.id.menu_button)
        button.setOnClickListener {
            if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                drawerLayout.closeDrawer(Gravity.RIGHT)
            } else {
                drawerLayout.openDrawer(Gravity.RIGHT)
            }
        }
        val packageInfo = packageManager.getPackageInfo(packageName, 0).versionName
        val appverText = findViewById<TextView>(R.id.appVersion)
        appverText.text = packageInfo
        val addButton = findViewById<Button>(R.id.btn_add_image)
        addButton.setOnClickListener {
            pickFromGallery()
        }
        navHowTo.setOnClickListener {
            startActivity(Intent(this, InfoActivity::class.java))
        }
        navSetting.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }
        switchNoti.isChecked = GlobalAppication.prefs.isSwitchOn
        if (GlobalAppication.prefs.isSwitchOn) {
            val imageservice = Intent(this, GlobalAppication.imageService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (!ImageService.isInit) {
                    startForegroundService(imageservice)
                }
            } else {
                if (!ImageService.isInit) {
                    startService(imageservice)
                }
            }
        }
        switchNoti.setOnCheckedChangeListener { _, isChecked ->
            val imageservice = Intent(this, GlobalAppication.imageService::class.java)
            imageservice.putExtra("imageDataList", viewModel.imageDataList.value)
             if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (!ImageService.isInit) {
                        startForegroundService(imageservice)
                    }
                } else {
                    if (!ImageService.isInit) {
                        startService(imageservice)
                    }
                }
                 Intent(this, ImageService::class.java).also {
                         intent -> bindService(intent, mainConnection, Context.BIND_AUTO_CREATE)
                 }
            } else {
                if (ImageService.isInit) {
                    if (mBound) {
                        unbindService(mainConnection)
                    }
                    stopService(imageservice)
                }
            }
            GlobalAppication.prefs.isSwitchOn = isChecked
        }
    }

    override fun onBackPressed() {
        finishAffinity()
        super.onBackPressed()
    }

    companion object {
        // This is an ad unit ID for a test ad. Replace with your own banner ad unit ID.
        private val AD_UNIT_ID = "ca-app-pub-3940256099942544/9214589741"
        private val AD_REAL_ID = "ca-app-pub-7696719602071323/3929623024"
    }
}