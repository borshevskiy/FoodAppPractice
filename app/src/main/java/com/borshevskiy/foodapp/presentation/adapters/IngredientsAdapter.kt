package com.borshevskiy.foodapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.borshevskiy.foodapp.R
import com.borshevskiy.foodapp.databinding.IngredientsRowLayoutBinding
import com.borshevskiy.foodapp.domain.models.ExtendedIngredient
import com.borshevskiy.foodapp.util.Constants.Companion.BASE_IMAGE_URL
import com.borshevskiy.foodapp.util.RecipesDiffUtil

class IngredientsAdapter: RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {

    private var ingredientsList = emptyList<ExtendedIngredient>()

    class MyViewHolder(val binding: IngredientsRowLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(IngredientsRowLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var currentIngredient = ingredientsList[position]
        with(holder.binding) {
            ingredientImageView.load(BASE_IMAGE_URL + currentIngredient.image) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
            ingredientName.text = currentIngredient.name.capitalize()
            ingredientAmount.text = currentIngredient.amount.toString()
            ingredientUnit.text = currentIngredient.unit
            ingredientConsistency.text = currentIngredient.consistency
            ingredientOriginal.text = currentIngredient.original
        }
    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    fun setData(ingredients: List<ExtendedIngredient>) {
        val recipesDiffUtil = RecipesDiffUtil(ingredientsList,ingredients)
        val diffUtil = DiffUtil.calculateDiff(recipesDiffUtil)
        ingredientsList = ingredients
        diffUtil.dispatchUpdatesTo(this)
    }
}