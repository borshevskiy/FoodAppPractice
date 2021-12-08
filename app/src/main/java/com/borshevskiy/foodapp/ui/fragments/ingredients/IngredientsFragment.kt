package com.borshevskiy.foodapp.ui.fragments.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.borshevskiy.foodapp.R
import com.borshevskiy.foodapp.adapters.IngredientsAdapter
import com.borshevskiy.foodapp.databinding.FragmentIngridientsBinding
import com.borshevskiy.foodapp.models.Result
import com.borshevskiy.foodapp.util.Constants.Companion.RECIPE_RESULT_KEY

class IngredientsFragment : Fragment() {

    private val mAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }
    private var _binding: FragmentIngridientsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngridientsBinding.inflate(inflater,container,false)

        val args = arguments
        val myBundle: Result? = args?.getParcelable(RECIPE_RESULT_KEY)

        setupRecyclerView()
        myBundle?.extendedIngredients?.let { mAdapter.setData(it) }


        return binding.root
    }

    private fun setupRecyclerView() {
        binding.ingredientsRecyclerview.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}