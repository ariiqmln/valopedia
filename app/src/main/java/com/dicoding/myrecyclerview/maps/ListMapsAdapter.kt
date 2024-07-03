package com.dicoding.myrecyclerview.maps

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.myrecyclerview.R

class ListMapsAdapter(private val listMaps: List<Maps>) : RecyclerView.Adapter<ListMapsAdapter.MapViewHolder>()  {

    class MapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgMapbg: ImageView = itemView.findViewById(R.id.img_map)
        var tvName: TextView = itemView.findViewById(R.id.tv_map_name)
        var tvDescriptionThum: TextView = itemView.findViewById(R.id.tv_map_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_map, parent, false)
        return MapViewHolder(view)
    }

    override fun onBindViewHolder(holder: MapViewHolder, position: Int) {
        val map = listMaps[position]
        holder.imgMapbg.setImageResource(map.imageResourceId)
        holder.tvName.text = map.name
        holder.tvDescriptionThum.text = map.desctamnel
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailMaps::class.java).apply {
                putExtra("EXTRA_MAP_NAME", map.name)
                putExtra("EXTRA_MAP_DESC", map.description)
                putExtra("EXTRA_MAP_DESC_THUMNAIL", map.desctamnel)
                putExtra("EXTRA_MAP_IMAGE_BG", map.imageResourceId)
                putExtra("EXTRA_MAP_IMAGE", map.image2ResourceId)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listMaps.size

}
