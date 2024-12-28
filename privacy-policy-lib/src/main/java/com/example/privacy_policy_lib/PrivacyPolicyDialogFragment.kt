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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.webkit.WebViewAssetLoader
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
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        viewModel.agreementContent.observe(viewLifecycleOwner) { content ->
            if (!content.isNullOrEmpty()) {
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
    }

    private fun loadPrivacyPolicy() {
        binding.llProgressBar.root.visibility = View.VISIBLE
        viewModel.getAgreementContent(
            isProduction = true,
            contractor = "ELOGO",
            itemCode = "eBookTransfer",
            language = "TR",
            agreementType = "USEAGREEMENT"
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