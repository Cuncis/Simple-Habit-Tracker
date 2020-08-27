package com.cuncisboss.simplehabittracker.util

import android.os.Build

object Constants {
    const val TAG = "_logHabit"

    const val DATABASE_NAME = "habit_tracker_db"

    const val PREF_NAME = "com.cuncisboss.simplehabittracker.PREF"
    const val KEY_TOTAL = "KEY_TOTAL"
    const val KEY_CURRENT_DATE = "KEY_CURRENT_DATE"

    const val TASK_TYPE_YESTERDAY = 1
    const val TASK_TYPE_TODAY = 2
    const val TASK_TYPE_TOMORROW = 3

    const val KEY_AVAILABLE = "Available"
    const val KEY_CLAIMED = "Claimed"

    const val NOTIFICATION_CHANNEL_ID = "task_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Task"
    const val NOTIFICATION_ID = 1   // notificationId cannot put 0 value

    const val ACTION_SERVICE = "ACTION_SERVICE"

    const val ACTION_SHOW_TODO_FRAGMENT = "ACTION_SHOW_TODO_FRAGMENT"

    const val TAG_INSERT = "TAG_INSERT"
    const val TAG_CLAIM = "TAG_CLAIM"

    const val CURRENT_POSITION_KEY = "CURRENT_POSITION_KEY"
}