package com.example.privacy_policy_lib

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.webkit.WebViewAssetLoader
import com.example.privacy_policy_lib.adapter.ContractsAdapter
import com.example.privacy_policy_lib.core.utils.PreferencesHelper
import com.example.privacy_policy_lib.databinding.FragmentPrivacyPolicyDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.privacy_policy_lib.core.extensions.setBottomSheetHeight
import com.example.privacy_policy_lib.core.model.ContractItem

class PrivacyPolicyDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentPrivacyPolicyDialogBinding? = null
    private val binding get() = _binding!!
    private var mAdapter: ContractsAdapter? = null
    val contractItemList = arrayListOf(
        ContractItem("Gizlilik Politikası"),
        ContractItem("Aydınlatma Metni"),
        ContractItem("Ticari Elektronik İleti metni")
    )
    var mPrivacyPolicyUrl: String? = null
    var mPrivacyPolicyFile: String? = null

    companion object {
        fun newInstance(privacyPolicyUrl: String, privacyPolicyFile: String): PrivacyPolicyDialogFragment {
            val fragment = PrivacyPolicyDialogFragment()
            fragment.mPrivacyPolicyUrl = privacyPolicyUrl
            fragment.mPrivacyPolicyFile = privacyPolicyFile
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = ContractsAdapter(requireActivity())
        mAdapter!!.addItem(contractItemList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPrivacyPolicyDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.btnRead.setOnClickListener {
            PreferencesHelper.init(requireContext())
            PreferencesHelper.markPrivacyPolicyAsRead()
            dismiss()
        }
        keepFullScreen()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        keepFullScreen()
    }

    private fun keepFullScreen(){
        val bottomSheet = requireView().parent as FrameLayout
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isDraggable = false
        behavior.skipCollapsed = true
        behavior.isHideable = false
        val wm = requireActivity().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        bottomSheet.setBottomSheetHeight(behavior, 0.8,wm)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadPrivacyPolicyFromWeb()
    }

    private fun loadPrivacyPolicyFromWeb() {
        binding.webView.webViewClient = object : WebViewClient() {
            var isTimeout = true
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest,
            ): Boolean {
                val intent = Intent(Intent.ACTION_VIEW, request.url)
                view.context.startActivity(intent)
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                Handler(Looper.getMainLooper()).postDelayed({
                    if(isTimeout) {
                        loadPrivacyPolicyFromLocal()
                    }
                }, 10000)
                binding.llProgressBar.root.visibility = View.VISIBLE
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                binding.llProgressBar.root.visibility = View.GONE
                delayReadButton()
                isTimeout = false
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
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

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                loadPrivacyPolicyFromLocal()
                super.onReceivedSslError(view, handler, error)
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            if (binding.llProgressBar.root.visibility == View.VISIBLE) {
                loadPrivacyPolicyFromLocal()
            }
        }, 10000)
        binding.llProgressBar.root.visibility = View.VISIBLE
        mPrivacyPolicyUrl?.let { binding.webView.loadUrl(it) }
    }

    private fun loadPrivacyPolicyFromLocal() {
        val assetLoader = requireActivity().let {  WebViewAssetLoader.AssetsPathHandler(it) }.let {
            WebViewAssetLoader.Builder()
                .addPathHandler("/assets/", it)
                .build()
        }

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
                return assetLoader.shouldInterceptRequest(request.url)
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