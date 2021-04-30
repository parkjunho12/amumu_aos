package com.junho.imageapp.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import coil.Coil
import coil.request.LoadRequest
import com.junho.imageapp.R
import com.junho.imageapp.database.format.ImageData
import com.junho.imageapp.databinding.ActivityInfoBinding
import com.junho.imageapp.databinding.ActivityMainBinding
import com.junho.imageapp.viewmodel.InfoViewModel
import com.junho.imageapp.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class InfoActivity :BaseActivity<ActivityInfoBinding, InfoViewModel>() {

    override val viewModel: InfoViewModel by viewModel()

    lateinit var imageView: ImageView
    var iamgeUri: ImageData? = null
    lateinit var imageBtn: ImageButton
    lateinit var deleteBtn: Button


    override val layoutResourceId: Int
        get() = R.layout.activity_info


    override fun initStartView() {
        iamgeUri = intent.getSerializableExtra("imageData") as ImageData?
        imageView = findViewById<ImageView>(R.id.img_detail)
        imageBtn = findViewById<ImageButton>(R.id.back_button_detail)
        deleteBtn = findViewById(R.id.btn_delete_info)
    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {
        if (iamgeUri != null) {
            val imageLoader = Coil.imageLoader(this)
            val request = LoadRequest.Companion.Builder(this)
                .data(Uri.parse(iamgeUri!!.imageUri))
                .target(imageView)
                .build()
            imageLoader.execute(request)
        }
        imageBtn.setOnClickListener {
            onBackPressed()
        }
        deleteBtn.setOnClickListener {
            if (iamgeUri != null) {
                viewModel.deleteItem(iamgeUri!!)
                onBackPressed()
            } else {
                Toast.makeText(this, getText(R.string.cant_delete), Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
        }
    }
}