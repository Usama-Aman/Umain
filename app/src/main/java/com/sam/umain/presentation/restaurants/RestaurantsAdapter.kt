package com.sam.umain.presentation.restaurants

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.sam.umain.R
import com.sam.umain.data.model.FilterModel
import com.sam.umain.data.model.Restaurant
import com.sam.umain.databinding.ItemRestaurantsBinding

class RestaurantsAdapter(private val onClick: (Int, String, View) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context
    private var oldList: List<Restaurant> = ArrayList()
    private var filters = HashMap<String, FilterModel>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return Item(
            ItemRestaurantsBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_restaurants,
                    parent,
                    false
                )
            )
        )
    }

    override fun getItemCount(): Int = oldList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is Item)
            holder.onBind(position)
    }

    inner class Item(private val binding: ItemRestaurantsBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun onBind(position: Int) {
            val restaurant = oldList[position]
            binding.ivRestaurant.load(restaurant.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_foreground)
            }

            var filterTags = ""
//            restaurant.filterIds?.joinToString()
            restaurant.filterIds?.forEachIndexed { index, s ->
                filterTags += filters[s]?.name
                if (index < (restaurant.filterIds?.size?.minus(1) ?: 0))
                    filterTags += context.resources.getString(R.string.dot)
            }
            binding.tvTags.text = filterTags
            binding.tvTitle.text = restaurant.name
            binding.tvRating.text = restaurant.rating.toString()
            binding.tvTime.text = context.getString(R.string.deliveryTIme, restaurant.deliveryTimeMinutes.toString())

            binding.root.setOnClickListener {
                onClick(position, binding.tvTags.text.toString(), binding.root)
            }
        }
    }

    fun setData(newList: MutableList<Restaurant>, filtersHashMap: HashMap<String, FilterModel>) {
        filters = filtersHashMap
        val diffUtil = RestaurantsDiffUtil(oldList, newList.toList())
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        oldList = newList.toList()
        diffResult.dispatchUpdatesTo(this)
    }
}

/*
* Diff util is used to update the list efficiently
* */
class RestaurantsDiffUtil(
    private val oldList: List<Restaurant>,
    private val newList: List<Restaurant>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}