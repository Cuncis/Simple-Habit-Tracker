package com.cuncisboss.simplehabittracker.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.cuncisboss.simplehabittracker.db.HabitTrackerDatabase
import com.cuncisboss.simplehabittracker.ui.todo.TodoViewModel
import com.cuncisboss.simplehabittracker.util.Constants.DATABASE_NAME
import com.cuncisboss.simplehabittracker.util.Constants.PREF_NAME
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val localModule = module {
    single { provideDatabase(androidApplication()) }
    single { provideTaskDao(get()) }
    single { providePreference(androidApplication()) }
}

val viewModelModule = module {
    viewModel { TodoViewModel(get()) }
}



private fun provideTaskDao(db: HabitTrackerDatabase) = db.taskDao()

private fun provideDatabase(app: Application) =
    Room.databaseBuilder(app, HabitTrackerDatabase::class.java, DATABASE_NAME).build()

private fun providePreference(app: Application) =
    app.getSharedPreferences(PREF_NAME, MODE_PRIVATE)

