package com.junho.imageapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.junho.imageapp.R
import com.junho.imageapp.databinding.ActivityMainBinding
import com.junho.imageapp.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {


    override val layoutResourceId: Int
        get() = R.layout.activity_main

    override val viewModel: MainViewModel by viewModel()

    override fun initStartView() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor =
                ContextCompat.getColor(this@MainActivity, R.color.white)
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {
        setNavigationDrawer()
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