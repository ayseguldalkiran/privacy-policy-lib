package com.example.privacy_policy_lib.core.model

import com.example.privacy_policy_lib.core.utils.RetrofitServiceFactory.BASE_NAMESPACE_PRODUCTION
import com.example.privacy_policy_lib.core.utils.RetrofitServiceFactory.HTTP
import com.example.privacy_policy_lib.core.utils.RetrofitServiceFactory.NAMESPACE_END
import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root

// Test amaçlı kullanılırken BASE_NAMESPACE_PRODUCTION yerine BASE_NAMESPACE_PRODUCTION2 kullanılmalı.

@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
data class GetAgreementContentResponse(
    @field:Element(name = "Body", required = false)
    var body: GetAgreementContentResponseBody? = null
)

@Root(name = "Body", strict = false)
data class GetAgreementContentResponseBody(
    @field:Element(name = "GetAgreementContentResponse", required = false)
    @Namespace(reference = "$HTTP$BASE_NAMESPACE_PRODUCTION$NAMESPACE_END")
    var contentResponse: AgreementContentResponse? = null
)

@Root(name = "GetAgreementContentResponse", strict = false)
@Namespace(reference = "$HTTP$BASE_NAMESPACE_PRODUCTION$NAMESPACE_END")
data class AgreementContentResponse(
    @field:Element(name = "GetAgreementContentResult", required = false)
    var result: AgreementContentResult? = null
)

@Root(name = "GetAgreementContentResult", strict = false)
data class AgreementContentResult(
    @field:Element(name = "content", required = false)
    var content: String? = null,
    @field:Element(name = "contentHash", required = false)
    var contentHash: String? = null,
    @field:Element(name = "itemCode", required = false)
    var itemCode: String? = null,
    @field:Element(name = "agreementType", required = false)
    var agreementType: String? = null,
    @field:Element(name = "contractor", required = false)
    var contractor: String? = null,
    @field:Element(name = "language", required = false)
    var language: String? = null,
    @field:Element(name = "beginDate", required = false)
    var beginDate: String? = null,
    @field:Element(name = "endDate", required = false)
    var endDate: String? = null,
    @field:Element(name = "description", required = false)
    var description: String? = null,
    @field:Element(name = "agreementPeriod", required = false)
    var agreementPeriod: Int? = null,
    @field:Element(name = "error", required = false)
    var error: String? = null
)
