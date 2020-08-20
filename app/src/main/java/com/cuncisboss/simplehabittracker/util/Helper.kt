package com.cuncisboss.simplehabittracker.util

import android.content.res.ColorStateList
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import com.cuncisboss.simplehabittracker.R
import com.cuncisboss.simplehabittracker.model.Task
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object Helper {

    fun List<Task>.reverseThis() {
        (this as ArrayList).reverse()
    }

    fun View.showSnackbarMessage(message: String) {
        Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
    }

    fun getCurrentDatetime(): String {
        return SimpleDateFormat("dd/M/yyyy hh:mm", Locale.getDefault()).format(Date())
    }

    fun formatToYesterdayOrTodayOrTomorrow(date: String): String {
        /*
        Date dateTime = new SimpleDateFormat("EEE hh:mma MMM d, yyyy").parse(date);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(dateTime);
    Calendar today = Calendar.getInstance();
    Calendar yesterday = Calendar.getInstance();
    yesterday.add(Calendar.DATE, -1);
    DateFormat timeFormatter = new SimpleDateFormat("hh:mma");
         */
        val datetime = SimpleDateFormat("dd/M/yyyy hh:mm", Locale.getDefault()).parse(date)
        val calendar = Calendar.getInstance()
        calendar.time = datetime as Date
        val today = Calendar.getInstance()
        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DATE, -1)
        val timeFormatter = SimpleDateFormat("hh:mma", Locale.getDefault())

        return if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)
            && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            "Today"
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR)
            && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            "Yesterday"
        } else {
            "Tomorrow"
        }
    }

    fun View.disableBackgroundTint() {
        this.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                this.context,
                R.color.colorScrim
            )
        )
        this.isEnabled = false
    }

}