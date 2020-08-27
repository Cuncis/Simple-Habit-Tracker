package com.cuncisboss.simplehabittracker.ui.reward.available

import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.cuncisboss.simplehabittracker.R
import com.cuncisboss.simplehabittracker.databinding.FragmentAvailableBinding
import com.cuncisboss.simplehabittracker.model.Reward
import com.cuncisboss.simplehabittracker.ui.reward.RewardAdapter
import com.cuncisboss.simplehabittracker.ui.reward.RewardViewModel
import com.cuncisboss.simplehabittracker.ui.todo.TodoDialog
import com.cuncisboss.simplehabittracker.util.AlertDialogHelper
import com.cuncisboss.simplehabittracker.util.Constants.KEY_AVAILABLE
import com.cuncisboss.simplehabittracker.util.Constants.KEY_CLAIMED
import com.cuncisboss.simplehabittracker.util.Constants.KEY_TOTAL
import com.cuncisboss.simplehabittracker.util.Constants.TAG_CLAIM
import com.cuncisboss.simplehabittracker.util.Constants.TAG_INSERT
import org.koin.android.ext.android.inject


class AvailableFragment : Fragment() {

    private lateinit var binding: FragmentAvailableBinding

    private val viewModel by inject<RewardViewModel>()
    private val pref by inject<SharedPreferences>()

    private lateinit var adapter: RewardAdapter

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

        adapter = RewardAdapter()

        observeViewModel()

        adapter.setListener {
            it?.let {
                val total = pref.getLong(KEY_TOTAL, 0L)
                if (total > it.nominal) {
                    AlertDialogHelper().apply {
                        editTitleDialog(it.name)
                        setClaimedListener(false) {
                            val result = total - it.nominal
                            pref.edit().putLong(KEY_TOTAL, result).apply()
                            binding.totalMoney = result.toString()

                            it.status = KEY_CLAIMED
                            viewModel.updateReward(it)
                        }
                    }.show(childFragmentManager, TAG_CLAIM)
                } else {
                    Toast.makeText(requireContext(), "Gold is not enough!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.getAllRewardsByStatus(KEY_AVAILABLE).observe(viewLifecycleOwner, Observer {
            binding.rvAvailable.adapter = adapter
            adapter.submitList(it)
        })
    }

    private fun showAddRewardDialog() {
        TodoDialog().apply {
            editTitleDialog("Reward Title")
            setSaveListener { title, reward ->
                viewModel.addReward(
                    Reward(
                        title,
                        reward,
                        KEY_AVAILABLE
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