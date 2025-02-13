package com.example.privacy_policy_lib.core.service

import com.example.privacy_policy_lib.core.model.ApproveAgreementEnvelope
import com.example.privacy_policy_lib.core.model.ApproveAgreementResponse
import com.example.privacy_policy_lib.core.model.Body
import com.example.privacy_policy_lib.core.model.GetAgreementContentRequest
import com.example.privacy_policy_lib.core.model.GetAgreementContent
import com.example.privacy_policy_lib.core.model.OGetAgreementRequest
import com.example.privacy_policy_lib.core.utils.RetrofitServiceFactory
import com.example.privacy_policy_lib.core.model.GetAgreementContentResponse
import com.example.privacy_policy_lib.core.model.GetCurrentApprovedAgreementContentHashByTokenRequest
import com.example.privacy_policy_lib.core.model.GetCurrentApprovedAgreementContentHashByTokenResponse
import com.example.privacy_policy_lib.core.model.PrivacyPolicyState
import com.example.privacy_policy_lib.core.utils.RetrofitServiceFactory.APPROVE_AGREEMENT_CONTENT
import com.example.privacy_policy_lib.core.utils.RetrofitServiceFactory.GET_AGREEMENT_CONTENT
import com.example.privacy_policy_lib.core.utils.RetrofitServiceFactory.GET_CURRENT_APPROVED_AGREEMENT_CONTENT_HASH_BY_TOKEN
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AgreementService {

    fun callGetAgreementContent(
        agreementPosition: Int,
        onSuccess: (GetAgreementContentResponse?) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val api = RetrofitServiceFactory.createRetrofit(PrivacyPolicyState.params.isProduction)

        val soapAction = RetrofitServiceFactory.getSoapAction(PrivacyPolicyState.params.isProduction, GET_AGREEMENT_CONTENT)

        val oGetAgreementRequest = OGetAgreementRequest(
            contractor = PrivacyPolicyState.params.contractor,
            itemCode = PrivacyPolicyState.params.itemCode,
            language = PrivacyPolicyState.params.language,
            agreementType = PrivacyPolicyState.params.agreementTypes[agreementPosition].toString()
        )
        val getAgreementContent = GetAgreementContent(oGetAgreementRequest)
        val body = Body(getAgreementContent)
        val envelope = GetAgreementContentRequest(body)

        api.getAgreementContent(soapAction, envelope).enqueue(object : Callback<GetAgreementContentResponse> {
            override fun onResponse(
                call: Call<GetAgreementContentResponse>,
                response: Response<GetAgreementContentResponse>
            ) {
                if (response.isSuccessful) {
                    onSuccess(response.body())
                } else {
                    onFailure(Exception("Error: ${response.errorBody()?.string()}"))
                }
            }

            override fun onFailure(call: Call<GetAgreementContentResponse>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    fun callApproveAgreementContent(
        isProduction: Boolean,
        request: ApproveAgreementEnvelope,
        onSuccess: (ApproveAgreementResponse?) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val api = RetrofitServiceFactory.createRetrofit(isProduction)

        val soapAction = RetrofitServiceFactory.getSoapAction(isProduction, APPROVE_AGREEMENT_CONTENT)

        api.approveAgreementContent(soapAction, request).enqueue(object : Callback<ApproveAgreementResponse> {
            override fun onResponse(
                call: Call<ApproveAgreementResponse>,
                response: Response<ApproveAgreementResponse>
            ) {
                if (response.isSuccessful) {
                    onSuccess(response.body())
                } else {
                    onFailure(Exception("Error: ${response.errorBody()?.string()}"))
                }
            }

            override fun onFailure(call: Call<ApproveAgreementResponse>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    fun callGetCurrentApprovedAgreementContentHashByToken(
        isProduction: Boolean,
        request: GetCurrentApprovedAgreementContentHashByTokenRequest,
        onSuccess: (GetCurrentApprovedAgreementContentHashByTokenResponse?) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val api = RetrofitServiceFactory.createRetrofit(isProduction)

        val soapAction = RetrofitServiceFactory.getSoapAction(isProduction, GET_CURRENT_APPROVED_AGREEMENT_CONTENT_HASH_BY_TOKEN)

        api.getCurrentApprovedAgreementContentHashByToken(soapAction, request).enqueue(object : Callback<GetCurrentApprovedAgreementContentHashByTokenResponse> {
            override fun onResponse(
                call: Call<GetCurrentApprovedAgreementContentHashByTokenResponse>,
                response: Response<GetCurrentApprovedAgreementContentHashByTokenResponse>
            ) {
                if (response.isSuccessful) {
                    onSuccess(response.body())
                } else {
                    onFailure(Exception("Error: ${response.errorBody()?.string()}"))
                }
            }

            override fun onFailure(call: Call<GetCurrentApprovedAgreementContentHashByTokenResponse>, t: Throwable) {
                onFailure(t)
            }
        })
    }

}

object AgreementServiceSingleton {
    val instance: AgreementService by lazy { AgreementService() }
}
