package com.example.privacypolicy

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.privacy_policy_lib.PrivacyPolicyDialogFragment
import com.example.privacy_policy_lib.core.utils.PreferencesHelper

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

        //showPrivacyPolicyIfNotReadBefore()
    }

    private fun showPrivacyPolicyIfNotReadBefore() {
        if (!PreferencesHelper.isPrivacyPolicyRead()) {
            val f = PrivacyPolicyDialogFragment()
            f.isCancelable = false
            f.show(supportFragmentManager, "PrivacyFragment")
        }
    }
}