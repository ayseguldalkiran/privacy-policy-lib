package com.example.privacy_policy_lib

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CheckBoxViewModel : ViewModel() {
    val checkboxStates = MutableLiveData(booleanArrayOf(false, false, false))

    fun setCheckboxState(index: Int, state: Boolean) {
        val states = checkboxStates.value ?: BooleanArray(3)
        states[index] = state
        checkboxStates.value = states
    }
}