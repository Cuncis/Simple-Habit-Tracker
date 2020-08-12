package com.cuncisboss.simplehabittracker.di

import android.app.Application
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.cuncisboss.simplehabittracker.R
import com.cuncisboss.simplehabittracker.db.HabitTrackerDatabase
import com.cuncisboss.simplehabittracker.ui.todo.TodoViewModel
import com.cuncisboss.simplehabittracker.util.Constants.DATABASE_NAME
import com.niwattep.materialslidedatepicker.SlideDatePickerDialog
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module



val localModule = module {
    single { provideDatabase(androidApplication()) }
    single { provideTaskDao(get()) }
}

val viewModelModule = module {
    viewModel { TodoViewModel(get()) }
}


private fun provideTaskDao(db: HabitTrackerDatabase) = db.taskDao()

private fun provideDatabase(app: Application)
        = Room.databaseBuilder(app, HabitTrackerDatabase::class.java, DATABASE_NAME).build()