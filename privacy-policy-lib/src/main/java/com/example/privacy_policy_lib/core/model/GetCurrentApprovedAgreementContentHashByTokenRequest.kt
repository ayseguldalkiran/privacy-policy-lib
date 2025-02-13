package com.example.privacy_policy_lib.core.model

import com.example.privacy_policy_lib.core.utils.RetrofitServiceFactory.BASE_NAMESPACE_PRODUCTION
import com.example.privacy_policy_lib.core.utils.RetrofitServiceFactory.HTTP
import com.example.privacy_policy_lib.core.utils.RetrofitServiceFactory.NAMESPACE_END
import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.NamespaceList
import org.simpleframework.xml.Root

@Root(name = "soap:Envelope")
@NamespaceList(
    Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap"),
    Namespace(reference = "http://www.w3.org/2001/XMLSchema-instance", prefix = "xsi"),
    Namespace(reference = "http://www.w3.org/2001/XMLSchema", prefix = "xsd")
)
data class GetCurrentApprovedAgreementContentHashByTokenRequest(
    @field:Element(name = "soap:Body")
    @param:Element(name = "soap:Body")
    var body: GetCurrentApprovedAgreementContentHashByTokenBody
)

@Root(name = "soap:Body")
data class GetCurrentApprovedAgreementContentHashByTokenBody(
    @field:Element(name = "GetCurrentApprovedAgreementContentHashByToken")
    @param:Element(name = "GetCurrentApprovedAgreementContentHashByToken")
    @field:Namespace(reference = "$HTTP$BASE_NAMESPACE_PRODUCTION$NAMESPACE_END")
    var getCurrentApprovedAgreementContentHashByToken: GetCurrentApprovedAgreementContentHashByToken
)

@Root(name = "GetCurrentApprovedAgreementContentHashByToken")
@Namespace(reference = "$HTTP$BASE_NAMESPACE_PRODUCTION$NAMESPACE_END")
data class GetCurrentApprovedAgreementContentHashByToken(
    @field:Element(name = "agreementToken")
    @param:Element(name = "agreementToken")
    val agreementToken: String
)
