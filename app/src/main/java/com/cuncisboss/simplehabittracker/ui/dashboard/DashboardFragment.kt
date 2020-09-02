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
import com.cuncisboss.simplehabittracker.util.Constants.KEY_LEVEL
import com.cuncisboss.simplehabittracker.util.Constants.KEY_USERNAME
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
                pref.edit().putString(KEY_USERNAME, username).apply()
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
                Log.d(TAG, "setUserDetail: $user")
                binding.user = user

                Log.d(TAG, "onViewCreated: Sudah Terdaftar")
                binding.layoutUserDetail.visibility = View.VISIBLE
                binding.btnCreateUser.visibility = View.GONE
                initExp(user.exp.toDouble())
                pref.edit().putBoolean(KEY_USER_EXIST, true).apply()
            } ?: run {
                Log.d(TAG, "onViewCreated: Belum Terdaftar")
                binding.layoutUserDetail.visibility = View.GONE
                binding.btnCreateUser.visibility = View.VISIBLE
                pref.edit().putBoolean(KEY_USER_EXIST, false).apply()
            }
        })

        viewModel.level.observe(viewLifecycleOwner) {
            binding.level = "Level $it"
            viewModel.totalLevel.postValue(it)
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun initExp(exp: Double) {
        val totalExp = exp.toLong() - Helper.getTotalExp(pref.getInt(KEY_LEVEL, 1))
        if (totalExp >= Helper.calXpForLevel(Helper.calculateLevel(exp)).toLong()) {
            val nextLevel = pref.getInt(KEY_LEVEL, 1) + 1
            viewModel.setLevel(nextLevel)
            pref.edit().putInt(KEY_LEVEL, nextLevel).apply()
        } else {
            val currentLevel = pref.getInt(KEY_LEVEL, 1)
            viewModel.setLevel(currentLevel)
        }

        viewModel.totalLevel.observe(viewLifecycleOwner) { totalLevel ->
            val totalExp2 = exp.toLong() - Helper.getTotalExp(totalLevel)
            binding.progressExp.apply {
                labelText = "Exp ${totalExp2}/${Helper.calXpForLevel(totalLevel).toLong()}"
                max = Helper.calXpForLevel(totalLevel).toFloat()
                min = 0f
                progress = totalExp2.toFloat()
            }
        }
    }
}