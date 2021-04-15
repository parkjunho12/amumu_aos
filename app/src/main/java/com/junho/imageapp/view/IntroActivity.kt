package com.junho.imageapp.view

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.junho.imageapp.R
import com.junho.imageapp.database.format.ImageData
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class IntroActivity : AppCompatActivity() {

    private val requestActivity: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult() // ◀ StartActivityForResult 처리를 담당
    ) { activityResult ->
        // action to do something
        if (activityResult == null) {
            Toast.makeText(this, getText(R.string.perm_deny_ment), Toast.LENGTH_LONG).show()
            finishAffinity()
            return@registerForActivityResult
        } else {
            startActivity()
        }
    }
    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            isGranted ->
        if (isGranted) {
            startActivity()
        } else {
            Toast.makeText(this, getText(R.string.perm_deny_ment), Toast.LENGTH_LONG).show()
            finishAffinity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun startActivity() {
        Handler(Looper.getMainLooper()).postDelayed(
            Runnable {
                val introToMainActivity = Intent(this@IntroActivity, MainActivity::class.java)
                introToMainActivity.apply {

                }
                startActivity(introToMainActivity)
            }, 2000
        )
    }
}