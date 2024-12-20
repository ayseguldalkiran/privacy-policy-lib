package com.example.privacy_policy_lib.core.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.privacy_policy_lib.core.extensions.PrivacyPolicy
import com.example.privacy_policy_lib.core.service.PrivacyPolicyPageHelper
import com.example.privacy_policy_lib.core.service.PrivacyPolicyServiceProvider
import com.example.privacy_policy_lib.enum.AgreementServiceNames
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PrivacyPolicyViewModel(
    private val privacyPolicyService: PrivacyPolicyServiceProvider,
    private val isProduction: Boolean
) : ViewModel() {

    val loading = MutableLiveData<Boolean>()
    val isInitialPage = MutableLiveData<Boolean>(true)
    val showPdf = MutableLiveData<Boolean>(false)
    val selectedPdf = MutableLiveData<PrivacyPolicy?>()
    val policies = MutableLiveData<List<PrivacyPolicy>>()

    private val isDatesExpiredMap = mutableMapOf<String, Boolean>()

    fun initPage(currentPolicies: List<PrivacyPolicy>) {
        loading.value = true
        viewModelScope.launch {
            currentPolicies.forEach { policy ->
                checkAgreementToken(policy, currentPolicies)
            }
            loading.value = false
        }
    }

    private suspend fun checkAgreementToken(
        policy: PrivacyPolicy,
        currentPolicies: List<PrivacyPolicy>
    ) {
        val parameters = policy.policyParameters
        if (parameters?.agreementToken != null) {
            getCurrentApprovedPolicy(policy, currentPolicies)
        } else {
            isDatesExpiredMap[parameters?.itemCode ?: ""] = true
            getAgreementContent(policy, currentPolicies)
        }
    }

    private suspend fun getCurrentApprovedPolicy(
        policy: PrivacyPolicy,
        currentPolicies: List<PrivacyPolicy>
    ) {
        val parameters = policy.policyParameters ?: return
        val dataProp = PrivacyPolicyPageHelper.getDataProp(
            parameters,
            AgreementServiceNames.GetCurrentApprovedContentHash,
            isProduction
        )
        val response = privacyPolicyService.getCurrentApprovedAgreementContentHashByToken(dataProp, isProduction)
        val responseResult = response.result
        if (responseResult?.contentHash != null && isValidDate(responseResult.endDate)) {
            isDatesExpiredMap[parameters.itemCode] = false
        } else {
            isDatesExpiredMap[parameters.itemCode] = true
            getAgreementContent(policy, currentPolicies)
        }
    }

    private fun isValidDate(endDate: String?): Boolean {
        return try {
            val today = Date()
            val end = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(endDate ?: "")
            today.before(end)
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun getAgreementContent(
        policy: PrivacyPolicy,
        currentPolicies: List<PrivacyPolicy>
    ) {
        val dataProp = PrivacyPolicyPageHelper.getDataProp(
            policy.policyParameters,
            AgreementServiceNames.GetAgreementContent,
            isProduction
        )
        val response = privacyPolicyService.getAgreementContent(dataProp)
        val responseResult = response.result
        if (responseResult?.content != null) {
            val updatedPolicy = policy.copy(
                source = "data:application/pdf;base64,${responseResult.content}",
                contentHashProp = responseResult.contentHash
            )
            policies.value = currentPolicies.map {
                if (it == policy) updatedPolicy else it
            }
        }
    }

    fun onPressPdfReadButton(policy: PrivacyPolicy) {

    }

    fun onPressApprove() {

    }
}
