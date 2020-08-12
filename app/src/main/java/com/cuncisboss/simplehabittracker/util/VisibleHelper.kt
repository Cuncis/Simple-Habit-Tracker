package com.cuncisboss.simplehabittracker.util

import android.view.View

object VisibleHelper {

    fun View.showView() {
        this.visibility = View.VISIBLE
    }

    fun View.hideView() {
        this.visibility = View.GONE
    }
}