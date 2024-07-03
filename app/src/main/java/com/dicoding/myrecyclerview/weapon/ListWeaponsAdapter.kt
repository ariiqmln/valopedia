package com.dicoding.myrecyclerview.weapon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.myrecyclerview.R


class ListWeaponsAdapter (private val listWepon: List<Weapons>) : RecyclerView.Adapter<ListWeaponsAdapter.WeaponViewHolder>(){

    class WeaponViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgWeaponbg: ImageView = itemView.findViewById(R.id.image_weapon)
        var tvName: TextView = itemView.findViewById(R.id.nameweapon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeaponViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_weapon, parent, false)
        return WeaponViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeaponViewHolder, position: Int) {
        val weapons = listWepon[position]
        holder.imgWeaponbg.setImageResource(weapons.photoweapons)
        holder.tvName.text = weapons.namaweapon
    }

    override fun getItemCount(): Int = listWepon.size
}