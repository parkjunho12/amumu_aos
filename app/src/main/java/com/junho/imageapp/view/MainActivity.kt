package com.junho.imageapp.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.Switch
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.junho.imageapp.R
import com.junho.imageapp.database.format.ImageData
import com.junho.imageapp.databinding.ActivityMainBinding
import com.junho.imageapp.service.ImageService
import com.junho.imageapp.view.adapter.MainAdapter
import com.junho.imageapp.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension


@KoinApiExtension
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {


    override val layoutResourceId: Int
        get() = R.layout.activity_main

    override val viewModel: MainViewModel by viewModel()
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var mAdapter: MainAdapter
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var switchNoti: Switch
    private val requestActivity: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult() // ◀ StartActivityForResult 처리를 담당
    ) { activityResult ->
        // action to do something
        if (activityResult == null) {
            return@registerForActivityResult
        } else {
            val selectedImage = activityResult.data!!.data
            val imageData= ImageData(imageUri = selectedImage.toString())
            viewModel.insertImageData(imageData)
        }
    }
    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        isGranted ->
        if (isGranted) {

        } else {

        }
    }


    override fun initStartView() {
        setStatusBar(this@MainActivity, R.color.white)
        mRecyclerView =  findViewById<RecyclerView>(R.id.recyclerview_main)
        gridLayoutManager = GridLayoutManager(applicationContext, 2)
        switchNoti = findViewById(R.id.switch_noti)
        mRecyclerView.layoutManager = gridLayoutManager
    }

    override fun initDataBinding() {
        viewModel.imageDataList.observe(this, {
            mAdapter = MainAdapter(this@MainActivity, it)
            mAdapter.itemClick = object : MainAdapter.ItemClick {
                override fun onClick(view: View, position: Int) {

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
        requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

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

        val button = findViewById<Button>(R.id.menu_button)
        button.setOnClickListener {
            if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                drawerLayout.closeDrawer(Gravity.RIGHT)
            } else {
                drawerLayout.openDrawer(Gravity.RIGHT)
            }
        }
        val addButton = findViewById<Button>(R.id.btn_add_image)
        addButton.setOnClickListener {
            pickFromGallery()
        }
        switchNoti.setOnCheckedChangeListener { _, isChecked ->
            val imageservice = Intent(this, ImageService::class.java)
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
            } else {
                if (ImageService.isInit) {
                    stopService(imageservice)
                }
            }
        }
    }
}