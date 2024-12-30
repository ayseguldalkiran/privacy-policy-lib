package com.example.privacy_policy_lib

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ParcelFileDescriptor
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.webkit.WebViewAssetLoader
import com.example.privacy_policy_lib.core.model.ApproveAgreementRequest
import com.example.privacy_policy_lib.core.utils.ContextUtils
import com.example.privacy_policy_lib.core.utils.PreferencesHelper
import com.example.privacy_policy_lib.databinding.FragmentPrivacyPolicyDialogBinding
import java.io.File

class PrivacyPolicyDialogFragment : Fragment() {

    private var _binding: FragmentPrivacyPolicyDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PrivacyPolicyViewModel by viewModels()

    private var mPrivacyPolicyUrl: String? = null
    private var mPrivacyPolicyFile: String? = null
    private var contentHash = ""

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        loadPrivacyPolicy()
    }

    override fun onStart() {
        super.onStart()
        binding.btnRead.setOnClickListener {
            ContextUtils.getmContext()?.let { PreferencesHelper.init(it) }
            PreferencesHelper.markPrivacyPolicyAsRead()
            sendApproveRequest()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        viewModel.agreementResponse.observe(viewLifecycleOwner) { response ->
            val content = response?.body?.contentResponse?.result?.content
            if (!content.isNullOrEmpty()) {
                contentHash = response.body?.contentResponse?.result?.contentHash ?: ""
                try {
                    displayPdf(content)
                } catch (e: Exception) {
                    e.printStackTrace()
                    switchToWebViewToShowLocalFile()
                }
            } else {
                switchToWebViewToShowLocalFile()
            }
        }
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            switchToWebViewToShowLocalFile()
        }
        viewModel.approvalResult.observe(viewLifecycleOwner) { response ->
            response?.let {
                // Token burada alınıp saklanmalı.
                Log.i("Approval Result Token", it.body?.approveAgreementContentResponse?.approveAgreementContentResult?.agreementToken ?: "")
                activity?.supportFragmentManager?.popBackStack()
            }
        }
    }

    private fun loadPrivacyPolicy() {
        binding.llProgressBar.root.visibility = View.VISIBLE
        // Bunlar pakedin kullanıldığı uygulamalarda tanımlı olmalı ve uygulamadan yapılan çağrıda intent'le gelmeli, contracts adapter'da da item'lar bunlardan yaratılmalı(contractItem objeleri bu bilgileri içermeli). Hangisi tıklanırsa onun içeriği PrivacyPolicyDialogFragment yaratılırken kullanımalı ve bu fonksiyona da oradan aktarılmalı.
        viewModel.getAgreementContent(
            isProduction = true, // Test için false yapılmalı.
            contractor = "ELOGO",
            itemCode = "eBookTransfer",
            language = "TR",
            agreementType = "USEAGREEMENT"
        )
    }

    private fun sendApproveRequest() {
        // Bunların bir kısmı zaten contractItem içeriği, geri kalanı da yine istek atan uygulamadan gelmeli ve fragment oluşturulurken aktarılmalı.
        val uniqueInfo = "10.122.122.43|Tiger|LN1|1"
        val request = ApproveAgreementRequest(
            uniqueInfo = uniqueInfo,
            ipAddress = "10.122.122.143",
            contentHash = contentHash,
            extensionFields = "",
            itemCode = "eBookTransfer",
            agreementType = "USEAGREEMENT",
            contractor = "ELOGO",
            beginDate = "2024-01-01", // Bu bilgi nereden alınıyor?
            language = "TR",
            signedContentBase64Encoded = ""
        )
        val envelope = viewModel.createApproveAgreementEnvelope(request)
        viewModel.approveAgreementContent(
            isProduction = false, // Test için false yapılmalı.
            request = envelope
        )
    }

    private fun displayPdf(base64Content: String) {
        val pdfFile = base64ToPdf(base64Content, "temp.pdf")
        val parcelFileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)
        val pdfRenderer = PdfRenderer(parcelFileDescriptor)

        binding.pdfScrollView.visibility = View.VISIBLE
        binding.webView.visibility = View.GONE

        for (i in 0 until pdfRenderer.pageCount) {
            val page = pdfRenderer.openPage(i)
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            page.close()

            val imageView = android.widget.ImageView(requireContext())
            imageView.setImageBitmap(bitmap)
            imageView.adjustViewBounds = true
            binding.pdfContainer.addView(imageView)
        }

        pdfRenderer.close()
        parcelFileDescriptor.close()
        binding.llProgressBar.root.visibility = View.GONE
    }

    private fun base64ToPdf(base64Data: String, fileName: String): File {
        val decodedBytes = Base64.decode(base64Data, Base64.DEFAULT)
        val file = File(requireContext().cacheDir, fileName)
        file.outputStream().use {
            it.write(decodedBytes)
        }
        return file
    }

    private fun switchToWebViewToShowLocalFile() {
        val assetLoader = ContextUtils.getmContext()?.let { WebViewAssetLoader.AssetsPathHandler(it) }?.let {
            WebViewAssetLoader.Builder()
                .addPathHandler("/assets/", it)
                .build()
            }

        binding.pdfScrollView.visibility = View.GONE
        binding.webView.visibility = View.VISIBLE

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