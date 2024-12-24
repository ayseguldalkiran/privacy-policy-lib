package com.example.privacy_policy_lib

import android.net.http.SslError
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.webkit.WebViewAssetLoader
import com.example.privacy_policy_lib.core.utils.PreferencesHelper
import com.example.privacy_policy_lib.databinding.FragmentPrivacyPolicyDialogBinding
import com.example.privacy_policy_lib.core.utils.ContextUtils

class PrivacyPolicyDialogFragment : Fragment() {

    private var _binding: FragmentPrivacyPolicyDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PrivacyPolicyViewModel by viewModels()

    private var mPrivacyPolicyUrl: String? = null
    private var mPrivacyPolicyFile: String? = null

    companion object {
        private const val ARG_URL = "privacy_policy_url"
        private const val ARG_FILE = "privacy_policy_file"

        fun newInstance(
            privacyPolicyUrl: String,
            privacyPolicyFile: String
        ): PrivacyPolicyDialogFragment {
            return PrivacyPolicyDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_URL, privacyPolicyUrl)
                    putString(ARG_FILE, privacyPolicyFile)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mPrivacyPolicyUrl = it.getString(ARG_URL)
            mPrivacyPolicyFile = it.getString(ARG_FILE)
        }
        context?.let { ContextUtils.setmContext(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrivacyPolicyDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.btnRead.setOnClickListener {
            ContextUtils.getmContext()?.let { PreferencesHelper.init(it) }
            PreferencesHelper.markPrivacyPolicyAsRead()
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeViewModel()
        loadPrivacyPolicy()
    }

    private fun observeViewModel() {
        viewModel.agreementContent.observe(viewLifecycleOwner) { content ->
            if (!content.isNullOrEmpty()) {
                try {
                    val dataUri = "data:application/pdf;base64,$content"
                    val html = "<iframe src='$dataUri' width='100%' height='100%'></iframe>"
                    binding.webView.settings.javaScriptEnabled = true
                    binding.webView.loadData(html, "text/html", "UTF-8")
                    delayReadButton()
                } catch (e: Exception) {
                    e.printStackTrace()
                    loadPrivacyPolicyFromLocal()
                }
            } else {
                loadPrivacyPolicyFromLocal()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            loadPrivacyPolicyFromLocal()
            println("Error loading privacy policy: $errorMessage")
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.llProgressBar.root.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun loadPrivacyPolicy() {
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                loadPrivacyPolicyFromLocal()
                super.onReceivedError(view, request, error)
            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                loadPrivacyPolicyFromLocal()
                super.onReceivedHttpError(view, request, errorResponse)
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                loadPrivacyPolicyFromLocal()
                super.onReceivedSslError(view, handler, error)
            }
        }
        viewModel.getAgreementContent(
            isProduction = true,
            contractor = "ELOGO",
            itemCode = "eBookTransfer",
            language = "TR",
            agreementType = "USEAGREEMENT"
        )
    }

    private fun loadPrivacyPolicyFromLocal() {
        val assetLoader = ContextUtils.getmContext()?.let { WebViewAssetLoader.AssetsPathHandler(it) }?.let {
            WebViewAssetLoader.Builder()
                .addPathHandler("/assets/", it)
                .build()
        }

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
                return assetLoader?.shouldInterceptRequest(request.url)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                binding.llProgressBar.root.visibility = View.GONE
                delayReadButton()
                super.onPageFinished(view, url)
            }
        }

        mPrivacyPolicyFile?.let { binding.webView.loadUrl(it) }
    }

    private fun delayReadButton() {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.btnRead.visibility = View.VISIBLE
        }, 1000)
    }
}