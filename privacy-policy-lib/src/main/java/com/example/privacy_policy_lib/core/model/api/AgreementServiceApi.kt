package com.example.privacy_policy_lib.core.model.api

import com.example.privacy_policy_lib.core.model.Envelope
import com.example.privacy_policy_lib.core.model.GetAgreementContentResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface AgreementServiceApi {

    @Headers("Content-Type: text/xml; charset=utf-8")
    @POST("AgreementService/LogoAgreementService.asmx")
    fun getAgreementContent(
        @Header("SOAPAction") soapAction: String,
        @Body envelope: Envelope
    ): Call<GetAgreementContentResponse>

}