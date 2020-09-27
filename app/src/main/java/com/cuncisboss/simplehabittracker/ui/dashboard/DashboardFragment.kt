package com.cuncisboss.simplehabittracker.ui.dashboard

import android.content.SharedPreferences
import android.graphics.Color
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
import com.cuncisboss.simplehabittracker.util.Constants.TAG_ADD_USER
import com.cuncisboss.simplehabittracker.util.Helper
import com.cuncisboss.simplehabittracker.util.Helper.showSnackbarMessage
import com.cuncisboss.simplehabittracker.util.VisibleHelper.hideView
import com.cuncisboss.simplehabittracker.util.VisibleHelper.showView
import com.cuncisboss.simplehabittracker.util.getCurrentWeek
import kotlinx.android.synthetic.main.fragment_dashboard.*
import lecho.lib.hellocharts.model.*
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
        initChart()

        Log.d("_logHabit", "onViewCreated: ${getCurrentWeek()}")
    }

    private fun initChart() {
//        val axisData = listOf(
//            "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
//        )
        val axisData = getCurrentWeek()
        val yAxisData = listOf(15, 20, 12, 12, 15, 19, 20, 21, 21, 24)

        val yAxisValues: ArrayList<PointValue> = ArrayList()
        val axisValues: ArrayList<AxisValue> = ArrayList()


        val line: Line = Line(yAxisValues).setColor(Color.parseColor("#9C27B0"))

        for (i in axisData.indices) {
            axisValues.add(i, AxisValue(i.toFloat()).setLabel(axisData[i]))
        }

        for (i in yAxisData.indices) {
            yAxisValues.add(PointValue(i.toFloat(), yAxisData[i].toFloat()))
        }

        val lines: MutableList<Line> = ArrayList()
        lines.add(line)

        val data = LineChartData()
        data.lines = lines

        val axis = Axis()
        axis.apply {
            values = axisValues
            textSize = 16
            textColor = Color.parseColor("#000000")
        }.also {
            data.axisXBottom = it
        }

        val yAxis = Axis()
        yAxis.apply {
            name = "Tasks"
            textColor = Color.parseColor("#000000")
            textSize = 16

        }.also {
            data.axisYLeft = it
        }

        binding.lineChartView.apply {
            lineChartData = data
            val viewport = Viewport(this.maximumViewport)
            viewport.top = 30f
            viewport.bottom = 0f
            maximumViewport = viewport
            currentViewport = viewport
        }
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
                binding.user = user     // Sudah Terdaftar

                binding.layoutUserDetail.visibility = View.VISIBLE
                binding.btnCreateUser.visibility = View.GONE
                initExp(user.exp.toDouble())
                pref.edit().putBoolean(KEY_USER_EXIST, true).apply()
            } ?: run {                  // Belum Terdaftar
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
        val currentLevel = pref.getInt(KEY_LEVEL, 1)
        val totalExp = exp.toLong() - Helper.getTotalExp(currentLevel)

        if (totalExp >= Helper.calXpForLevel(Helper.calculateLevel(exp)).toLong()) {
            val nextLevel = pref.getInt(KEY_LEVEL, 1) + 1
            viewModel.setLevel(nextLevel)
            pref.edit().putInt(KEY_LEVEL, nextLevel).apply()
        } else {
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