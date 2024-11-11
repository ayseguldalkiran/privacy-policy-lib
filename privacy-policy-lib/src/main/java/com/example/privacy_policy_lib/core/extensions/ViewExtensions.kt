package com.example.privacy_policy_lib.core.extensions

import android.view.View
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetBehavior


fun View?.setBottomSheetHeight(behavior: BottomSheetBehavior<*>, heightInPercentage: Double, wm: WindowManager?){
    if(this == null) return
    val layoutParams = this.layoutParams
    val windowHeight: Int = wm.getUsableScreenHeight()
    if (layoutParams != null) {
        layoutParams.height = (windowHeight * heightInPercentage).toInt()
    }
    this.layoutParams = layoutParams
    behavior.state = BottomSheetBehavior.STATE_EXPANDED
    behavior.peekHeight = (windowHeight * heightInPercentage).toInt()
}