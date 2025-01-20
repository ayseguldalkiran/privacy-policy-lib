package com.example.privacy_policy_lib.core

import android.content.Context
import androidx.annotation.StringRes
import com.example.privacy_policy_lib.R


enum class AgreementTypes(@JvmField var value: Int, @field:StringRes @param:StringRes var resId: Int) {
    GENERALAGREEMENT(1, R.string.str_general_agreement),
    TERMSOFUSE(2, R.string.str_terms_of_use);

    companion object {
        fun getStringForEnum(type: AgreementTypes, context: Context): String {
            if (type.resId != -1) {
                return context.getString(type.resId)
            } else {
                return ""
            }
        }
    }
}