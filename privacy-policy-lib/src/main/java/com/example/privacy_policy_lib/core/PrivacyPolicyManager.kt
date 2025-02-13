import com.example.privacy_policy_lib.core.model.GetCurrentApprovedAgreementContentHashByToken
import com.example.privacy_policy_lib.core.model.GetCurrentApprovedAgreementContentHashByTokenBody
import com.example.privacy_policy_lib.core.model.GetCurrentApprovedAgreementContentHashByTokenRequest
import com.example.privacy_policy_lib.core.model.GetCurrentApprovedAgreementContentHashByTokenResponse
import com.example.privacy_policy_lib.core.service.AgreementServiceSingleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object PrivacyPolicyManager {

    private val agreementService = AgreementServiceSingleton.instance

    suspend fun getCurrentApprovedAgreementContentHashByToken(
        isProduction: Boolean,
        agreementToken: String
    ): Result<GetCurrentApprovedAgreementContentHashByTokenResponse?> {
        return withContext(Dispatchers.IO) {
            val request = GetCurrentApprovedAgreementContentHashByTokenRequest(
                GetCurrentApprovedAgreementContentHashByTokenBody(
                    GetCurrentApprovedAgreementContentHashByToken(agreementToken)
                )
            )

            agreementService.getCurrentApprovedAgreementContentHashByToken(
                isProduction = isProduction,
                request = request
            )
        }
    }
}
