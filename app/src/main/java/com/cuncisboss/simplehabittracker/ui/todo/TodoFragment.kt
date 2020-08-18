package com.cuncisboss.simplehabittracker.ui.todo

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.cuncisboss.simplehabittracker.R
import com.cuncisboss.simplehabittracker.databinding.DialogAddTaskBinding
import com.cuncisboss.simplehabittracker.databinding.FragmentTodoBinding
import com.cuncisboss.simplehabittracker.model.Task
import com.cuncisboss.simplehabittracker.service.AlarmReceiver
import com.cuncisboss.simplehabittracker.util.Constants.TAG
import com.cuncisboss.simplehabittracker.util.DatePickerFragment
import com.cuncisboss.simplehabittracker.util.Helper.reverseThis
import com.cuncisboss.simplehabittracker.util.Helper.showSnackbarMessage
import com.cuncisboss.simplehabittracker.util.TimePickerFragment
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


class TodoFragment : Fragment(), DatePickerFragment.DialogDateListener, TimePickerFragment.DialogTimeListener {

    companion object {
        val DATE_PICKER_TAG = "DATE_PICKER_TAG"
        val TIME_PICKER_ONCE_TAG = "TIME_PICKER_ONCE_TAG"
        val TIME_PICKER_REPEAT_TAG = "TIME_PICKER_REPEAT_TAG"
    }

    private val viewModel by inject<TodoViewModel>()

    private lateinit var dialogBinding: DialogAddTaskBinding
    private lateinit var binding: FragmentTodoBinding

    private val alarmReceiver by lazy { AlarmReceiver() }

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

        if (savedInstanceState != null) {
            dialogBinding = DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                R.layout.dialog_add_task, null, false
            )
            val timeDialog = childFragmentManager.findFragmentByTag(TIME_PICKER_ONCE_TAG) as TimePickerFragment
            timeDialog.setListener(this)
        }

        val adapter = TodoAdapter()
        rv_task.adapter = adapter

        viewModel.getTasks().observe(viewLifecycleOwner, Observer {
            it.reverseThis()
            adapter.submitList(it)
        })
    }

    private fun dialogInsert() {
        val builder = AlertDialog.Builder(requireContext())
        dialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.dialog_add_task, null, false
        )
        builder.setView(dialogBinding.root)

        val dialog = builder.create()

        dialogBinding.spType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                v: View?,
                position: Int,
                id: Long
            ) {
                dialogBinding.type = position
            }
        }

        dialogBinding.etDate.setOnClickListener {
            val dateFrag = DatePickerFragment()
            dateFrag.setListener(this)
            dateFrag.show(childFragmentManager, DATE_PICKER_TAG)
        }

        dialogBinding.btnSave.setOnClickListener {
//            viewModel.addTask(Task(
//                0,
//                dialogBinding.etTask.text.toString(),
//                dialogBinding.etDate.text.toString(),
//                type,
//                dialogBinding.etReward.text.toString().toLong(),
//                false
//            ))
            requireView().showSnackbarMessage("Task added")
            dialog.dismiss()
        }

        dialogBinding.btnCancel.setOnClickListener {
            dialog.cancel()
        }

        dialog.show()
    }

    private fun dialogAlertTime() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)

        builder.setTitle("Add Time?")
        builder.setPositiveButton("Yes") { dialog, which ->
            val timeFrag = TimePickerFragment()
            timeFrag.setListener(this)
            timeFrag.show(childFragmentManager, TIME_PICKER_ONCE_TAG)
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialogBinding.etDate.setText(strDatetime)
            alarmReceiver.setOneTimeAlarm(requireContext(),
               AlarmReceiver.TYPE_ONE_TIME, strDatetime, getString(R.string.default_time), "Do It Now")
            dialog.cancel()
        }
        builder.create().show()
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

    var strDatetime = ""
    override fun onDialogSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        strDatetime = dateFormat.format(calendar.time)
        dialogAlertTime()
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        when (tag) {
            TIME_PICKER_ONCE_TAG -> {
                dialogBinding.etDate.setText(String.format(getString(R.string.datetime_format), strDatetime, dateFormat.format(calendar.time)))

                alarmReceiver.setOneTimeAlarm(requireContext(),
                    AlarmReceiver.TYPE_ONE_TIME, strDatetime, dateFormat.format(calendar.time), "Do It Now")
            }
            else -> { }
        }
    }
}