package com.example.privacy_policy_lib.core.service

import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.transport.HttpTransportSE

fun callSoapWebService() {
    val namespace = "http://schemas.xmlsoap.org/soap/envelope/"
    val methodName = "GetAgreementContent"
    val soapAction = "$namespace/$methodName"
    val url = "https://license1.logo.com.tr/LogoLicenseService/AgreementService/LogoAgreementService.asmx"
    val request = SoapObject(namespace, methodName)
    val innerRequest = SoapObject(namespace, "oGetAgreementRequest")

    innerRequest.addProperty("contractor", "ELOGO")
    innerRequest.addProperty("itemCode", "eBookTransfer")
    innerRequest.addProperty("language", "TR")
    innerRequest.addProperty("agreementType", "USEAGREEMENT")

    request.addSoapObject(innerRequest)

    val envelope = SoapEnvelope(SoapEnvelope.VER11)
    envelope.dotNet = true
    envelope.setOutputSoapObject(request)

    val httpTransport = HttpTransportSE(url)
    try {
        httpTransport.call(soapAction, envelope)

        val response = envelope.response
        println("SOAP Response: $response")
    } catch (e: Exception) {
        e.printStackTrace()
        println("Error: ${e.message}")
    }
}