package com.example.privacy_policy_lib.core.model

import com.example.privacy_policy_lib.core.utils.RetrofitServiceFactory.BASE_NAMESPACE_PRODUCTION
import com.example.privacy_policy_lib.core.utils.RetrofitServiceFactory.HTTP
import com.example.privacy_policy_lib.core.utils.RetrofitServiceFactory.NAMESPACE_END
import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root

@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class GetCurrentApprovedAgreementContentHashByTokenResponse(
    @field:Element(name = "Body", required = false)
    var body: GetCurrentApprovedAgreementContentHashByTokenResponseBody? = null
)

@Root(name = "Body", strict = false)
data class GetCurrentApprovedAgreementContentHashByTokenResponseBody(
    @field:Element(name = "GetCurrentApprovedAgreementContentHashByTokenResponse", required = false)
    @Namespace(reference = "$HTTP$BASE_NAMESPACE_PRODUCTION$NAMESPACE_END")
    var contentResponse: CurrentApprovedAgreementContentResponse? = null
)

@Root(name = "GetCurrentApprovedAgreementContentHashByTokenResponse", strict = false)
@Namespace(reference = "$HTTP$BASE_NAMESPACE_PRODUCTION$NAMESPACE_END")
data class CurrentApprovedAgreementContentResponse(
    @field:Element(name = "GetCurrentApprovedAgreementContentHashByTokenResult", required = false)
    var result: CurrentApprovedAgreementContentResult? = null
)

@Root(name = "GetCurrentApprovedAgreementContentHashByTokenResult", strict = false)
data class CurrentApprovedAgreementContentResult(
    @field:Element(name = "content", required = false)
    var content: String? = null,

    @field:Element(name = "contentHash", required = false)
    var contentHash: String? = null,

    @field:Element(name = "contentTimestamp", required = false)
    var contentTimestamp: String? = null,

    @field:Element(name = "signedContentBase64", required = false)
    var signedContentBase64: String? = null,

    @field:Element(name = "concatInfo", required = false)
    var concatInfo: String? = null,

    @field:Element(name = "beginDate", required = false)
    var beginDate: String? = null,

    @field:Element(name = "endDate", required = false)
    var endDate: String? = null,

    @field:Element(name = "approveDate", required = false)
    var approveDate: String? = null,

    @field:Element(name = "description", required = false)
    var description: String? = null,

    @field:Element(name = "agreementPeriod", required = false)
    var agreementPeriod: Int? = null,

    @field:Element(name = "error", required = false)
    var error: AgreementError? = null
)

@Root(name = "error", strict = false)
data class AgreementError(
    @field:Element(name = "ErrorCode", required = false)
    var errorCode: String? = null,

    @field:Element(name = "ErrorMessage", required = false)
    var errorMessage: String? = null
)
