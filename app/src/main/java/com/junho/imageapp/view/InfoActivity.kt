package com.junho.imageapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.junho.imageapp.R
import com.junho.imageapp.databinding.ActivityInfoBinding
import com.junho.imageapp.databinding.ActivityMainBinding
import com.junho.imageapp.viewmodel.InfoViewModel
import com.junho.imageapp.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class InfoActivity :BaseActivity<ActivityInfoBinding, InfoViewModel>() {

    override val viewModel: InfoViewModel by viewModel()

    override val layoutResourceId: Int
        get() = R.layout.activity_info


    override fun initStartView() {

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

    }
}