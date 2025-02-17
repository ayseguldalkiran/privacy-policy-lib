package com.example.privacy_policy_lib.core

import com.example.privacy_policy_lib.core.model.GetCurrentApprovedAgreementContentHashByToken
import com.example.privacy_policy_lib.core.model.GetCurrentApprovedAgreementContentHashByTokenBody
import com.example.privacy_policy_lib.core.model.GetCurrentApprovedAgreementContentHashByTokenRequest
import com.example.privacy_policy_lib.core.model.GetCurrentApprovedAgreementContentHashByTokenResponse
import com.example.privacy_policy_lib.core.service.AgreementServiceSingleton
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

object PrivacyPolicyManager {

    private val agreementService = AgreementServiceSingleton.instance

    @JvmStatic
    suspend fun getCurrentApprovedAgreementContentHashByToken(
        isProduction: Boolean,
        agreementToken: String
    ): Result<GetCurrentApprovedAgreementContentHashByTokenResponse?> {
        return withContext(Dispatchers.IO) {
            val request = GetCurrentApprovedAgreementContentHashByTokenRequest(
                GetCurrentApprovedAgreementContentHashByTokenBody(
                    GetCurrentApprovedAgreementContentHashByToken(agreementToken)
                )
            )

            agreementService.getCurrentApprovedAgreementContentHashByToken(
                isProduction = isProduction,
                request = request
            )
        }
    }

    @JvmStatic
    fun getCurrentApprovedAgreementContentHashByTokenRx(
        isProduction: Boolean,
        agreementToken: String
    ): Single<GetCurrentApprovedAgreementContentHashByTokenResponse> {
        return Single.fromCallable {
            runBlocking {
                val result = getCurrentApprovedAgreementContentHashByToken(isProduction, agreementToken)
                result.getOrThrow() ?: throw NullPointerException("API yanıtı null döndü!")
            }
        }
            .subscribeOn(Schedulers.io())
            .onErrorResumeNext { throwable ->
                Single.error(Exception("API çağrısı başarısız oldu: ${throwable.message}"))
            }
    }
}
