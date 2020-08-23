package com.cuncisboss.simplehabittracker.ui.todo.today

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.cuncisboss.simplehabittracker.R
import com.cuncisboss.simplehabittracker.databinding.DialogAddTaskBinding
import com.cuncisboss.simplehabittracker.databinding.FragmentTodayBinding
import com.cuncisboss.simplehabittracker.model.Task
import com.cuncisboss.simplehabittracker.ui.todo.TodoAdapter
import com.cuncisboss.simplehabittracker.ui.todo.TodoDialog
import com.cuncisboss.simplehabittracker.ui.todo.TodoViewModel
import com.cuncisboss.simplehabittracker.util.Constants
import com.cuncisboss.simplehabittracker.util.Constants.TAG
import com.cuncisboss.simplehabittracker.util.Constants.TAG_INSERT
import com.cuncisboss.simplehabittracker.util.Constants.TASK_TYPE_TODAY
import com.cuncisboss.simplehabittracker.util.Constants.TASK_TYPE_TOMORROW
import com.cuncisboss.simplehabittracker.util.Helper
import com.cuncisboss.simplehabittracker.util.Helper.reverseThis
import com.cuncisboss.simplehabittracker.util.Helper.showSnackbarMessage
import com.cuncisboss.simplehabittracker.util.VisibleHelper.hideView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_alert_actions.view.*
import org.koin.android.ext.android.inject


class TodayFragment : Fragment() {

    private val viewModel by inject<TodoViewModel>()
    private val pref by inject<SharedPreferences>()

    private lateinit var binding: FragmentTodayBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_today, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "today: ${Helper.formatToYesterdayOrTodayOrTomorrow(Helper.getCurrentDatetime(0))}")
        Log.d(TAG, "today: ${Helper.getCurrentDatetime(0)}")

        if (savedInstanceState != null) {
            val todoDialog = childFragmentManager
                .findFragmentByTag(TAG_INSERT) as TodoDialog?
            todoDialog?.setSaveListener { taskName, reward ->
                viewModel.addTask(
                    Task(
                        0,
                        taskName,
                        Helper.formatToYesterdayOrTodayOrTomorrow(Helper.getCurrentDatetime(0)),
                        TASK_TYPE_TODAY,      // 1. repeating, 2. once, 3. certain -> 1. Yesterday 2. Today 3. Tomorrow
                        reward
                    )
                )
                (requireParentFragment().view as View).showSnackbarMessage("Task added")
            }
        }

        val adapter = TodoAdapter()
        binding.rvToday.adapter = adapter

        viewModel.getTasks(TASK_TYPE_TODAY).observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "onViewCreated: $it")
            it.reverseThis()
            adapter.submitList(it)
        })

        adapter.setChecklistListener { v, task ->
            if (v.id == R.id.btn_checklist) {
                dialogAlert(task, true)
            } else {
                dialogAlert(task,false)
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
                task.type = TASK_TYPE_TOMORROW
                task.date = Helper.formatToYesterdayOrTodayOrTomorrow(Helper.getCurrentDatetime(1))
                viewModel.updateTask(task)
                dialog.dismiss()
                requireView().showSnackbarMessage("Task skipped")
            }
        }

        view.btn_done_task.setOnClickListener {
            if (task != null) {
                task.type = TASK_TYPE_TOMORROW
                task.date = Helper.formatToYesterdayOrTodayOrTomorrow(Helper.getCurrentDatetime(1))
                viewModel.updateTask(task)
                if (pref.getLong(Constants.KEY_TOTAL, 0L) == 0L) {
                    pref.edit().putLong(Constants.KEY_TOTAL, task.value).apply()
                } else {
                    val total = task.value + pref.getLong(Constants.KEY_TOTAL, 0L)
                    pref.edit().putLong(Constants.KEY_TOTAL, total).apply()
                }
                Log.d(TAG, "dialogAlert: ${pref.getLong(Constants.KEY_TOTAL, 0L)}")
                dialog.dismiss()
                requireView().showSnackbarMessage("Congrats your task is done")
            }
        }

        dialog.show()
    }

    private fun showInsertDialog() {
        TodoDialog().apply {
            setSaveListener { taskName, reward ->
                viewModel.addTask(
                    Task(
                        0,
                        taskName,
                        Helper.formatToYesterdayOrTodayOrTomorrow(Helper.getCurrentDatetime(0)),
                        TASK_TYPE_TODAY,      // 1. repeating, 2. once, 3. certain -> 1. Yesterday 2. Today 3. Tomorrow
                        reward
                    )
                )
                (requireParentFragment().view as View).showSnackbarMessage("Task added")
            }
        }.show(childFragmentManager, TAG_INSERT)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.getItem(0).isVisible = menu.findItem(R.id.action_add) != null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_add) {
            showInsertDialog()
//            viewModel.removeAllTask()
        }
        return super.onOptionsItemSelected(item)
    }

}