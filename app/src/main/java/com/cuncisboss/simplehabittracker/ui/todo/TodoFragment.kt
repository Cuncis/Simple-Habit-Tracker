package com.cuncisboss.simplehabittracker.ui.todo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cuncisboss.simplehabittracker.R
import com.cuncisboss.simplehabittracker.model.Task
import kotlinx.android.synthetic.main.fragment_todo.*


class TodoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TodoAdapter()
        rv_task.adapter = adapter

        val list = arrayListOf<Task>()
        list.add(Task(0, "", "", 0, 0L, false))
        list.add(Task(0, "", "", 0, 0L, false))
        list.add(Task(0, "", "", 0, 0L, false))
        list.add(Task(0, "", "", 0, 0L, false))
        list.add(Task(0, "", "", 0, 0L, false))
        list.add(Task(0, "", "", 0, 0L, false))
        list.add(Task(0, "", "", 0, 0L, false))
        list.add(Task(0, "", "", 0, 0L, false))
        list.add(Task(0, "", "", 0, 0L, false))
        list.add(Task(0, "", "", 0, 0L, false))
        list.add(Task(0, "", "", 0, 0L, false))
        list.add(Task(0, "", "", 0, 0L, false))
        adapter.submitList(list)
    }

}