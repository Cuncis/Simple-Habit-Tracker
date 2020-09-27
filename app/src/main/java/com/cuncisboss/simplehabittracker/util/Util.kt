package com.cuncisboss.simplehabittracker.util

import java.text.SimpleDateFormat
import java.util.*


fun getCurrentWeek(): List<String?> {
    val calendar = Calendar.getInstance()

    val format = SimpleDateFormat("E", Locale.getDefault())

    val days = arrayOfNulls<String>(7)
    val delta = -calendar.get(GregorianCalendar.DAY_OF_WEEK) + 2
    calendar.add(Calendar.DAY_OF_MONTH, delta)
    for (i in 0..6) {
        days[i] = format.format(calendar.time)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }

    return days.asList()
}