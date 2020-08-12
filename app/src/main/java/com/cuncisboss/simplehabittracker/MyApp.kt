package com.cuncisboss.simplehabittracker

import android.app.Application
import com.cuncisboss.simplehabittracker.di.localModule
import com.cuncisboss.simplehabittracker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MyApp)
            modules(
                listOf(
                    localModule,
                    viewModelModule
                )
            )
        }
    }

}