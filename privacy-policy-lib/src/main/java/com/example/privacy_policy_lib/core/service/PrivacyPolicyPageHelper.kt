package com.example.privacy_policy_lib.core.service

import com.example.privacy_policy_lib.core.extensions.PrivacyPolicyParameters
import com.example.privacy_policy_lib.enum.AgreementServiceNames
import com.example.privacy_policy_lib.enum.HelperContentUrl

class PrivacyPolicyPageHelper {
    companion object {
        fun getDataProp(
            parameters: PrivacyPolicyParameters,
            serviceName: AgreementServiceNames,
            isProduction: Boolean
        ): String {
            val baseUrl = if (isProduction) HelperContentUrl.HELPER_PROD_URL else HelperContentUrl.HELPER_TEST_URL
            return when (serviceName) {
                AgreementServiceNames.ApproveContent -> """
                    <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
                      <soap:Body>
                        <ApproveAgreementContent xmlns="$baseUrl">
                          <oApproveAgreementRequest>
                            <uniqueInfo>${parameters.uniqueInfo}</uniqueInfo>
                            <ipAddress>${parameters.ipAddress}</ipAddress>
                            <contentHash>${parameters.contentHash}</contentHash>
                            <itemCode>${parameters.itemCode}</itemCode>
                            <agreementType>${parameters.agreementType}</agreementType>
                            <contractor>${parameters.contractor}</contractor>
                            <language>${parameters.language}</language>
                          </oApproveAgreementRequest>
                        </ApproveAgreementContent>
                      </soap:Body>
                    </soap:Envelope>
                """.trimIndent()

                AgreementServiceNames.GetAgreementContent -> """
                    <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
                      <soap:Body>
                        <GetAgreementContent xmlns="$baseUrl">
                          <oGetAgreementRequest>
                            <contractor>${parameters.contractor}</contractor>
                            <itemCode>${parameters.itemCode}</itemCode>
                            <language>${parameters.language}</language>
                            <agreementType>${parameters.agreementType}</agreementType>
                          </oGetAgreementRequest>
                        </GetAgreementContent>
                      </soap:Body>
                    </soap:Envelope>
                """.trimIndent()

                AgreementServiceNames.GetCurrentApprovedContentHash -> """
                    <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:agr="$baseUrl">
                        <soap:Header/>
                        <soap:Body>
                            <agr:GetCurrentApprovedAgreementContentHashByToken>
                                <agr:agreementToken>${parameters.agreementToken}</agr:agreementToken>
                            </agr:GetCurrentApprovedAgreementContentHashByToken>
                        </soap:Body>
                    </soap:Envelope>
                """.trimIndent()

                AgreementServiceNames.GetCurrentApprovedAgreementContentByToken -> """
                    <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:agr="$baseUrl">
                        <soap:Header/>
                        <soap:Body>
                            <agr:GetCurrentApprovedAgreementContentByToken>
                                <agr:agreementToken>${parameters.agreementToken}</agr:agreementToken>
                            </agr:GetCurrentApprovedAgreementContentByToken>
                        </soap:Body>
                    </soap:Envelope>
                """.trimIndent()

                else -> ""
            }
        }
    }
}
