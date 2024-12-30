package com.example.privacy_policy_lib.core.model

import com.example.privacy_policy_lib.core.utils.RetrofitServiceFactory.BASE_NAMESPACE_PRODUCTION
import com.example.privacy_policy_lib.core.utils.RetrofitServiceFactory.HTTP
import com.example.privacy_policy_lib.core.utils.RetrofitServiceFactory.NAMESPACE_END
import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.NamespaceList
import org.simpleframework.xml.Root

// Test amaçlı kullanılırken BASE_NAMESPACE_PRODUCTION yerine BASE_NAMESPACE_PRODUCTION2 kullanılmalı.

@Root(name = "soap:Envelope")
@NamespaceList(
    Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap"),
    Namespace(reference = "http://www.w3.org/2001/XMLSchema-instance", prefix = "xsi"),
    Namespace(reference = "http://www.w3.org/2001/XMLSchema", prefix = "xsd")
)
data class ApproveAgreementEnvelope(
    @field:Element(name = "soap:Body")
    @param:Element(name = "soap:Body")
    var body: ApproveAgreementBody
)

@Root(name = "soap:Body")
data class ApproveAgreementBody(
    @field:Element(name = "ApproveAgreementContent")
    @param:Element(name = "ApproveAgreementContent")
    @field:Namespace(reference = "$HTTP$BASE_NAMESPACE_PRODUCTION$NAMESPACE_END")
    var approveAgreementContent: ApproveAgreementContent
)

@Root(name = "ApproveAgreementContent")
@Namespace(reference = "$HTTP$BASE_NAMESPACE_PRODUCTION$NAMESPACE_END")
data class ApproveAgreementContent(
    @field:Element(name = "oApproveAgreementRequest")
    @param:Element(name = "oApproveAgreementRequest")
    var oApproveAgreementRequest: ApproveAgreementRequest
)

@Root(name = "oApproveAgreementRequest")
data class ApproveAgreementRequest(
    @field:Element(name = "uniqueInfo")
    @param:Element(name = "uniqueInfo")
    val uniqueInfo: String,

    @field:Element(name = "ipAddress")
    @param:Element(name = "ipAddress")
    val ipAddress: String,

    @field:Element(name = "contentHash")
    @param:Element(name = "contentHash")
    val contentHash: String,

    @field:Element(name = "extensionFields")
    @param:Element(name = "extensionFields")
    val extensionFields: String,

    @field:Element(name = "itemCode")
    @param:Element(name = "itemCode")
    val itemCode: String,

    @field:Element(name = "agreementType")
    @param:Element(name = "agreementType")
    val agreementType: String,

    @field:Element(name = "contractor")
    @param:Element(name = "contractor")
    val contractor: String,

    @field:Element(name = "language")
    @param:Element(name = "language")
    val language: String,

    @field:Element(name = "beginDate")
    @param:Element(name = "beginDate")
    val beginDate: String,

    @field:Element(name = "signedContentBase64Encoded")
    @param:Element(name = "signedContentBase64Encoded")
    val signedContentBase64Encoded: String
)