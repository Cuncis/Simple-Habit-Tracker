package com.cuncisboss.simplehabittracker.util

import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import com.cuncisboss.simplehabittracker.R
import com.cuncisboss.simplehabittracker.model.Task
import com.cuncisboss.simplehabittracker.util.Constants.BASE_EXP
import com.cuncisboss.simplehabittracker.util.Constants.EXPONENT
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor
import kotlin.math.pow

object Helper {

    fun List<Task>.reverseThis() {
        (this as ArrayList).reverse()
    }

    fun View.showSnackbarMessage(message: String) {
        Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
    }

    fun getCurrentDatetime(incOrDec: Int): String {
        val sdf = SimpleDateFormat("dd/M/yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DATE, incOrDec)
        return sdf.format(calendar.time)
    }

    fun formatToYesterdayOrTodayOrTomorrow(date: String): String {
        val datetime = SimpleDateFormat("dd/M/yyyy", Locale.getDefault()).parse(date)
        val calendar = Calendar.getInstance()
        calendar.time = datetime as Date
        val today = Calendar.getInstance()
        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DATE, -1)

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

    fun checkIsToday(date: String): Int {
        val datetime = SimpleDateFormat("dd/M/yyyy", Locale.getDefault()).parse(date)
        val calendar = Calendar.getInstance()
        calendar.time = datetime as Date
        val today = Calendar.getInstance()
        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DATE, -1)

        return if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)
            && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) { // today
            1
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR)          // yesterday
            && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            0
        } else {                                                                        // tomorrow
            2
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

    fun calXpForLevel(level: Int): Double {
        return BASE_EXP + (BASE_EXP * level.toDouble().pow(EXPONENT))
    }

    fun calculateFullTargetExp(level: Int): Long {
        var requiredExp = 0.0
        for (i in 0..level) {
            requiredExp += calXpForLevel(i)
        }
        return requiredExp.toLong()
    }

    fun calculateFullTargetExpAfter(level: Int): Long {
        var requiredExp = 0.0
        for (i in 0..level+1) {
            requiredExp += calXpForLevel(i)
        }
        return requiredExp.toLong()
    }

    fun calculateLevel(exp: Double): Int {
        var level = 0
        var maxExp = calXpForLevel(0)
        do {
            maxExp += calXpForLevel(++level)
        } while (maxExp < exp)
        return level
    }

    fun experience(level: Int): Double {
        var a = 0.0
        for (i in 0..level) {
            a += floor(i + 300 * 2.0.pow((i / 7).toDouble()))
        }
        return floor(a/4)
    }

}