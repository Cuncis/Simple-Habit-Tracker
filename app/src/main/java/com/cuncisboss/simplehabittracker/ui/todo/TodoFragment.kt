package com.cuncisboss.simplehabittracker.ui.todo

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.cuncisboss.simplehabittracker.R
import com.cuncisboss.simplehabittracker.databinding.DialogAddTaskBinding
import com.cuncisboss.simplehabittracker.databinding.FragmentTodoBinding
import com.cuncisboss.simplehabittracker.model.Task
import com.cuncisboss.simplehabittracker.util.Constants.TAG
import com.cuncisboss.simplehabittracker.util.Helper
import com.cuncisboss.simplehabittracker.util.Helper.reverseThis
import com.cuncisboss.simplehabittracker.util.Helper.showSnackbarMessage
import kotlinx.android.synthetic.main.fragment_todo.*
import org.koin.android.ext.android.inject


class TodoFragment : Fragment() {

    private val viewModel by inject<TodoViewModel>()

//    private lateinit var dialogBinding: DialogAddTaskBinding
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
        Log.d(
            TAG,
            "dialogInsert: ${Helper.formatToYesterdayOrTodayOrTomorrow(Helper.getCurrentDatetime())}"
        )

        val adapter = TodoAdapter()
        rv_task.adapter = adapter

        viewModel.getTasks().observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "onViewCreated: $it")
            it.reverseThis()
            adapter.submitList(it)
        })
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
                    1,      // 1. repeating, 2. once, 3. certain
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