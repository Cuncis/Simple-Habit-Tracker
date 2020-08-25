package com.cuncisboss.simplehabittracker.ui.todo

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.cuncisboss.simplehabittracker.R
import com.cuncisboss.simplehabittracker.databinding.DialogAddTaskBinding

class TodoDialog : DialogFragment() {

    private var saveListener: ((String, Long) -> Unit)? = null

    fun setSaveListener(saveListener: (String, Long) -> Unit) {
        this.saveListener = saveListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.dialog_add_task, null, false
        ) as DialogAddTaskBinding

        builder.setView(binding.root)

        val dialog = builder.create()

        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        binding.btnSave.setOnClickListener {
            saveListener?.let { save ->
                when {
                    binding.etTask.text.isEmpty() -> {
                        binding.etTask.error = "Task should not be empty"
                    }
                    binding.etReward.text.isEmpty() -> {
                        binding.etReward.error = "Reward should not be empty"
                    }
                    else -> {
                        save(
                            binding.etTask.text.toString(),
                            binding.etReward.text.toString().toLong()
                        )
                        dialog.dismiss()
                    }
                }
            }
        }

        return dialog
    }
}