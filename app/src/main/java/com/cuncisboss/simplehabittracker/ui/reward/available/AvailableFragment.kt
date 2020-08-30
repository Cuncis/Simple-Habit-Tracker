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
import com.cuncisboss.simplehabittracker.util.InsertDialogHelper
import com.cuncisboss.simplehabittracker.util.Constants.KEY_AVAILABLE
import com.cuncisboss.simplehabittracker.util.Constants.KEY_CLAIMED
import com.cuncisboss.simplehabittracker.util.Constants.KEY_OPTION
import com.cuncisboss.simplehabittracker.util.Constants.KEY_TOTAL
import com.cuncisboss.simplehabittracker.util.Constants.KEY_USER_EXIST
import com.cuncisboss.simplehabittracker.util.Constants.TAG_CLAIM
import com.cuncisboss.simplehabittracker.util.Constants.TAG_INSERT
import com.cuncisboss.simplehabittracker.util.Helper.showSnackbarMessage
import com.cuncisboss.simplehabittracker.util.OptionDialogHelper
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

        initListener()
    }

    private fun initListener() {
        adapter.setListener { v, reward ->
            if (v.id == R.id.btn_checklist) {
                showCheckListDialog(reward)
            } else {
                showOptionDialog(reward)
            }
        }
    }

    private fun showCheckListDialog(reward: Reward?) {
        reward?.let {
            val total = pref.getLong(KEY_TOTAL, 0L)
            if (total > it.nominal) {
                InsertDialogHelper().apply {
                    editTitleDialog(it.name)
                    editBtnNameDialog("Claim")
                    setClaimedListener(false) {
                        val result = total - it.nominal
                        pref.edit().putLong(KEY_TOTAL, result).apply()
                        binding.totalMoney = result.toString()

                        it.status = KEY_CLAIMED
                        viewModel.updateReward(it)
                    }
                }.show(childFragmentManager, TAG_CLAIM)
            } else {
                Toast.makeText(requireContext(), "Not enough gold for this reward.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showOptionDialog(reward: Reward?) {
        OptionDialogHelper().apply {
            editTitleDialog(reward?.name)
            setEditListener {
                dialogEdit(reward)
            }
            setDeleteListener {
                reward?.let {
                    viewModel.removeReward(it)
                    (requireParentFragment().view as View).showSnackbarMessage("Reward deleted")
                }
            }
        }.show(childFragmentManager, KEY_OPTION)
    }

    private fun dialogEdit(reward: Reward?) {
        TodoDialog().apply {
            editTitleDialog("Reward Title")
            setButtonUpdate("Update")
            setInitField(reward?.name, reward?.nominal.toString())
            setSaveListener { title, rwd, _ ->
                viewModel.updateReward(
                    Reward(
                        title,
                        rwd,
                        KEY_AVAILABLE,
                        reward?.id
                    )
                )
                (requireParentFragment().view as View).showSnackbarMessage("Reward updated")
            }
        }.show(childFragmentManager, TAG_INSERT)
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
            setSaveListener { title, reward, _ ->
                viewModel.addReward(
                    Reward(
                        title,
                        reward,
                        KEY_AVAILABLE
                    )
                )
                (requireParentFragment().view as View).showSnackbarMessage("Reward added")
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
            if (pref.getBoolean(KEY_USER_EXIST, false)) {
                showAddRewardDialog()
            } else {
                Toast.makeText(requireContext(), "User not found.", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}