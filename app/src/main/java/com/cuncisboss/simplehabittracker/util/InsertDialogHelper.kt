package com.cuncisboss.simplehabittracker.util

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.cuncisboss.simplehabittracker.R
import com.cuncisboss.simplehabittracker.util.Helper.disableBackgroundTint
import com.cuncisboss.simplehabittracker.util.VisibleHelper.hideView
import kotlinx.android.synthetic.main.dialog_alert_actions.view.*

class InsertDialogHelper : DialogFragment() {

    private var claimedListener: (() -> Unit)? = null
    private var claimed: Boolean = false
    private var title: String? = null

    fun editTitleDialog(title: String?) {
        this.title = title
    }

    fun setClaimedListener(claimed: Boolean, claimedListener: () -> Unit) {
        this.claimedListener = claimedListener
        this.claimed = claimed
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(true)
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_alert_actions, null as ViewGroup?)
        builder.setView(view)

        val dialog = builder.create()

        if (claimed) {
            view.btn_skip_task.hideView()
            view.btn_delete_task.hideView()
            view.btn_done_task.disableBackgroundTint()
        } else {
            view.btn_delete_task.hideView()
            view.btn_skip_task.hideView()
        }
        view.tvTitleDialog.text = title.toString()

        view.btn_done_task.text = getString(R.string.claim)
        view.btn_done_task.setOnClickListener {
            claimedListener?.let { claim ->
                claim()
                dialog.dismiss()
            }
        }

        return dialog
    }

}