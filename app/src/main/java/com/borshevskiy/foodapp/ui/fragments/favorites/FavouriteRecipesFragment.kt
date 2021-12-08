package com.borshevskiy.foodapp.ui.fragments.favorites

import android.os.Bundle
import android.os.Message
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.borshevskiy.foodapp.R
import com.borshevskiy.foodapp.adapters.FavoriteRecipesAdapter
import com.borshevskiy.foodapp.databinding.FragmentFavouriteRecipesBinding
import com.borshevskiy.foodapp.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteRecipesFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()
    private val mAdapter: FavoriteRecipesAdapter by lazy { FavoriteRecipesAdapter(requireActivity(),mainViewModel) }

    private var _binding: FragmentFavouriteRecipesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteRecipesBinding.inflate(inflater,container,false)
        setHasOptionsMenu(true)
        setupRecyclerView()
        mainViewModel.readFavoriteRecipes.observe(viewLifecycleOwner, { database ->
            if (database.isNotEmpty()) {
                mAdapter.setData(database)
            } else {
                with(binding) {
                    noDataImageView.visibility = View.VISIBLE
                    noDataTextView.visibility  = View.VISIBLE
                }
            }
        })
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_recipes_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deleteAll_favorite_recipes_menu) {
            mainViewModel.deleteAllFavoriteRecipes()
            showSnackBar()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        mAdapter.clearContextualActionMode()
    }

    private fun setupRecyclerView() {
        binding.favoriteRecipesRecyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun showSnackBar() {
        Snackbar.make(binding.root,"All recipes removed.",Snackbar.LENGTH_LONG).setAction("Okay"){}.show()
    }
}