package com.cuncisboss.simplehabittracker.util

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.cuncisboss.simplehabittracker.R
import kotlinx.android.synthetic.main.dialog_new_user.view.*

class AddUserDialogHelper : DialogFragment() {

    private var saveListener: ((String?) -> Unit)? = null

    fun setSaveListener(saveListener: (String?) -> Unit) {
        this.saveListener = saveListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(true)
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_new_user, null as ViewGroup?)
        builder.setView(view)

        val dialog = builder.create()

        view.btnSave.setOnClickListener {
            saveListener?.let { save ->
                save(view.et_username.text.toString())
                dialog.dismiss()
            }
        }

        return dialog
    }

}