import android.os.Bundle
import com.example.privacy_policy_lib.ContractsFragment
import com.example.privacy_policy_lib.core.model.PrivacyPolicyLibParams
import com.example.privacy_policy_lib.core.utils.IntentExtraName

object PrivacyPolicyBuilder {
    fun createContractsFragment(
        params: PrivacyPolicyLibParams? = null,
        privacyPolicyFile: String = "privacy_policy.html",
        onAccepted: (() -> Unit)? = null
    ): ContractsFragment {
        return ContractsFragment().apply {
            arguments = Bundle().apply {
                putString(IntentExtraName.ARG_FILE, privacyPolicyFile)
                putParcelable("params", params)
            }
            onPrivacyPolicyAccepted = onAccepted
        }
    }
} 