package com.cuncisboss.simplehabittracker.ui.todo.yesterday

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.cuncisboss.simplehabittracker.R
import com.cuncisboss.simplehabittracker.databinding.FragmentYesterdayBinding
import com.cuncisboss.simplehabittracker.model.Task
import com.cuncisboss.simplehabittracker.ui.todo.TodoAdapter
import com.cuncisboss.simplehabittracker.ui.todo.TodoViewModel
import com.cuncisboss.simplehabittracker.util.Constants
import com.cuncisboss.simplehabittracker.util.Constants.KEY_TOTAL
import com.cuncisboss.simplehabittracker.util.Constants.TAG
import com.cuncisboss.simplehabittracker.util.Constants.TASK_TYPE_TODAY
import com.cuncisboss.simplehabittracker.util.Constants.TASK_TYPE_YESTERDAY
import com.cuncisboss.simplehabittracker.util.Helper
import com.cuncisboss.simplehabittracker.util.Helper.reverseThis
import com.cuncisboss.simplehabittracker.util.Helper.showSnackbarMessage
import com.cuncisboss.simplehabittracker.util.VisibleHelper.hideView
import kotlinx.android.synthetic.main.dialog_alert_actions.view.*
import kotlinx.android.synthetic.main.item_task.view.*
import org.koin.android.ext.android.inject

class YesterdayFragment : Fragment() {

    private val viewModel by inject<TodoViewModel>()
    private val pref by inject<SharedPreferences>()

    private lateinit var binding: FragmentYesterdayBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_yesterday, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(Constants.TAG, "dialogInsert: ${Helper.formatToYesterdayOrTodayOrTomorrow(Helper.getCurrentDatetime(-1))}")
        Log.d(Constants.TAG, "dialogInsert: ${Helper.getCurrentDatetime(-1)}")

        val adapter = TodoAdapter()
        binding.rvYesterday.adapter = adapter

        viewModel.getTasks(TASK_TYPE_YESTERDAY).observe(viewLifecycleOwner, Observer {
            it.reverseThis()
            adapter.submitList(it)
        })

        adapter.setChecklistListener { v, task ->
            if (v.id == R.id.btn_checklist) {
                dialogAlert(task, true)
            } else {
                dialogAlert(task, false)
            }
        }
    }

    private fun dialogAlert(task: Task?, isChecked: Boolean) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(true)
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_alert_actions, null as ViewGroup?)
        builder.setView(view)

        val dialog = builder.create()

        view.tvTitleDialog.text = task?.name

        if (isChecked) {
            view.btn_delete_task.hideView()
            view.btn_skip_task.hideView()
        } else {
            view.btn_done_task.hideView()
        }

        view.btn_delete_task.setOnClickListener {
            if (task != null) {
                viewModel.removeTask(task)
                dialog.dismiss()
                requireView().showSnackbarMessage("Task deleted")
            }
        }

        view.btn_skip_task.setOnClickListener {
            if (task != null) {
                task.type = TASK_TYPE_TODAY
                task.date = Helper.formatToYesterdayOrTodayOrTomorrow(Helper.getCurrentDatetime(0))
                viewModel.updateTask(task)
                dialog.dismiss()
                requireView().showSnackbarMessage("Task skipped")
            }
        }

        view.btn_done_task.setOnClickListener {
            if (task != null) {
                task.type = TASK_TYPE_TODAY
                task.date = Helper.formatToYesterdayOrTodayOrTomorrow(Helper.getCurrentDatetime(0))
                viewModel.updateTask(task)
                if (pref.getLong(KEY_TOTAL, 0L) == 0L) {
                    pref.edit().putLong(KEY_TOTAL, task.value).apply()
                } else {
                    val total = task.value + pref.getLong(KEY_TOTAL, 0L)
                    pref.edit().putLong(KEY_TOTAL, total).apply()
                }
                Log.d(TAG, "dialogAlert: ${pref.getLong(KEY_TOTAL, 0L)}")
                dialog.dismiss()
                requireView().showSnackbarMessage("Congrats your task is done")
            }
        }

        dialog.show()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if (menu.findItem(R.id.action_add) != null) {
            menu.getItem(0).isVisible = false
        }
    }
}