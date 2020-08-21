package com.cuncisboss.simplehabittracker.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.cuncisboss.simplehabittracker.R
import com.cuncisboss.simplehabittracker.util.Constants.KEY_TOTAL
import kotlinx.android.synthetic.main.fragment_reward.*
import org.koin.android.ext.android.inject


class RewardFragment : Fragment() {

    private val pref by inject<SharedPreferences>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reward, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_text_sample.text = pref.getLong(KEY_TOTAL, 0L).toString()
    }


}