package com.arkadii.myspeedtest.ui.speedtest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SpeedTestViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is "
    }
    val text: LiveData<String> = _text
}