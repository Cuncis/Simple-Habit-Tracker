package com.cuncisboss.simplehabittracker.ui.todo

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cuncisboss.simplehabittracker.R
import com.cuncisboss.simplehabittracker.databinding.ItemTaskBinding
import com.cuncisboss.simplehabittracker.model.Task

class TodoAdapter : RecyclerView.Adapter<TodoAdapter.ViewHolder>(){

    private var taskList = arrayListOf<Task>()
    private var listener: ((view: View, Task?) -> Unit)? = null

    class ViewHolder(val binding: ItemTaskBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_task,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = taskList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.task = taskList[position]
        holder.itemView.setOnClickListener {
            listener?.invoke(it, taskList[position])
        }
        holder.binding.btnChecklist.setOnClickListener {
            listener?.invoke(it, taskList[position])
        }
    }

    fun setChecklistListener(listener: (view: View, Task?) -> Unit) {
        this.listener = listener
    }

    fun submitList(newTaskList: List<Task>) {
        taskList.clear()
        taskList.addAll(newTaskList)
        notifyDataSetChanged()
    }

}