package com.example.remedialucp2_235

import android.app.Application
import com.example.remedialucp2_235.data.container.AppContainer
import com.example.remedialucp2_235.data.container.DefaultAppContainer

class LibraryApp : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}