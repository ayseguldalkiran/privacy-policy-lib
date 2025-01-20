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
import com.example.privacy_policy_lib.core.model.PrivacyPolicyState
import com.example.privacy_policy_lib.core.model.PrivacyPolicyState.PARAMS_TO_GET_FROM_APP

class PrivacyPolicyMainActivity : AppCompatActivity() {
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

        val params = intent?.getParcelableExtra<PrivacyPolicyLibParams>(PARAMS_TO_GET_FROM_APP)

        openFragment(params)
    }

    private fun openFragment(params: PrivacyPolicyLibParams?) {
        var contractItemList = arrayListOf<ContractItem>()
        params?.let {
            PrivacyPolicyState.params = it
            contractItemList = ArrayList(
                it.agreementTypes.map { agreement ->
                    ContractItem(AgreementTypes.getStringForEnum(agreement, this))
                }
            )
        } ?: run {
            val agreementTypes = arrayListOf(
                AgreementTypes.GENERALAGREEMENT,
                AgreementTypes.TERMSOFUSE
            )
            contractItemList = arrayListOf(
                ContractItem(getStringForEnum(agreementTypes[0], this)),
                ContractItem(getStringForEnum(agreementTypes[1], this))
            )
        }

        val fragment = ContractsFragment()
        fragment.contractItemList = contractItemList

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}