package com.cuncisboss.simplehabittracker.ui.reward.claimed

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.cuncisboss.simplehabittracker.R
import com.cuncisboss.simplehabittracker.model.Reward
import com.cuncisboss.simplehabittracker.ui.reward.RewardAdapter
import com.cuncisboss.simplehabittracker.ui.reward.RewardViewModel
import com.cuncisboss.simplehabittracker.util.AlertDialogHelper
import com.cuncisboss.simplehabittracker.util.Constants.KEY_CLAIMED
import com.cuncisboss.simplehabittracker.util.Constants.TAG
import com.cuncisboss.simplehabittracker.util.Constants.TAG_CLAIM
import com.cuncisboss.simplehabittracker.util.Helper
import kotlinx.android.synthetic.main.fragment_claimed.*
import org.koin.android.ext.android.inject

class ClaimedFragment : Fragment(R.layout.fragment_claimed) {

    private val viewModel by inject<RewardViewModel>()

    private lateinit var adapter: RewardAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RewardAdapter()

        adapter.setListener {
            AlertDialogHelper().apply {
                editTitleDialog(it?.name)
                setClaimedListener(true) { /* NO-OP */ }
            }.show(childFragmentManager, TAG_CLAIM)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.getAllRewardsByStatus(KEY_CLAIMED).observe(viewLifecycleOwner, Observer {
            rv_claimed.adapter = adapter
            adapter.submitList(it)
        })
    }
}