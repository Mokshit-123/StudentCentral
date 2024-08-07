package com.example.notices

import android.app.Application
import com.example.notices.data.AppContainer
import com.example.notices.data.DefaultAppContainer

class MyApp: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}