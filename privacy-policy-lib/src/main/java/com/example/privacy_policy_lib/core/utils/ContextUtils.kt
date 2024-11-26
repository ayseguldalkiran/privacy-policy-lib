package com.example.privacy_policy_lib.core.utils

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import androidx.annotation.StringRes

class ContextUtils {
    companion object {
        @SuppressLint("StaticFieldLeak")
        var mContext: Context? = null

        fun getmContext(): Context? {
            return mContext
        }

        fun setmContext(mContext: Context) {
            this.mContext = mContext
        }

        fun getStringResource(@StringRes resId: Int): String {
            var value = ""
            if (resId == 0) return value
            try {
                value = getmContext()?.getString(resId).toString()
            } catch (e: java.lang.Exception) {
                android.util.Log.e(TAG, "getStringResource: ", e)
            }
            return value
        }

    }
}
