package com.lostandfoundapp

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdvertAdapter(private var advertList: List<Advert>) : RecyclerView.Adapter<AdvertAdapter.AdvertViewHolder>() {

    class AdvertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRowType: TextView = itemView.findViewById(R.id.tvRowType)
        val tvRowName: TextView = itemView.findViewById(R.id.tvRowName)
        val tvRowDate: TextView = itemView.findViewById(R.id.tvRowDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_advert_row, parent, false)
        return AdvertViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdvertViewHolder, position: Int) {
        val currentItem = advertList[position]

        holder.tvRowType.text = currentItem.postType
        holder.tvRowName.text = currentItem.name
        holder.tvRowDate.text = currentItem.date


        if (currentItem.postType == "Lost") {
            holder.tvRowType.setTextColor(Color.parseColor("#D32F2F")) // Red
        } else {
            holder.tvRowType.setTextColor(Color.parseColor("#388E3C")) // Green
        }


        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ItemDetailActivity::class.java).apply {
                putExtra("ADVERT_ID", currentItem.id)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return advertList.size
    }


    fun updateData(newList: List<Advert>) {
        advertList = newList
        notifyDataSetChanged()
    }
}