package com.sample.myfirebaseapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.sample.myfirebaseapplication.callbacks.FcmBroadcastListener
import com.sample.myfirebaseapplication.databinding.ActivityMainBinding
import com.sample.myfirebaseapplication.fragments.FirstFragment
import com.sample.myfirebaseapplication.`object`.JioTokenSDK


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var action: String? = ""
    private var fragmentCallback: FcmBroadcastListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handelIntent()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        fragmentCallback = this as FcmBroadcastListener
        findNavController(R.id.nav_host_fragment_content_main)

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handelIntent()
    }

    fun handelIntent(){
        if (getIntent() != null) {
            action = intent?.action
            when (action) {
                "API" -> {
//                    JioTokenSDK.tokenListener?.let { it.callAcceptApi() }
                    fragmentCallback?.callAcceptApi()
                }
            }
        }
    }

    fun setCallback(fragmentCallback: FcmBroadcastListener?) {
        this.fragmentCallback = fragmentCallback
    }


}