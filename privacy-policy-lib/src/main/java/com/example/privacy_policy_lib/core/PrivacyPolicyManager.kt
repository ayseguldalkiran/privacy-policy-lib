package com.example.privacy_policy_lib.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.privacy_policy_lib.core.model.GetCurrentApprovedAgreementContentHashByTokenRequest
import com.example.privacy_policy_lib.core.model.GetCurrentApprovedAgreementContentHashByTokenResponse
import com.example.privacy_policy_lib.core.model.GetCurrentApprovedAgreementContentHashByToken
import com.example.privacy_policy_lib.core.model.GetCurrentApprovedAgreementContentHashByTokenBody
import com.example.privacy_policy_lib.core.service.AgreementServiceSingleton

object PrivacyPolicyManager {

    private val agreementService = AgreementServiceSingleton.instance

    private val _approvedAgreementContentResponse = MutableLiveData<GetCurrentApprovedAgreementContentHashByTokenResponse?>()
    val approvedAgreementContentResponse: LiveData<GetCurrentApprovedAgreementContentHashByTokenResponse?> get() = _approvedAgreementContentResponse

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun getCurrentApprovedAgreementContentHashByToken(
        isProduction: Boolean,
        agreementToken: String
    ) {
        val request = GetCurrentApprovedAgreementContentHashByTokenRequest(
            GetCurrentApprovedAgreementContentHashByTokenBody(
                GetCurrentApprovedAgreementContentHashByToken(agreementToken)
            )
        )

        agreementService.callGetCurrentApprovedAgreementContentHashByToken(
            isProduction = isProduction,
            request = request,
            onSuccess = { response ->
                _approvedAgreementContentResponse.postValue(response)
            },
            onFailure = { throwable ->
                _error.postValue(throwable.message ?: "Unknown error occurred.")
            }
        )
    }
}
