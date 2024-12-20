package com.example.privacy_policy_lib.core.service

import com.example.privacy_policy_lib.enum.ServiceUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException

object PrivacyPolicyServiceProvider {

    private val client = OkHttpClient()

    private fun getUrl(isProduction: Boolean): String {
        return if (isProduction) ServiceUrl.PROD_URL.url else ServiceUrl.TEST_URL.url
    }

    fun approveAgreementContent(data: String, isProduction: Boolean): String {
        return makePostRequest(data, isProduction)
    }

    fun getCurrentApprovedAgreementContentHashByToken(
        data: String,
        isProduction: Boolean
    ): String {
        return makePostRequest(data, isProduction)
    }

    fun getAgreementContent(data: String, isProduction: Boolean): String {
        return makePostRequest(data, isProduction)
    }

    fun getCurrentApprovedAgreementContentByToken(
        data: String,
        isProduction: Boolean
    ): String {
        return makePostRequest(data, isProduction)
    }

    private fun makePostRequest(data: String, isProduction: Boolean): String {
        val url = getUrl(isProduction)
        val mediaType = "text/xml".toMediaType()
        val body = RequestBody.create(mediaType, data)

        val request = Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Content-Type", "text/xml")
            .build()

        return try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                response.body?.string() ?: throw IOException("Response body is null")
            }
        } catch (e: Exception) {
            e.message ?: "An error occurred"
        }
    }
}
