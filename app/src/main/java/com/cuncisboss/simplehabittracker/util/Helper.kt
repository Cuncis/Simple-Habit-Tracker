package com.cuncisboss.simplehabittracker.util

import android.content.Context
import android.view.View
import com.cuncisboss.simplehabittracker.model.Task
import com.google.android.material.snackbar.Snackbar

object Helper {

    fun List<Task>.reverseThis() {
        (this as ArrayList).reverse()
    }

    fun View.showSnackbarMessage(message: String) {
        Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
    }

}