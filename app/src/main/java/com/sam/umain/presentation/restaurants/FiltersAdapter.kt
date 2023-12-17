package com.sam.umain.presentation.restaurants

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.sam.umain.R
import com.sam.umain.data.model.FilterModel
import com.sam.umain.databinding.ItemFilterBinding

class FiltersAdapter(private val onClick: (filterId: String) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context

    private var oldList: ArrayList<FilterModel> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return Item(
            ItemFilterBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_filter,
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

    inner class Item(private val binding: ItemFilterBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun onBind(position: Int) {
            val filter = oldList[position]

            binding.tvFilter.text = filter.name
            binding.ivFilter.load(filter.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_foreground)
            }

            if (filter.isSelected) {
                binding.filterCardView.setCardBackgroundColor(
                    ContextCompat.getColor(context, R.color.selectedColor)
                )
                binding.tvFilter.setTextColor(
                    ContextCompat.getColor(context, R.color.white)
                )
            }else{
                binding.filterCardView.setCardBackgroundColor(
                    ContextCompat.getColor(context, R.color.white)
                )
                binding.tvFilter.setTextColor(
                    ContextCompat.getColor(context, R.color.black)
                )
            }



            binding.root.setOnClickListener {
                onClick(oldList[position].id.toString())
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(filtersHashMap: HashMap<String, FilterModel>) {
        oldList.clear()

        filtersHashMap.forEach { (_, filterModel) ->
            oldList.add(filterModel)
        }
        notifyDataSetChanged()
    }
}