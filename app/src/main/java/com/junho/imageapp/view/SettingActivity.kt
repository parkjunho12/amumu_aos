package com.junho.imageapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.junho.imageapp.R
import com.junho.imageapp.databinding.ActivityInfoBinding
import com.junho.imageapp.viewmodel.InfoViewModel
import com.junho.imageapp.viewmodel.SettingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingActivity :BaseActivity<ActivityInfoBinding, SettingViewModel>()  {

    override val viewModel: SettingViewModel by viewModel()

    override val layoutResourceId: Int
        get() = R.layout.activity_setting


    override fun initStartView() {

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

    }
}