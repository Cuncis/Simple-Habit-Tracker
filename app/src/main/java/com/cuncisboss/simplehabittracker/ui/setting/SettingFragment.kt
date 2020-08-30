package com.cuncisboss.simplehabittracker.ui.setting

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cuncisboss.simplehabittracker.R
import com.cuncisboss.simplehabittracker.ui.dashboard.DashboardViewModel
import com.cuncisboss.simplehabittracker.ui.reward.RewardViewModel
import com.cuncisboss.simplehabittracker.ui.todo.TodoViewModel
import com.cuncisboss.simplehabittracker.util.Constants.KEY_USER_EXIST
import com.cuncisboss.simplehabittracker.util.Constants.TAG_RESET
import com.cuncisboss.simplehabittracker.util.Helper.showSnackbarMessage
import com.cuncisboss.simplehabittracker.util.InsertDialogHelper
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class SettingFragment : Fragment(R.layout.fragment_setting) {

    private val todoViewModel by inject<TodoViewModel>()
    private val rewardViewModel by inject<RewardViewModel>()
    private val dashboardViewModel by inject<DashboardViewModel>()
    private val pref by inject<SharedPreferences>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
    }

    private fun initListener() {
        btn_resetAll.setOnClickListener {
            if (pref.getBoolean(KEY_USER_EXIST, false)) {
                InsertDialogHelper().apply {
                    editBtnNameDialog("Reset All")
                    setClaimedListener(false) {
                        todoViewModel.removeAllTask()
                        rewardViewModel.removeAllReward()
                        dashboardViewModel.removeUserDetail()
                        pref.edit().clear().apply()
                        (requireParentFragment().view as View).showSnackbarMessage("Reset Data Successfully")
                    }
                }.show(childFragmentManager, TAG_RESET)
            } else {
                Toast.makeText(requireContext(), "User not found.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}