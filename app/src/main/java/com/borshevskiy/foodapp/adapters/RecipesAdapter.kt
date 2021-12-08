package com.borshevskiy.foodapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.borshevskiy.foodapp.R
import com.borshevskiy.foodapp.R.color.green
import com.borshevskiy.foodapp.databinding.RecipesRowLayoutBinding
import com.borshevskiy.foodapp.models.FoodRecipe
import com.borshevskiy.foodapp.models.Result
import com.borshevskiy.foodapp.ui.fragments.recipes.RecipesFragmentDirections
import com.borshevskiy.foodapp.util.RecipesDiffUtil
import org.jsoup.Jsoup

class RecipesAdapter: RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {

    private var recipesList = emptyList<Result>()

    class MyViewHolder(var binding: RecipesRowLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RecipesRowLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentRecipe = recipesList[position]
        val context = holder.itemView.context
        with(holder.binding) {
            titleTextView.text = currentRecipe.title
            descriptionTextView.text = Jsoup.parse(currentRecipe.summary).text()
            likesTextView.text = currentRecipe.aggregateLikes.toString()
            clockTextView.text = currentRecipe.readyInMinutes.toString()
            recipeImageView.load(currentRecipe.image) { crossfade(600)
            error(R.drawable.ic_error_placeholder)}
            if(currentRecipe.vegan) {
                veganTextView.setTextColor(getColor(context, green))
                veganImageView.setColorFilter(getColor(context, green))
            }
            recipesRowLayout.setOnClickListener {
                try {
                    val action = RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(currentRecipe)
                    recipesRowLayout.findNavController().navigate(action)
                }catch (e: Exception) { }
            }
        }
    }

    override fun getItemCount(): Int {
        return recipesList.size
    }

    fun setData(newData: FoodRecipe) {
        val recipesDiffUtil = RecipesDiffUtil(recipesList,newData.results)
        val diffUtil = DiffUtil.calculateDiff(recipesDiffUtil)
        recipesList = newData.results
        diffUtil.dispatchUpdatesTo(this)
    }
}