package com.example.privacy_policy_lib.core.model.api

import com.example.privacy_policy_lib.core.model.ApproveAgreementEnvelope
import com.example.privacy_policy_lib.core.model.ApproveAgreementResponse
import com.example.privacy_policy_lib.core.model.GetAgreementContentRequest
import com.example.privacy_policy_lib.core.model.GetAgreementContentResponse
import com.example.privacy_policy_lib.core.model.GetCurrentApprovedAgreementContentHashByTokenRequest
import com.example.privacy_policy_lib.core.model.GetCurrentApprovedAgreementContentHashByTokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface AgreementServiceApi {

    companion object {
        const val REQUEST_URL = "LogoLicenseService/AgreementService/LogoAgreementService.asmx"
        const val REQUEST_URL_TEST = "AgreementService/LogoAgreementService.asmx"
        //Test amaçlı kullanılırken REQUEST_URL_TEST kullanılmalı.
    }

    @Headers("Content-Type: text/xml; charset=utf-8")
    @POST(REQUEST_URL)
    fun getAgreementContent(
        @Header("SOAPAction") soapAction: String,
        @Body envelope: GetAgreementContentRequest
    ): Call<GetAgreementContentResponse>

    @Headers("Content-Type: text/xml; charset=utf-8")
    @POST(REQUEST_URL)
    fun approveAgreementContent(
        @Header("SOAPAction") soapAction: String,
        @Body envelope: ApproveAgreementEnvelope
    ): Call<ApproveAgreementResponse>

    @Headers("Content-Type: text/xml; charset=utf-8")
    @POST(REQUEST_URL)
    fun getCurrentApprovedAgreementContentHashByToken(
        @Header("SOAPAction") soapAction: String,
        @Body envelope: GetCurrentApprovedAgreementContentHashByTokenRequest
    ): Call<GetCurrentApprovedAgreementContentHashByTokenResponse>

}