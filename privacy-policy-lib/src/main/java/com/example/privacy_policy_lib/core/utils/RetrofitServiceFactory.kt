package com.example.privacy_policy_lib.core.utils

import com.example.privacy_policy_lib.core.model.api.AgreementServiceApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitServiceFactory {

    private fun getBaseUrl(isProduction: Boolean): String {
        return if (isProduction) {
            "$HTTPS$BASE_NAMESPACE_PRODUCTION"
        } else {
            "$HTTP$BASE_NAMESPACE_TEST"
        }
    }

    fun getSoapAction(isProduction: Boolean, methodName: String): String {
        return if (isProduction) {
            "$HTTP$BASE_NAMESPACE_PRODUCTION$NAMESPACE_END/$methodName"
        } else {
            "$HTTP$BASE_NAMESPACE_PRODUCTION2$NAMESPACE_END/$methodName"
        }
    }

    fun createRetrofit(
        isProduction: Boolean,
        readTimeout: Long = 30,
        connectTimeout: Long = 10
    ): AgreementServiceApi {
        val baseUrl = getBaseUrl(isProduction)

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor(logging)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)

        val client = clientBuilder.build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
            .create(AgreementServiceApi::class.java)
    }

    const val HTTP = "http://"
    const val HTTPS = "https://"
    const val BASE_NAMESPACE_PRODUCTION = "license1.logo.com.tr/"
    const val BASE_NAMESPACE_PRODUCTION2 = "license2.logo.com.tr/"
    const val BASE_NAMESPACE_TEST = "licensetest.logo.com.tr/"
    const val NAMESPACE_END = "LogoLicenseService/AgreementService"
    const val GET_AGREEMENT_CONTENT = "GetAgreementContent"

}
