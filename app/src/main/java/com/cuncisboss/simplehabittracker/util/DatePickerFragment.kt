package com.cuncisboss.simplehabittracker.util

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment: DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var mListener: DialogDateListener? = null

    override fun onDetach() {
        super.onDetach()
        if (mListener != null) {
            mListener = null
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        mListener?.onDialogSet(tag, year, month, dayOfMonth)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DATE)
        return DatePickerDialog(activity as Context, this, year, month, date)
    }

    fun setListener(listener: DialogDateListener) {
        mListener = listener
    }

    interface DialogDateListener {
        fun onDialogSet(tag: String?, year: Int, month: Int, dayOfMonth: Int)
    }
}







