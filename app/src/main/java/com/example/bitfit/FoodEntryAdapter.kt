package com.example.bitfit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.io.File

class FoodEntryAdapter(private val entries: MutableList<FoodEntry>) :
    RecyclerView.Adapter<FoodEntryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val foodNameTextView: TextView = view.findViewById(R.id.tvFoodName)
        val caloriesTextView: TextView = view.findViewById(R.id.tvCalories)
        val photoImageView: ImageView = view.findViewById(R.id.ivEntryPhoto)
        val accentBar: View = view.findViewById(R.id.viewAccentBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food_entry, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = entries[position]
        holder.foodNameTextView.text = entry.foodName
        holder.caloriesTextView.text = entry.calories.toString()

        if (!entry.photoPath.isNullOrEmpty() && File(entry.photoPath).exists()) {
            holder.photoImageView.visibility = View.VISIBLE
            holder.accentBar.visibility = View.GONE
            Glide.with(holder.itemView.context)
                .load(File(entry.photoPath))
                .centerCrop()
                .into(holder.photoImageView)
        } else {
            holder.photoImageView.visibility = View.GONE
            holder.accentBar.visibility = View.VISIBLE
        }
    }

    override fun getItemCount() = entries.size

    fun updateData(newEntries: List<FoodEntry>) {
        entries.clear()
        entries.addAll(newEntries)
        notifyDataSetChanged()
    }
}