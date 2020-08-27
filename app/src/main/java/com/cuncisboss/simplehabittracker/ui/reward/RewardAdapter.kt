package com.cuncisboss.simplehabittracker.ui.reward

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cuncisboss.simplehabittracker.R
import com.cuncisboss.simplehabittracker.databinding.ItemRewardBinding
import com.cuncisboss.simplehabittracker.model.Reward
import com.cuncisboss.simplehabittracker.util.Helper.disableBackgroundTint

class RewardAdapter : RecyclerView.Adapter<RewardAdapter.ViewHolder>() {

    private var listener: ((Reward?) -> Unit)? = null

    private var rewardList = arrayListOf<Reward>()

    private var claimed: Boolean = false

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
            listener?.invoke(rewardList[position])
        }
        if (claimed) {
            holder.binding.btnChecklist.disableBackgroundTint()
        }
    }

    fun setClaimed(isClaimed: Boolean) {
        this.claimed = isClaimed
    }

    fun submitList(newList: List<Reward>) {
        rewardList.clear()
        rewardList.addAll(newList)
        notifyDataSetChanged()
    }

    fun setListener(listener: (Reward?) -> Unit) {
        this.listener = listener
    }

    override fun getItemCount(): Int = rewardList.size

    class ViewHolder(val binding: ItemRewardBinding): RecyclerView.ViewHolder(binding.root)

}