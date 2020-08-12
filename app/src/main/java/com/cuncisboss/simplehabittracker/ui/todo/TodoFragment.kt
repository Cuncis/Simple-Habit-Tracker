package com.cuncisboss.simplehabittracker.ui.todo

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.cuncisboss.simplehabittracker.R
import com.cuncisboss.simplehabittracker.databinding.DialogAddTaskBinding
import com.cuncisboss.simplehabittracker.databinding.FragmentTodoBinding
import com.cuncisboss.simplehabittracker.model.Task
import com.cuncisboss.simplehabittracker.util.Constants.TAG
import com.cuncisboss.simplehabittracker.util.VisibleHelper.hideView
import com.cuncisboss.simplehabittracker.util.VisibleHelper.showView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.niwattep.materialslidedatepicker.SlideDatePickerDialog
import com.niwattep.materialslidedatepicker.SlideDatePickerDialogCallback
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_todo.*
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*


class TodoFragment : Fragment(), SlideDatePickerDialogCallback {

    private val viewModel by inject<TodoViewModel>()

    private lateinit var dialogBinding: DialogAddTaskBinding
    private lateinit var binding: FragmentTodoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_todo, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TodoAdapter()
        rv_task.adapter = adapter

        viewModel.getTasks().observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    private fun dialogInsert() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        dialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.dialog_add_task, null, false
        )
        builder.setView(dialogBinding.root)

        val dialog = builder.create()
        var type: Int = 0

        dialogBinding.spType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                v: View?,
                position: Int,
                id: Long
            ) {
                type = position
                dialogBinding.type = position
            }
        }

        dialogBinding.etDate.setOnClickListener {
            val cal = Calendar.getInstance()
            cal.set(Calendar.DAY_OF_MONTH, 31)
            cal.set(Calendar.MONTH, 11)
            cal.set(Calendar.YEAR, 2030)
            SlideDatePickerDialog.newInstance(endDate = cal, themeColor = ContextCompat.getColor(requireContext(), R.color.md_blue_700))
                .show(childFragmentManager, "TAG")
        }

        dialogBinding.btnSave.setOnClickListener {
            viewModel.addTask(Task(
                0,
                dialogBinding.etTask.text.toString(),
                dialogBinding.etDate.text.toString(),
                type,
                dialogBinding.etReward.text.toString().toLong(),
                false
            ))
            dialog.dismiss()
        }

        dialogBinding.btnCancel.setOnClickListener {
            dialog.cancel()
        }

        dialog.show()
    }

    override fun onPositiveClick(day: Int, month: Int, year: Int, calendar: Calendar) {
        dialogBinding.etDate.setText(SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(calendar.time))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_add) {
            dialogInsert()
        }
        return super.onOptionsItemSelected(item)
    }
}