package com.alexey.minay.androidlinthomework

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.GlobalScope

class MainViewModel: ViewModel() {
    val scope = GlobalScope
}