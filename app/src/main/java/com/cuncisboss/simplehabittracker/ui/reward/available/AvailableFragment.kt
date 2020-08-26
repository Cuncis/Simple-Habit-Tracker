package com.cuncisboss.simplehabittracker.ui.reward.available

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.cuncisboss.simplehabittracker.R
import com.cuncisboss.simplehabittracker.databinding.FragmentAvailableBinding
import com.cuncisboss.simplehabittracker.model.Reward
import com.cuncisboss.simplehabittracker.ui.reward.RewardAdapter
import com.cuncisboss.simplehabittracker.ui.reward.RewardViewModel
import com.cuncisboss.simplehabittracker.ui.todo.TodoDialog
import com.cuncisboss.simplehabittracker.util.Constants.KEY_TOTAL
import com.cuncisboss.simplehabittracker.util.Constants.TAG
import com.cuncisboss.simplehabittracker.util.Constants.TAG_INSERT
import org.koin.android.ext.android.inject


class AvailableFragment : Fragment() {

    private lateinit var binding: FragmentAvailableBinding

    private val viewModel by inject<RewardViewModel>()
    private val pref by inject<SharedPreferences>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_available, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.totalMoney = pref.getLong(KEY_TOTAL, 0L).toString()

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.getAllRewards().observe(viewLifecycleOwner, Observer {
            val adapter = RewardAdapter()
            binding.rvAvailable.adapter = adapter
            adapter.submistList(it)
        })
    }

    private fun showAddRewardDialog() {
        TodoDialog().apply {
            editTitleDialog("Reward Title")
            setSaveListener { title, reward ->
                viewModel.addReward(
                    Reward(
                        title,
                        reward
                    )
                )
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
            showAddRewardDialog()
        }
        return super.onOptionsItemSelected(item)
    }
}