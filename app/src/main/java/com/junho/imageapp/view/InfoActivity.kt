package com.junho.imageapp.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ScrollView
import coil.Coil
import coil.request.LoadRequest
import com.junho.imageapp.R
import com.junho.imageapp.databinding.ActivityInfoBinding
import com.junho.imageapp.databinding.ActivityMainBinding
import com.junho.imageapp.viewmodel.InfoViewModel
import com.junho.imageapp.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class InfoActivity :BaseActivity<ActivityInfoBinding, InfoViewModel>() {

    override val viewModel: InfoViewModel by viewModel()

    lateinit var imageView: ImageView
    lateinit var iamgeUri: String

    override val layoutResourceId: Int
        get() = R.layout.activity_info


    override fun initStartView() {
        iamgeUri = intent.getStringExtra("imageData").toString()
        imageView = findViewById<ImageView>(R.id.img_detail)
    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

        val imageLoader = Coil.imageLoader(this)
        val request = LoadRequest.Companion.Builder(this)
            .data(Uri.parse(iamgeUri))
            .target(imageView)
            .build()
        imageLoader.execute(request)
    }
}