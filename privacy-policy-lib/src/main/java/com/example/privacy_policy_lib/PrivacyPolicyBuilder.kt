package com.example.privacy_policy_lib

import android.os.Bundle
import com.example.privacy_policy_lib.core.PrivacyPolicyManager

import com.example.privacy_policy_lib.core.model.GetCurrentApprovedAgreementContentHashByTokenResponse
import com.example.privacy_policy_lib.core.model.PrivacyPolicyLibParams

object PrivacyPolicyBuilder {
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

    @JvmStatic
    suspend fun getCurrentApprovedAgreementContent(
        isProduction: Boolean,
        agreementToken: String
    ): Result<GetCurrentApprovedAgreementContentHashByTokenResponse?> {
        return PrivacyPolicyManager.getCurrentApprovedAgreementContentHashByToken(isProduction, agreementToken)
    }
} 