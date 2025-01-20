package com.example.privacy_policy_lib.core.utils

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object PreferencesHelper {
    const val IS_PRIVACY_POLICY_READ = "isprivacypolicyread"
    const val PREF_NAME: String = "CommonPrefs"
    lateinit var mSharedPreferences: SharedPreferences

    @JvmStatic
    fun init(context: Context) {
        mSharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun markPrivacyPolicyAsRead() {
        try {
            setPreferences(IS_PRIVACY_POLICY_READ, "1")
        } catch (e: Exception) {
            Log.e(TAG, "markPrivacyPolicyAsRead: ", e)
        }
    }

    private fun setPreferences(prefName: String?, prefValue: String?) {
        try {
            val editor: SharedPreferences.Editor = mSharedPreferences!!.edit()
            editor.putString(prefName, prefValue)
            editor.apply()
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "setPreferences: ", e)
        }
    }
}