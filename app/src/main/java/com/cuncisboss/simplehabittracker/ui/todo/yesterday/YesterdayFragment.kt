package com.cuncisboss.simplehabittracker.ui.todo.yesterday

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.cuncisboss.simplehabittracker.R
import com.cuncisboss.simplehabittracker.databinding.FragmentYesterdayBinding
import com.cuncisboss.simplehabittracker.ui.todo.TodoAdapter
import com.cuncisboss.simplehabittracker.ui.todo.TodoViewModel
import com.cuncisboss.simplehabittracker.util.Constants
import com.cuncisboss.simplehabittracker.util.Constants.TASK_TYPE_YESTERDAY
import com.cuncisboss.simplehabittracker.util.Helper.reverseThis
import com.cuncisboss.simplehabittracker.util.VisibleHelper.hideView
import kotlinx.android.synthetic.main.dialog_alert_actions.view.*
import org.koin.android.ext.android.inject

class YesterdayFragment : Fragment() {

    private val viewModel by inject<TodoViewModel>()

    private lateinit var binding: FragmentYesterdayBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_yesterday, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TodoAdapter()
        binding.rvYesterday.adapter = adapter

        viewModel.getTasks(TASK_TYPE_YESTERDAY).observe(viewLifecycleOwner, Observer {
            it.reverseThis()
            adapter.submitList(it)
        })

        adapter.setChecklistListener { v, task ->
            if (v.id == R.id.btn_checklist) {
                dialogAlert(true)
            } else {
                dialogAlert(false)
            }
        }
    }

    private fun dialogAlert(isChecked: Boolean) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(true)
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_alert_actions, null as ViewGroup?)
        builder.setView(view)

        val dialog = builder.create()

        if (isChecked) {
            view.btn_delete_task.hideView()
            view.btn_skip_task.hideView()
        } else {
            view.btn_done_task.hideView()
            view.btn_skip_task.hideView()
        }

        dialog.show()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.getItem(0).isVisible = false
    }
}