package com.borshevskiy.foodapp.ui.fragments.foodjoke

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.borshevskiy.foodapp.R
import com.borshevskiy.foodapp.data.database.entities.FoodJokeEntity
import com.borshevskiy.foodapp.databinding.FragmentFoodJokeBinding
import com.borshevskiy.foodapp.util.Constants.Companion.API_KEY
import com.borshevskiy.foodapp.util.NetworkResult
import com.borshevskiy.foodapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodJokeFragment : Fragment() {

    private val mainViewModel by viewModels<MainViewModel>()

    private var _binding: FragmentFoodJokeBinding? = null
    private val binding get() = _binding!!

    private var foodJoke = "No Food Joke"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodJokeBinding.inflate(inflater,container,false)

        setHasOptionsMenu(true)

        mainViewModel.getFoodJoke(API_KEY)
        mainViewModel.foodJokeResponse.observe(viewLifecycleOwner, { response ->
            when(response) {
                is NetworkResult.Success -> {
                    with(binding) {
                        progressBar.visibility = View.INVISIBLE
                        foodJokeCardView.visibility = View.VISIBLE
                        foodJokeTextView.text = response.data?.text
                        if (response.data != null) {
                            foodJoke = response.data.text
                        }
                    }
                } is NetworkResult.Error -> {
                    with(binding) {
                        progressBar.visibility = View.INVISIBLE
                        foodJokeCardView.visibility = View.INVISIBLE
                        foodJokeErrorTextView.text = response.message.toString()
                    }
                    loadDataFromCache()
                    Toast.makeText(requireContext(),response.message.toString(),Toast.LENGTH_SHORT).show()
                } is NetworkResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.foodJokeCardView.visibility = View.INVISIBLE
                }
            }
        })
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.food_joke_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.share_food_joke_menu) {
            val shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, foodJoke)
                this.type = "text/plain"
            }
            startActivity(shareIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readFoodJoke.observe(viewLifecycleOwner, { database ->
                if (database != null) {
                    binding.foodJokeCardView.visibility = View.VISIBLE
                    binding.foodJokeTextView.text = database.first().foodJoke.text
                    foodJoke = database.first().foodJoke.text
                    if (database.isEmpty()) {
                        with(binding) {
                            foodJokeCardView.visibility = View.INVISIBLE
                            foodJokeErrorImageView.visibility = View.VISIBLE
                            foodJokeErrorTextView.visibility = View.VISIBLE
                        }
                    }
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}