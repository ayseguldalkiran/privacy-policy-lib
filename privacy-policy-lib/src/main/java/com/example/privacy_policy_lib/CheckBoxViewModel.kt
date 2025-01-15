package com.example.privacy_policy_lib

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CheckBoxViewModel : ViewModel() {
    val checkboxStates = MutableLiveData<BooleanArray>()

    fun initialize(size: Int) {
        checkboxStates.value = BooleanArray(size) { false }
    }

    fun setCheckboxState(index: Int, state: Boolean) {
        val states = checkboxStates.value ?: BooleanArray(checkboxStates.value?.size ?: 0)
        if (index in states.indices) {
            states[index] = state
            checkboxStates.value = states
        }
    }
}