package com.example.privacy_policy_lib.core.service

import com.example.privacy_policy_lib.core.model.Body
import com.example.privacy_policy_lib.core.model.Envelope
import com.example.privacy_policy_lib.core.model.GetAgreementContent
import com.example.privacy_policy_lib.core.model.OGetAgreementRequest
import com.example.privacy_policy_lib.core.utils.RetrofitServiceFactory
import com.example.privacy_policy_lib.core.model.GetAgreementContentResponse
import com.example.privacy_policy_lib.core.utils.RetrofitServiceFactory.GET_AGREEMENT_CONTENT
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AgreementService {

    fun callGetAgreementContent(
        isProduction: Boolean,
        contractor: String,
        itemCode: String?,
        language: String,
        agreementType: String,
        onSuccess: (GetAgreementContentResponse?) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val api = RetrofitServiceFactory.createRetrofit(isProduction)

        val soapAction = RetrofitServiceFactory.getSoapAction(isProduction, GET_AGREEMENT_CONTENT)

        val oGetAgreementRequest = OGetAgreementRequest(
            contractor = contractor,
            itemCode = itemCode ?: "",
            language = language,
            agreementType = agreementType
        )
        val getAgreementContent = GetAgreementContent(oGetAgreementRequest)
        val body = Body(getAgreementContent)
        val envelope = Envelope(body)

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
}

object AgreementServiceSingleton {
    val instance: AgreementService by lazy { AgreementService() }
}
