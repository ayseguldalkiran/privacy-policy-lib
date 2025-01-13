package com.example.privacypolicy

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.privacy_policy_lib.ContractsFragment
import com.example.privacy_policy_lib.core.AgreementTypes
import com.example.privacy_policy_lib.core.AgreementTypes.Companion.getStringForEnum
import com.example.privacy_policy_lib.core.model.ContractItem
import com.example.privacy_policy_lib.core.model.PrivacyPolicyLibParams
import com.example.privacy_policy_lib.core.utils.IntentExtraName

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        window.statusBarColor = getColor(R.color.colorPrimaryDark)

        openFragment()
    }

    private fun openFragment() {
        val agreementType = arrayListOf(
            AgreementTypes.GENERALAGREEMENT,
            AgreementTypes.TERMSOFUSE
        )
        val params = PrivacyPolicyLibParams(
            isProduction = true,
            contractor = "ELOGO",
            itemCode = "eBookTransfer",
            language = "TR",
            agreementType = agreementType[0]
        )

        val contractItemList = arrayListOf(
            ContractItem(getStringForEnum(agreementType[0], this)),
            ContractItem(getStringForEnum(agreementType[1], this))
        )

        val privacyPolicyFile = "privacy_policy.html"

        val bundle = Bundle().apply {
            putParcelable(IntentExtraName.ARG_PARAMS, params)
            putString(IntentExtraName.ARG_FILE, privacyPolicyFile)
        }

        val fragment = ContractsFragment()
        fragment.contractItemList = contractItemList
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}