package com.cuncisboss.simplehabittracker.util

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.cuncisboss.simplehabittracker.R
import com.cuncisboss.simplehabittracker.util.VisibleHelper.hideView
import kotlinx.android.synthetic.main.dialog_alert_actions.view.*

class OptionDialogHelper : DialogFragment() {

    private var deleteListener: (() -> Unit)? = null
    private var editListener: (() -> Unit)? = null
    private var title: String? = null

    fun editTitleDialog(title: String?) {
        this.title = title
    }

    fun setDeleteListener(deleteListener: () -> Unit) {
        this.deleteListener = deleteListener
    }

    fun setEditListener(editListener: () -> Unit) {
        this.editListener = editListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(true)
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_alert_actions, null as ViewGroup?)
        builder.setView(view)

        val dialog = builder.create()

        view.tvTitleDialog.text = title

        view.btn_done_task.hideView()

        view.btn_delete_task.setOnClickListener {
            deleteListener?.let { delete ->
                delete()
                dialog.dismiss()
            }
        }

        // Edit
        view.btn_skip_task.text = getString(R.string.edit)
        view.btn_skip_task.setOnClickListener {
            editListener?.let {  edit ->
                edit()
                dialog.dismiss()
            }
        }

        return dialog
    }

}