package com.borshevskiy.foodapp.presentation.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.borshevskiy.foodapp.R
import com.borshevskiy.foodapp.databinding.ActivityDetailsBinding
import com.borshevskiy.foodapp.presentation.adapters.PagerAdapter
import com.borshevskiy.foodapp.presentation.fragments.ingredients.IngredientsFragment
import com.borshevskiy.foodapp.presentation.fragments.instructions.InstructionsFragment
import com.borshevskiy.foodapp.presentation.fragments.overview.OverviewFragment
import com.borshevskiy.foodapp.presentation.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()

    private var recipeSaved = false
    private var saveRecipeId = 0

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments: ArrayList<Fragment> = arrayListOf(OverviewFragment(),IngredientsFragment(),InstructionsFragment())
        val titles: ArrayList<String> = arrayListOf("Overview", "Ingredients", "Instructions")
        val resultBundle = Bundle()
        resultBundle.putParcelable("recipeBundle",args.result)
        with(binding) {
            viewPager.adapter = PagerAdapter(resultBundle,fragments,titles,supportFragmentManager)
            tabLayout.setupWithViewPager(viewPager)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu) = run {
        menuInflater.inflate(R.menu.details_menu,menu)
        checkSavedRecipes(menu.findItem(R.id.save_to_favorites_menu))
        true
    }

    private fun checkSavedRecipes(menuItem: MenuItem) {
        mainViewModel.readFavoriteRecipes.observe(this) { favoritesEntity ->
            try {
                for (savedRecipe in favoritesEntity) {
                    if (savedRecipe.id == args.result.id) {
                        changeMenuColor(menuItem, R.color.yellow)
                        saveRecipeId = savedRecipe.id
                        recipeSaved = true
                    } else changeMenuColor(menuItem, R.color.white)
                }
            } catch (_: Exception) { }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            finish()
        } else if(item.itemId == R.id.save_to_favorites_menu && !recipeSaved) {
            saveToFavorites(item)
        } else if(item.itemId == R.id.save_to_favorites_menu && recipeSaved) {
            removeFromFavorites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveToFavorites(item: MenuItem) {
        mainViewModel.insertFavoriteRecipe(args.result)
        changeMenuColor(item,R.color.yellow)
        showSnackBar("Recipe saved.")
        recipeSaved = true
    }

    private fun removeFromFavorites(item: MenuItem) {
        mainViewModel.deleteFavoriteRecipe(saveRecipeId, args.result)
        changeMenuColor(item,R.color.white)
        showSnackBar("Removed from favorites.")
        recipeSaved = false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.detailsLayout,message,Snackbar.LENGTH_LONG).setAction("Okay"){}.show()
    }

    private fun changeMenuColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this,color))
    }
}