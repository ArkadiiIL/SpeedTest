package com.arkadii.myspeedtest.ui.speedtest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arkadii.myspeedtest.databinding.FragmentSpeedTestBinding

class SpeedTestFragment : Fragment() {

    private var _binding: FragmentSpeedTestBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val speedTestViewModel =
            ViewModelProvider(this)[SpeedTestViewModel::class.java]

        _binding = FragmentSpeedTestBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}