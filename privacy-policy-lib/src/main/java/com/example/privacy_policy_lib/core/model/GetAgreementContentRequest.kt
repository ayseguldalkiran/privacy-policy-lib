package com.example.privacy_policy_lib.core.model

import com.example.privacy_policy_lib.core.utils.RetrofitServiceFactory.BASE_NAMESPACE_PRODUCTION
import com.example.privacy_policy_lib.core.utils.RetrofitServiceFactory.BASE_NAMESPACE_PRODUCTION2
import com.example.privacy_policy_lib.core.utils.RetrofitServiceFactory.BASE_NAMESPACE_TEST
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
data class Envelope(
    @field:Element(name = "soap:Body")
    @param:Element(name = "soap:Body")
    var body: Body
)

@Root(name = "soap:Body")
data class Body(
    @field:Element(name = "GetAgreementContent")
    @param:Element(name = "GetAgreementContent")
    @field:Namespace(reference = "$HTTP$BASE_NAMESPACE_PRODUCTION$NAMESPACE_END")
    var getAgreementContent: GetAgreementContent
)

@Root(name = "GetAgreementContent")
@Namespace(reference = "$HTTP$BASE_NAMESPACE_PRODUCTION$NAMESPACE_END")
data class GetAgreementContent(
    @field:Element(name = "oGetAgreementRequest")
    @param:Element(name = "oGetAgreementRequest")
    var oGetAgreementRequest: OGetAgreementRequest
)

@Root(name = "oGetAgreementRequest")
data class OGetAgreementRequest(
    @field:Element(name = "contractor")
    @param:Element(name = "contractor")
    val contractor: String,

    @field:Element(name = "itemCode", required = false)
    @param:Element(name = "itemCode", required = false)
    val itemCode: String?,

    @field:Element(name = "language")
    @param:Element(name = "language")
    val language: String,

    @field:Element(name = "agreementType")
    @param:Element(name = "agreementType")
    val agreementType: String
)
