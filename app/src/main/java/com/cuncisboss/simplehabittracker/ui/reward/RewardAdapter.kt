package com.cuncisboss.simplehabittracker.ui.reward

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cuncisboss.simplehabittracker.R
import com.cuncisboss.simplehabittracker.databinding.ItemRewardBinding
import com.cuncisboss.simplehabittracker.model.Reward

class RewardAdapter : RecyclerView.Adapter<RewardAdapter.ViewHolder>() {

    private var listener: (() -> Unit)? = null

    private var rewardList = arrayListOf<Reward>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_reward,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.reward = rewardList[position]
        holder.binding.btnChecklist.setOnClickListener {
            listener?.invoke()
        }
    }

    fun submistList(newList: List<Reward>) {
        rewardList.clear()
        rewardList.addAll(newList)
        notifyDataSetChanged()
    }

    fun setListener(listener: () -> Unit) {
        this.listener = listener
    }

    override fun getItemCount(): Int = rewardList.size

    class ViewHolder(val binding: ItemRewardBinding): RecyclerView.ViewHolder(binding.root)

}