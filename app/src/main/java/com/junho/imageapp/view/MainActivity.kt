package com.junho.imageapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.junho.imageapp.R
import com.junho.imageapp.database.format.ImageData
import com.junho.imageapp.databinding.ActivityMainBinding
import com.junho.imageapp.view.adapter.MainAdapter
import com.junho.imageapp.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {


    override val layoutResourceId: Int
        get() = R.layout.activity_main

    override val viewModel: MainViewModel by viewModel()
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var mAdapter: MainAdapter

    override fun initStartView() {
        setStatusBar(this@MainActivity, R.color.white)
        mRecyclerView =  findViewById<RecyclerView>(R.id.recyclerview_main)
        gridLayoutManager = GridLayoutManager(applicationContext, 2)
        mRecyclerView.layoutManager = gridLayoutManager
    }

    override fun initDataBinding() {
        viewModel.imageDataList.observe(this, {
            mAdapter = MainAdapter(this@MainActivity, it)
            mRecyclerView.adapter = mAdapter
            mRecyclerView.adapter!!.notifyDataSetChanged()
        })
    }

    override fun initAfterBinding() {
        setNavigationDrawer()
        viewModel.fetchDataList()
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
    }
}