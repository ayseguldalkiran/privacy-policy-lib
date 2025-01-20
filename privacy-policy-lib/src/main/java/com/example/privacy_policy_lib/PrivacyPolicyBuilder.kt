package com.example.privacy_policy_lib

import android.os.Bundle
import com.example.privacy_policy_lib.core.model.PrivacyPolicyLibParams

class PrivacyPolicyBuilder {
    companion object {
        @JvmStatic
        fun createContractsFragment(
            params: PrivacyPolicyLibParams? = null,
            onAccepted: (() -> Unit)? = null
        ): ContractsFragment {
            return ContractsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("params", params)
                }
                onPrivacyPolicyAccepted = onAccepted
            }
        }
    }
} 