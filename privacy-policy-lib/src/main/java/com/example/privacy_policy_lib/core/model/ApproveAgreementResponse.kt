package com.example.privacy_policy_lib.core.model

import com.example.privacy_policy_lib.core.utils.RetrofitServiceFactory.BASE_NAMESPACE_PRODUCTION
import com.example.privacy_policy_lib.core.utils.RetrofitServiceFactory.HTTP
import com.example.privacy_policy_lib.core.utils.RetrofitServiceFactory.NAMESPACE_END
import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.NamespaceList
import org.simpleframework.xml.Root

// Test amaçlı kullanılırken BASE_NAMESPACE_PRODUCTION yerine BASE_NAMESPACE_PRODUCTION2 kullanılmalı.

@Root(name = "Envelope", strict = false)
@NamespaceList(
    Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap"),
    Namespace(reference = "http://www.w3.org/2001/XMLSchema-instance", prefix = "xsi"),
    Namespace(reference = "http://www.w3.org/2001/XMLSchema", prefix = "xsd")
)
data class ApproveAgreementResponse(
    @field:Element(name = "Body", required = false)
    var body: ApproveAgreementResponseBody? = null
)

@Root(name = "Body", strict = false)
data class ApproveAgreementResponseBody(
    @field:Element(name = "ApproveAgreementContentResponse", required = false)
    @Namespace(reference = "$HTTP$BASE_NAMESPACE_PRODUCTION$NAMESPACE_END")
    var approveAgreementContentResponse: ApproveAgreementContentResponse? = null
)

@Root(name = "ApproveAgreementContentResponse", strict = false)
@Namespace(reference = "$HTTP$BASE_NAMESPACE_PRODUCTION$NAMESPACE_END")
data class ApproveAgreementContentResponse(
    @field:Element(name = "ApproveAgreementContentResult", required = false)
    var approveAgreementContentResult: ApproveAgreementContentResult? = null
)

@Root(name = "ApproveAgreementContentResult", strict = false)
data class ApproveAgreementContentResult(
    @field:Element(name = "error", required = false)
    var error: Error? = null,

    @field:Element(name = "agreementToken", required = false)
    var agreementToken: String? = null
)

@Root(name = "error", strict = false)
data class Error(
    @field:Element(name = "ErrorCode", required = false)
    var errorCode: String? = null,

    @field:Element(name = "ErrorMessage", required = false)
    var errorMessage: String? = null
)
