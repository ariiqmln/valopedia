package com.dicoding.myrecyclerview

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListHeroAdapter(private val listHero: ArrayList<Hero>, private val videoDescriptions: List<String>) : RecyclerView.Adapter<ListHeroAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_hero, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val context = holder.itemView.context
        val (name, description, photo, role, origin, bg, tamnel,soundagent,vidiodeskripsi) = listHero[position]
        holder.imgPhoto.setImageResource(photo)
        holder.tvName.text = name
        holder.tvDescription.text = tamnel
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailHero::class.java).also {
                it.putExtra("VIDEO_URL", videoDescriptions[position]) // Kirim URL video
                it.putExtra(DetailHero.extraname,name)
                it.putExtra(DetailHero.extradesc,description)
                it.putExtra(DetailHero.extrapic,photo)
                it.putExtra(DetailHero.extrabg,bg)
                it.putExtra(DetailHero.extrarole, role)
                it.putExtra(DetailHero.extraorigin, origin)
                it.putExtra(DetailHero.extrasound, soundagent)
                it.putExtra(DetailHero.extrasvidiodeskripsi, vidiodeskripsi)
                it.putExtra(DetailHero.extratamnel, tamnel)
            }

            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int = listHero.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        val tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_item_description)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Hero)
    }
}