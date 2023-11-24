package com.borshevskiy.foodapp.presentation.fragments.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import coil.load
import com.borshevskiy.foodapp.R
import com.borshevskiy.foodapp.databinding.FragmentOverviewBinding
import com.borshevskiy.foodapp.domain.models.Food
import com.borshevskiy.foodapp.util.Constants.Companion.RECIPE_RESULT_KEY
import org.jsoup.Jsoup

class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(inflater,container,false)

        val args = arguments
        val myBundle: Food? = args?.getParcelable(RECIPE_RESULT_KEY)

        with(binding) {
            mainImageView.load(myBundle?.image)
            titleTextView.text = myBundle?.title
            likesTextView.text = myBundle?.aggregateLikes.toString()
            timeTextView.text = myBundle?.readyInMinutes.toString()
            summaryTextView.text = Jsoup.parse(myBundle?.summary).text()

            if (myBundle?.vegetarian == true) {
                vegetarianTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                vegetarianImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            }
            if (myBundle?.vegan == true) {
                veganTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
                veganImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            }
            if (myBundle?.glutenFree == true) {
                glutenFreeTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
                glutenFreeImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            }
            if (myBundle?.dairyFree == true) {
                dairyFreeTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
                dairyFreeImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            }
            if (myBundle?.veryHealthy == true) {
                healthyTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
                healthyImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            }
            if (myBundle?.cheap == true) {
                cheapTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
                cheapImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}