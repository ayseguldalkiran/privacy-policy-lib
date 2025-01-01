package com.example.privacy_policy_lib

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.privacy_policy_lib.core.model.ApproveAgreementBody
import com.example.privacy_policy_lib.core.model.ApproveAgreementContent
import com.example.privacy_policy_lib.core.service.AgreementServiceSingleton
import com.example.privacy_policy_lib.core.model.GetAgreementContentResponse
import com.example.privacy_policy_lib.core.model.ApproveAgreementEnvelope
import com.example.privacy_policy_lib.core.model.ApproveAgreementRequest
import com.example.privacy_policy_lib.core.model.ApproveAgreementResponse
import com.example.privacy_policy_lib.core.model.GetAgreementContentParams

class PrivacyPolicyViewModel : ViewModel() {

    private val agreementService = AgreementServiceSingleton.instance

    private val _agreementResponse = MutableLiveData<GetAgreementContentResponse>()
    val agreementResponse: LiveData<GetAgreementContentResponse> get() = _agreementResponse

    private val _approvalResult = MutableLiveData<ApproveAgreementResponse?>()
    val approvalResult: LiveData<ApproveAgreementResponse?> get() = _approvalResult

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading // Şu an kullanmıyoruz, kullanılabilir.

    fun getAgreementContent(
        getAgreementContentParams: GetAgreementContentParams
    ) {
        _isLoading.postValue(true)

        agreementService.callGetAgreementContent(
            getAgreementContentParams,
            onSuccess = { response ->
                handleSuccess(response)
            },
            onFailure = { error ->
                handleError(error)
            }
        )
    }

    fun createApproveAgreementEnvelope(
        approveAgreementRequest: ApproveAgreementRequest
    ): ApproveAgreementEnvelope {
        val approveAgreementContent = ApproveAgreementContent(approveAgreementRequest)
        val body = ApproveAgreementBody(approveAgreementContent)
        val envelope = ApproveAgreementEnvelope(body)
        return envelope
    }

    fun approveAgreementContent(
        isProduction: Boolean,
        request: ApproveAgreementEnvelope
    ) {
        _isLoading.postValue(true)

        agreementService.callApproveAgreementContent(
            isProduction = isProduction,
            request = request,
            onSuccess = { response ->
                handleApprovalSuccess(response)
            },
            onFailure = { error ->
                handleError(error)
            }
        )
    }

    private fun handleSuccess(response: GetAgreementContentResponse?) {
        _isLoading.postValue(false)
        response?.let {
            val content = it.body?.contentResponse?.result?.content
            if (!content.isNullOrEmpty()) {
                _agreementResponse.postValue(it)
            } else {
                _error.postValue("Agreement content is empty.")
            }
        }
    }

    private fun handleApprovalSuccess(response: ApproveAgreementResponse?) {
        _isLoading.postValue(false)
        _approvalResult.postValue(response)
    }

    private fun handleError(throwable: Throwable) {
        _isLoading.postValue(false)
        _error.postValue(throwable.message ?: "Unknown error occurred.")
    }
}
