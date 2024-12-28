package com.example.privacy_policy_lib

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.privacy_policy_lib.core.service.AgreementServiceSingleton
import com.example.privacy_policy_lib.core.model.GetAgreementContentResponse

class PrivacyPolicyViewModel : ViewModel() {

    private val agreementService = AgreementServiceSingleton.instance

    private val _agreementContent = MutableLiveData<String?>()
    val agreementContent: LiveData<String?> get() = _agreementContent

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getAgreementContent(
        isProduction: Boolean,
        contractor: String,
        itemCode: String?,
        language: String,
        agreementType: String
    ) {
        _isLoading.postValue(true)

        agreementService.callGetAgreementContent(
            isProduction = isProduction,
            contractor = contractor,
            itemCode = itemCode,
            language = language,
            agreementType = agreementType,
            onSuccess = { response ->
                handleSuccess(response)
            },
            onFailure = { error ->
                handleError(error)
            }
        )
    }

    private fun handleSuccess(response: GetAgreementContentResponse?) {
        _isLoading.postValue(false)
        val content = response?.body?.contentResponse?.result?.content
        if (!content.isNullOrEmpty()) {
            _agreementContent.postValue(content)
        } else {
            _error.postValue("Agreement content is empty.")
        }
    }

    private fun handleError(throwable: Throwable) {
        _isLoading.postValue(false)
        _error.postValue(throwable.message ?: "Unknown error occurred.")
    }
}
