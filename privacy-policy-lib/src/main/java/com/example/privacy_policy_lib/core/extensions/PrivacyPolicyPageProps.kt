package com.example.privacy_policy_lib.core.extensions

import com.example.privacy_policy_lib.core.model.AgreementContentResult
import com.example.privacy_policy_lib.core.model.ApproveAgreementContentResponse
import java.time.format.TextStyle

data class PrivacyPolicyPageProps(
    val navigation: Any,
    val loadingView: Any,
    val initialPageProps: InitialPageProps,
    val pdfStyles: PdfStyles? = null,
    val listItemStyles: ListItemStyles? = null,
    val privacyPolicies: List<PrivacyPolicy>?,
    val isProduction: Boolean,
    val readonly: Boolean? = null,
    val customServiceIsActive: Boolean? = null
)

data class InitialPageProps(
    val description: String? = null,
    val header: String? = null,
    val button: ButtonProps,
    val styles: InitialPageStyles? = null
)

data class ButtonProps(
    val onPress: ((List<PrivacyPolicyParameters>) -> Unit)? = null,
    val title: String
)

data class InitialPageStyles(
    val initialLabelContainerText: TextStyle? = null,
    val initialHeaderText: TextStyle? = null,
    val initialDescriptionText: TextStyle? = null
)

data class PdfStyles(
    val header: TextStyle? = null,
    //val viewer: ViewStyle? = null
)

data class ListItemStyles(
    val approveDate: TextStyle? = null,
    val itemHeader: TextStyle? = null
)

data class PrivacyPolicy(
    val source: String? = null,
    val contentHashProp: String? = null,
    val isChecked: Boolean? = null,
    val pdfPage: PdfPage,
    val isMandatory: Boolean,
    val policyParameters: PrivacyPolicyParameters,
    val labelComponent: LabelComponent
)

data class PdfPage(
    val buttonTitle: String,
    val onPressButton: ((String?) -> Unit)? = null,
    val style: Any? = null,
    val header: String? = null,
    val hideButton: Boolean? = null
)

data class LabelComponent(
    val header: String,
    val iconComponent: (Boolean?, () -> Unit) -> Any,
    val fullText: String,
    val labelButtonText: String,
    val onPress: ((Boolean?) -> Unit)? = null,
    val listItemPdfButtonText: String,
    val approveDateTitle: String? = null
)

data class PrivacyPolicyParameters(
    val itemCode: String,
    val agreementType: String,
    val contractor: String,
    val language: String,
    val agreementToken: String? = null,
    val contentHash: String? = null,
    val ipAddress: String,
    val uniqueInfo: String,
    val approveDate: String? = null
)

data class ApproveAgreementResponseProps(
    val envelope: Envelope
)

data class CurrentApprovedAgreementResponseProps(
    val envelope: Envelope
)

data class ApprovedAgreementResponseProps(
    val envelope: Envelope
)

data class GetAgreementContentResponseProps(
    val envelope: Envelope
)

data class Envelope(
    val body: Body
)

data class Body(
    val getAgreementContentResponse: GetAgreementContentResponse? = null,
    val approveAgreementContentResponse: ApproveAgreementContentResponse? = null
)

data class GetAgreementContentResponse(
    val getAgreementContentResult: AgreementContentResult
)



