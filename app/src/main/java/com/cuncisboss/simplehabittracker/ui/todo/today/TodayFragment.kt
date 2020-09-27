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
import com.cuncisboss.simplehabittracker.databinding.FragmentTodayBinding
import com.cuncisboss.simplehabittracker.model.Task
import com.cuncisboss.simplehabittracker.model.User
import com.cuncisboss.simplehabittracker.ui.dashboard.DashboardViewModel
import com.cuncisboss.simplehabittracker.ui.todo.TodoAdapter
import com.cuncisboss.simplehabittracker.ui.todo.TodoDialog
import com.cuncisboss.simplehabittracker.ui.todo.TodoViewModel
import com.cuncisboss.simplehabittracker.util.Constants.KEY_EXP
import com.cuncisboss.simplehabittracker.util.Constants.KEY_QTY
import com.cuncisboss.simplehabittracker.util.Constants.KEY_TOTAL
import com.cuncisboss.simplehabittracker.util.Constants.KEY_USERNAME
import com.cuncisboss.simplehabittracker.util.Constants.KEY_USER_EXIST
import com.cuncisboss.simplehabittracker.util.Constants.TAG
import com.cuncisboss.simplehabittracker.util.Constants.TAG_INSERT
import com.cuncisboss.simplehabittracker.util.Constants.TASK_TYPE_TODAY
import com.cuncisboss.simplehabittracker.util.Constants.TASK_TYPE_TOMORROW
import com.cuncisboss.simplehabittracker.util.Helper
import com.cuncisboss.simplehabittracker.util.Helper.reverseThis
import com.cuncisboss.simplehabittracker.util.Helper.showSnackbarMessage
import com.cuncisboss.simplehabittracker.util.VisibleHelper.hideView
import kotlinx.android.synthetic.main.dialog_alert_actions.view.*
import org.koin.android.ext.android.inject


class TodayFragment : Fragment() {

    private val todoViewModel by inject<TodoViewModel>()
    private val dashboardViewModel by inject<DashboardViewModel>()
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

        savedInstanceState?.let {
            val todoDialog = childFragmentManager
                .findFragmentByTag(TAG_INSERT) as TodoDialog?
            todoDialog?.setSaveListener { taskName, reward, exp ->
                todoViewModel.addTask(
                    Task(
                        0,
                        taskName,
                        Helper.formatToYesterdayOrTodayOrTomorrow(Helper.getCurrentDatetime(0)),
                        TASK_TYPE_TODAY,      // 1. repeating, 2. once, 3. certain -> 1. Yesterday 2. Today 3. Tomorrow
                        reward,
                        exp
                    )
                )
                (requireParentFragment().view as View).showSnackbarMessage("Task added")
            }
        }

        val adapter = TodoAdapter()
        binding.rvToday.adapter = adapter

        todoViewModel.getTasks(TASK_TYPE_TODAY).observe(viewLifecycleOwner, {
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
            task?.let {
                todoViewModel.removeTask(task)
                dialog.dismiss()
                requireView().showSnackbarMessage("Task deleted")
            }
        }

        view.btn_skip_task.setOnClickListener { _ ->
            task?.let {
                it.type = TASK_TYPE_TOMORROW
                it.date = Helper.formatToYesterdayOrTodayOrTomorrow(Helper.getCurrentDatetime(1))
                todoViewModel.updateTask(it)
                dialog.dismiss()
                requireView().showSnackbarMessage("Task skipped")
            }
        }

        view.btn_done_task.setOnClickListener { v ->
            task?.let {
                it.type = TASK_TYPE_TOMORROW
                it.date = Helper.formatToYesterdayOrTodayOrTomorrow(Helper.getCurrentDatetime(1))
                todoViewModel.updateTask(it)

                val total = it.gold + pref.getLong(KEY_TOTAL, 0L)
                val totalExp = it.exp + pref.getLong(KEY_EXP, 0L)
                val taskQty = 1 + pref.getInt(KEY_QTY, 0)

                pref.edit().putLong(KEY_TOTAL, total).apply()
                pref.edit().putLong(KEY_EXP, totalExp).apply()
                pref.edit().putInt(KEY_QTY, taskQty).apply()

                dashboardViewModel.updateUserByUsername(
                    total, totalExp, pref.getString(KEY_USERNAME, "").toString(), taskQty)

                dialog.dismiss()
                requireView().showSnackbarMessage("Congrats your task is done")
            }
        }

        dialog.show()
    }

    private fun showInsertDialog() {
        TodoDialog().apply {
            setSaveListener { taskName, reward, exp ->
                todoViewModel.addTask(
                    Task(
                        0,
                        taskName,
                        Helper.formatToYesterdayOrTodayOrTomorrow(Helper.getCurrentDatetime(0)),
                        TASK_TYPE_TODAY,      // 1. repeating, 2. once, 3. certain -> 1. Yesterday 2. Today 3. Tomorrow
                        reward,
                        exp
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
            if (pref.getBoolean(KEY_USER_EXIST, false)) {
                showInsertDialog()
            } else {
                Toast.makeText(requireContext(), "User not found.", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}