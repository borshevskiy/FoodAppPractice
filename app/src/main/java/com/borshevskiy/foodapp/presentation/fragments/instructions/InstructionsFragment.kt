package com.borshevskiy.foodapp.presentation.fragments.instructions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.borshevskiy.foodapp.databinding.FragmentInstructionsBinding
import com.borshevskiy.foodapp.domain.models.Food
import com.borshevskiy.foodapp.util.Constants

class InstructionsFragment : Fragment() {

    private var _binding: FragmentInstructionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInstructionsBinding.inflate(inflater,container,false)

        val args = arguments
        val myBundle: Food? = args?.getParcelable(Constants.RECIPE_RESULT_KEY)

        with(binding) {
            instructionsWebView.webViewClient = object : WebViewClient() {}
            val webSiteUrl: String = myBundle!!.sourceUrl
            instructionsWebView.loadUrl(webSiteUrl)
        }

        return binding.root
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}