package com.borshevskiy.foodapp.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.borshevskiy.foodapp.R
import com.borshevskiy.foodapp.data.database.entities.FavoritesEntity
import com.borshevskiy.foodapp.databinding.FavoriteRecipesRowLayoutBinding
import com.borshevskiy.foodapp.ui.fragments.favorites.FavouriteRecipesFragmentDirections
import com.borshevskiy.foodapp.util.RecipesDiffUtil
import com.borshevskiy.foodapp.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import org.jsoup.Jsoup

class FavoriteRecipesAdapter(private val reqActivity: FragmentActivity, private val mainViewModel: MainViewModel):
    RecyclerView.Adapter<FavoriteRecipesAdapter.MyViewHolder>(), ActionMode.Callback {

    private var multiSelection = false
    private var selectedRecipes = arrayListOf<FavoritesEntity>()
    private var myViewHolders = arrayListOf<MyViewHolder>()

    private lateinit var mActionMode: ActionMode
    private lateinit var rootView: View

    private var favoriteRecipesList = emptyList<FavoritesEntity>()

    class MyViewHolder(var binding: FavoriteRecipesRowLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(FavoriteRecipesRowLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        myViewHolders.add(holder)
        rootView = holder.itemView.rootView
        val currentRecipe = favoriteRecipesList[position]
        val context = holder.itemView.context
        with(holder.binding) {
            favoriteTitleTextView.text = currentRecipe.result.title
            favoriteDescriptionTextView.text = Jsoup.parse(currentRecipe.result.summary).text()
            favoriteLikesTextView.text = currentRecipe.result.aggregateLikes.toString()
            favoriteClockTextView.text = currentRecipe.result.readyInMinutes.toString()
            favoriteRecipeImageView.load(currentRecipe.result.image) { crossfade(600)
                error(R.drawable.ic_error_placeholder)}
            if(currentRecipe.result.vegan) {
                favoriteVeganTextView.setTextColor(ContextCompat.getColor(context, R.color.green))
                favoriteVeganImageView.setColorFilter(ContextCompat.getColor(context, R.color.green))
            }
            favoriteRecipesRowLayout.setOnClickListener {
                if (multiSelection) {
                    applySelection(holder, currentRecipe)
                } else {
                    val action = FavouriteRecipesFragmentDirections.actionFavouriteRecipesFragmentToDetailsActivity(currentRecipe.result)
                    favoriteRecipesRowLayout.findNavController().navigate(action)
                }
            }
            favoriteRecipesRowLayout.setOnLongClickListener {
                if (!multiSelection) {
                    multiSelection = true
                    reqActivity.startActionMode(this@FavoriteRecipesAdapter)
                    applySelection(holder,currentRecipe)
                    true
                } else {
                    multiSelection = false
                    false
                }
            }
        }
    }

    private fun applySelection(holder: MyViewHolder, currentRecipe: FavoritesEntity) {
        if(selectedRecipes.contains(currentRecipe)) {
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
            applyActionModeTitle()
        } else {
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimary)
            applyActionModeTitle()
        }
    }

    private fun changeRecipeStyle(holder: MyViewHolder, backGroundColor: Int, strokeColor: Int) {
        with(holder.binding) {
            favoriteRecipesRowLayout.setBackgroundColor(
                ContextCompat.getColor(reqActivity, backGroundColor))
            favoriteRowCardView.strokeColor = ContextCompat.getColor(reqActivity, strokeColor)
        }
    }

    override fun getItemCount(): Int {
        return favoriteRecipesList.size
    }

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.favorites_contextual_menu, menu)
        mActionMode = actionMode!!

        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    private fun applyActionModeTitle() {
        when(selectedRecipes.size) {
            0 -> mActionMode.finish()
            1 -> mActionMode.title = "${selectedRecipes.size} item selected"
            else -> mActionMode.title = "${selectedRecipes.size} items selected"
        }
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        if (menu?.itemId == R.id.delete_favorite_recipe_menu) {
            selectedRecipes.forEach {
                mainViewModel.deleteFavoriteRecipe(it)
            }
            showSnackBar("${selectedRecipes.size} recipe(s) removed.")
            multiSelection = false
            selectedRecipes.clear()
            actionMode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        myViewHolders.forEach { holder ->
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        }
        multiSelection = false
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyStatusBarColor(color: Int) {
        reqActivity.window.statusBarColor = ContextCompat.getColor(reqActivity,color)
    }

    fun setData(newData: List<FavoritesEntity>) {
        val recipesDiffUtil = RecipesDiffUtil(favoriteRecipesList,newData)
        val diffUtil = DiffUtil.calculateDiff(recipesDiffUtil)
        favoriteRecipesList = newData
        diffUtil.dispatchUpdatesTo(this)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).setAction("Okay") {}.show()
    }

    fun clearContextualActionMode() {
        if (this::mActionMode.isInitialized) {
            mActionMode.finish()
        }
    }
}