package com.cuncisboss.simplehabittracker.ui.dashboard

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.cuncisboss.simplehabittracker.R
import com.cuncisboss.simplehabittracker.databinding.FragmentDashboardBinding
import com.cuncisboss.simplehabittracker.model.User
import com.cuncisboss.simplehabittracker.util.AddUserDialogHelper
import com.cuncisboss.simplehabittracker.util.Constants.KEY_EXP
import com.cuncisboss.simplehabittracker.util.Constants.KEY_EXP_TOTAL
import com.cuncisboss.simplehabittracker.util.Constants.KEY_LEVEL
import com.cuncisboss.simplehabittracker.util.Constants.KEY_TOTAL
import com.cuncisboss.simplehabittracker.util.Constants.KEY_USER_EXIST
import com.cuncisboss.simplehabittracker.util.Constants.TAG
import com.cuncisboss.simplehabittracker.util.Constants.TAG_ADD_USER
import com.cuncisboss.simplehabittracker.util.Helper
import com.cuncisboss.simplehabittracker.util.Helper.showSnackbarMessage
import com.cuncisboss.simplehabittracker.util.VisibleHelper.hideView
import com.cuncisboss.simplehabittracker.util.VisibleHelper.showView
import org.koin.android.ext.android.inject


class DashboardFragment : Fragment() {
    
    private lateinit var binding: FragmentDashboardBinding

    private val pref by inject<SharedPreferences>()
    private val viewModel by inject<DashboardViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreateUser.setOnClickListener {
            showAddUserDialog()
        }
        setUserDetail()

    }

    private fun showAddUserDialog() {
        AddUserDialogHelper().apply {
            setSaveListener { username ->
                viewModel.insertUser(
                    User(
                        username.toString(),
                        "Beginner",      // Beginner, Intermediate, Advanced, Expert
                    "Level 1",
                        0L,
                        0L
                    )
                )
                (requireParentFragment().view as View).showSnackbarMessage("New User Added Successfully")
            }
        }.show(childFragmentManager, TAG_ADD_USER)
    }

    private fun setUserDetail() {
        viewModel.showUserDetail().observe(viewLifecycleOwner, Observer {
            binding.layoutUserDetail.showView()
            binding.btnCreateUser.hideView()

            it?.let { user ->
                user.gold = pref.getLong(KEY_TOTAL, 0L)
                user.exp = pref.getLong(KEY_EXP, 0L)
                binding.user = user

                Log.d(TAG, "onViewCreated: Sudah Terdaftar")
                binding.layoutUserDetail.visibility = View.VISIBLE
                binding.btnCreateUser.visibility = View.GONE
                initExp(pref.getLong(KEY_EXP, 0L).toDouble())
                pref.edit().putBoolean(KEY_USER_EXIST, true).apply()
            } ?: run {
                Log.d(TAG, "onViewCreated: Belum Terdaftar")
                binding.layoutUserDetail.visibility = View.GONE
                binding.btnCreateUser.visibility = View.VISIBLE
                pref.edit().putBoolean(KEY_USER_EXIST, false).apply()
            }
        })
    }

    private fun initExp(exp: Double) {
        binding.level = "Level ${pref.getInt(KEY_LEVEL, 1)}"

        var total = 0L
        for (i in 1 until pref.getInt(KEY_LEVEL, 1)) {
            Log.d(TAG, "initExp: Level $i, Exp: ${Helper.calXpForLevel(i).toLong()}")
            total += Helper.calXpForLevel(i).toLong()
        }
        // exp -
        val totalExp = exp.toLong() - total
        if (totalExp >= Helper.calXpForLevel(Helper.calculateLevel(exp)).toLong()) {
            pref.edit().putInt(KEY_LEVEL, (pref.getInt(KEY_LEVEL, 1)+1)).apply()
        }

        Log.d(TAG, "initExp: Total Loop: $total")
        Log.d(TAG, "initExp: Exp: ${exp.toLong()}")
        Log.d(TAG, "initExp: TotalExp: $totalExp")
        Log.d(TAG, "initExp: HelperToLong: ${Helper.calXpForLevel(Helper.calculateLevel(exp)).toLong()}")

        binding.progressExp.apply {
            val totalLevel = pref.getInt(KEY_LEVEL, 1)
            labelText = "Exp ${totalExp}/${Helper.calXpForLevel(totalLevel).toLong()}"
            max = Helper.calXpForLevel(totalLevel).toFloat()
            min = 0f
            progress = totalExp.toFloat()
        }
    }
}