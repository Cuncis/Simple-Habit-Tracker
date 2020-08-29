package com.cuncisboss.simplehabittracker.ui.setting

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.cuncisboss.simplehabittracker.R
import com.cuncisboss.simplehabittracker.ui.reward.RewardViewModel
import com.cuncisboss.simplehabittracker.ui.todo.TodoViewModel
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
    private val pref by inject<SharedPreferences>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
    }

    private fun initListener() {
        btn_resetAll.setOnClickListener {
            InsertDialogHelper().apply {
                editBtnNameDialog("Reset All")
                setClaimedListener(false) {
                    todoViewModel.removeAllTask()
                    rewardViewModel.removeAllReward()
                    pref.edit().clear().apply()
                    (requireParentFragment().view as View).showSnackbarMessage("Reset Data Successfully")
                }
            }.show(childFragmentManager, TAG_RESET)
        }
    }
}