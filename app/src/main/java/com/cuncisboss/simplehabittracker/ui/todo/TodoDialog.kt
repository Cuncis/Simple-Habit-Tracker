package com.cuncisboss.simplehabittracker.ui.todo

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.cuncisboss.simplehabittracker.R
import com.cuncisboss.simplehabittracker.databinding.DialogAddTaskBinding
import org.koin.android.ext.android.bind

class TodoDialog : DialogFragment() {

    private var saveListener: ((String, Long, Long) -> Unit)? = null
    private var title: String? = null
    private var update: String? = null
    private var name: String? = null
    private var reward: String? = null

    fun setSaveListener(saveListener: (String, Long, Long) -> Unit) {
        this.saveListener = saveListener
    }

    fun editTitleDialog(title: String?) {
        this.title = title
    }

    fun setButtonUpdate(update: String) {
        this.update = update
    }

    fun setInitField(name: String?, reward: String?) {
        this.name = name
        this.reward = reward
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.dialog_add_task, null, false
        ) as DialogAddTaskBinding

        builder.setView(binding.root)

        val dialog = builder.create()

        title?.let { binding.textTitle.text = it }
        update?.let { binding.btnSave.text = it }

        name?.let { binding.etTask.setText(it) }
        reward?.let { binding.etReward.setText(it) }

        var type = ""
        binding.spType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) { }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                type = binding.spType.selectedItem.toString()
            }

        }

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
                            binding.etReward.text.toString().toLong(),
                            type.toLong()
                        )
                        dialog.dismiss()
                    }
                }
            }
        }

        return dialog
    }
}