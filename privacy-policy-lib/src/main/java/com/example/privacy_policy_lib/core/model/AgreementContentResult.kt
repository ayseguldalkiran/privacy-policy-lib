package com.example.privacy_policy_lib.core.model

data class AgreementContentResult(
    val agreementPeriod: String,
    val beginDate: String,
    val content: String,
    val contentHash: String,
    val description: String,
    val endDate: String,
    val error: Error?
)