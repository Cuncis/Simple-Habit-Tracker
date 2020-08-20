package com.cuncisboss.simplehabittracker.ui.todo.tomorrow

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.cuncisboss.simplehabittracker.R
import com.cuncisboss.simplehabittracker.databinding.DialogAddTaskBinding
import com.cuncisboss.simplehabittracker.databinding.FragmentTomorrowBinding
import com.cuncisboss.simplehabittracker.model.Task
import com.cuncisboss.simplehabittracker.ui.todo.TodoAdapter
import com.cuncisboss.simplehabittracker.ui.todo.TodoViewModel
import com.cuncisboss.simplehabittracker.util.Constants.TASK_TYPE_TOMORROW
import com.cuncisboss.simplehabittracker.util.Helper
import com.cuncisboss.simplehabittracker.util.Helper.reverseThis
import com.cuncisboss.simplehabittracker.util.Helper.showSnackbarMessage
import org.koin.android.ext.android.inject

class TomorrowFragment : Fragment() {

    private val viewModel by inject<TodoViewModel>()

    private lateinit var binding: FragmentTomorrowBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tomorrow, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TodoAdapter()
        binding.rvTomorrow.adapter = adapter

        viewModel.getTasks(TASK_TYPE_TOMORROW).observe(viewLifecycleOwner, Observer {
            it.reverseThis()
            adapter.submitList(it)
        })

        adapter.setChecklistListener { v, task ->
            if (v.id == R.id.btn_checklist) {
                Toast.makeText(requireContext(), "Check: ${task?.name}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Dialog Called!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun dialogInsert() {
        val builder = AlertDialog.Builder(requireContext())
        val dialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.dialog_add_task, null, false
        ) as DialogAddTaskBinding
        builder.setView(dialogBinding.root)

        val dialog = builder.create()

        dialogBinding.btnSave.setOnClickListener {
            viewModel.addTask(
                Task(
                    0,
                    dialogBinding.etTask.text.toString(),
                    Helper.formatToYesterdayOrTodayOrTomorrow(Helper.getCurrentDatetime()),
                    TASK_TYPE_TOMORROW,      // 1. repeating, 2. once, 3. certain -> 1. Yesterday 2. Today 3. Tomorrow
                    dialogBinding.etReward.text.toString().toInt()
                )
            )
            requireView().showSnackbarMessage("Task added")
            dialog.dismiss()
        }

        dialogBinding.btnCancel.setOnClickListener {
            dialog.cancel()
        }

        dialog.show()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.getItem(0).isVisible = true
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