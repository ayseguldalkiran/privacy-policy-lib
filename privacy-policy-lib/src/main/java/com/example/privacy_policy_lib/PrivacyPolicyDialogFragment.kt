package com.example.privacy_policy_lib

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ParcelFileDescriptor
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.webkit.WebViewAssetLoader
import com.example.privacy_policy_lib.core.AgreementTypes
import com.example.privacy_policy_lib.core.model.ApproveAgreementRequest
import com.example.privacy_policy_lib.core.model.PrivacyPolicyState
import com.example.privacy_policy_lib.core.model.PrivacyPolicyState.IS_APPROVED
import com.example.privacy_policy_lib.core.model.PrivacyPolicyState.POSITION
import com.example.privacy_policy_lib.core.model.PrivacyPolicyState.PRIVACY_POLICY
import com.example.privacy_policy_lib.core.utils.ContextUtils
import com.example.privacy_policy_lib.core.utils.IntentExtraName
import com.example.privacy_policy_lib.core.utils.PreferencesHelper
import com.example.privacy_policy_lib.databinding.FragmentPrivacyPolicyDialogBinding
import java.io.File

class PrivacyPolicyDialogFragment : Fragment() {
    private var _binding: FragmentPrivacyPolicyDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PrivacyPolicyViewModel by viewModels()
    private var mPrivacyPolicyFile: String? = null
    private var contentHash = ""
    private var beginDate = ""
    private var checkboxPosition: Int = 0

    companion object {
        @JvmStatic
        fun newInstance(
            privacyPolicyFile: String,
            position: Int
        ): PrivacyPolicyDialogFragment {
            return PrivacyPolicyDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(IntentExtraName.ARG_FILE, privacyPolicyFile)
                    putInt(IntentExtraName.ARG_POSITION, position)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mPrivacyPolicyFile = it.getString(IntentExtraName.ARG_FILE)
            checkboxPosition = it.getInt(IntentExtraName.ARG_POSITION)
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
        binding.privacyPolicyBtnRead.setOnClickListener {
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
                val agreementType = PrivacyPolicyState.params.agreementTypes[checkboxPosition]
                contentHash = response.body?.contentResponse?.result?.contentHash ?: ""
                beginDate = response.body?.contentResponse?.result?.beginDate ?: ""
                updateContentHashList(agreementType)
                val endDate = response.body?.contentResponse?.result?.endDate ?: ""
                updateEndDateList(agreementType, endDate)
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
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            val result = Bundle().apply {
                putInt(POSITION, checkboxPosition)
                putBoolean(IS_APPROVED, false)
            }
            parentFragmentManager.setFragmentResult(PRIVACY_POLICY, result)
            parentFragmentManager.popBackStack()
        }
        viewModel.approvalResult.observe(viewLifecycleOwner) { response ->
            response?.let { approveResponse ->
                val agreementType = PrivacyPolicyState.params.agreementTypes[checkboxPosition]
                val newToken = approveResponse.body?.approveAgreementContentResponse?.approveAgreementContentResult?.agreementToken ?: ""
                val existingIndex = PrivacyPolicyState.params.agreementTokenList.indexOfFirst { it.first == agreementType }
                if (existingIndex != -1) {
                    PrivacyPolicyState.params.agreementTokenList[existingIndex] = Pair(agreementType, newToken)
                } else {
                    PrivacyPolicyState.params.agreementTokenList.add(Pair(agreementType, newToken))
                }
            }
            val result = Bundle().apply {
                putInt(POSITION, checkboxPosition)
                putBoolean(IS_APPROVED, true)
            }
            parentFragmentManager.setFragmentResult(PRIVACY_POLICY, result)
            parentFragmentManager.popBackStack()
        }
    }

    private fun updateContentHashList(agreementType: AgreementTypes) {
        val existingIndex = PrivacyPolicyState.params.contentHashList.indexOfFirst { it.first == agreementType }
        if (existingIndex != -1) {
            PrivacyPolicyState.params.contentHashList[existingIndex] = Pair(agreementType, contentHash)
        } else {
            PrivacyPolicyState.params.contentHashList.add(Pair(agreementType, contentHash))
        }
    }


    private fun updateEndDateList(agreementType: AgreementTypes, endDate: String) {
        val existingIndex = PrivacyPolicyState.params.endDateList.indexOfFirst { it.first == agreementType }
        if (existingIndex != -1) {
            PrivacyPolicyState.params.endDateList[existingIndex] = Pair(agreementType, endDate)
        } else {
            PrivacyPolicyState.params.endDateList.add(Pair(agreementType, endDate))
        }
    }

    private fun loadPrivacyPolicy() {
        binding.llProgressBar.root.visibility = View.VISIBLE
        viewModel.getAgreementContent(checkboxPosition)
    }

    private fun sendApproveRequest() {
        val uniqueInfo = PrivacyPolicyState.params.server + "|" + PrivacyPolicyState.params.erpType + "|" + PrivacyPolicyState.params.userName + "|" + PrivacyPolicyState.params.password
        val request = ApproveAgreementRequest(
            uniqueInfo = uniqueInfo,
            ipAddress = PrivacyPolicyState.params.server,
            contentHash = contentHash,
            extensionFields = "",
            itemCode = PrivacyPolicyState.params.itemCode,
            agreementType = PrivacyPolicyState.params.agreementTypes[checkboxPosition].toString(),
            contractor = PrivacyPolicyState.params.contractor,
            beginDate = beginDate,
            language = PrivacyPolicyState.params.language,
            signedContentBase64Encoded = ""
        )
        val envelope = viewModel.createApproveAgreementEnvelope(request)
        viewModel.approveAgreementContent(
            isProduction = PrivacyPolicyState.params.isProduction, // Test için false yapılmalı.
            request = envelope
        )
    }

    private fun displayPdf(base64Content: String) {
        val pdfFile = base64ToPdf(base64Content, "temp.pdf")
        val parcelFileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)
        val pdfRenderer = PdfRenderer(parcelFileDescriptor)

        binding.privacyPolicyScrollView.visibility = View.VISIBLE
        binding.privacyPolicyWebView.visibility = View.GONE

        for (i in 0 until pdfRenderer.pageCount) {
            val page = pdfRenderer.openPage(i)
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            page.close()

            val imageView = android.widget.ImageView(requireContext())
            imageView.setImageBitmap(bitmap)
            imageView.adjustViewBounds = true
            binding.privacyPolicyPdfContainer.addView(imageView)
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

        binding.privacyPolicyScrollView.visibility = View.GONE
        binding.privacyPolicyWebView.visibility = View.VISIBLE

        binding.privacyPolicyWebView.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
                return assetLoader?.shouldInterceptRequest(request.url)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                binding.llProgressBar.root.visibility = View.GONE
                delayReadButton()
                super.onPageFinished(view, url)
            }
        }

        mPrivacyPolicyFile?.let { binding.privacyPolicyWebView.loadUrl(it) }
    }

    private fun delayReadButton() {
        val safeBinding = _binding
        if (safeBinding != null) {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.privacyPolicyBtnRead.visibility = View.VISIBLE
            }, 1000)
        }
    }
}