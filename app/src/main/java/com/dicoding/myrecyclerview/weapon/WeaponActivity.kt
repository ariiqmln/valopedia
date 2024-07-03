package com.dicoding.myrecyclerview.weapon

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.myrecyclerview.Author
import com.dicoding.myrecyclerview.MainMenuActivity
import com.dicoding.myrecyclerview.R
import com.dicoding.myrecyclerview.maps.ListMapsAdapter
import com.dicoding.myrecyclerview.maps.Maps
import java.util.Locale

class WeaponActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var rvWeapons: RecyclerView
    private val listWeapons = ArrayList<Weapons>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_weapon)

        searchView = findViewById(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })

        val buttonback = findViewById<ImageView>(R.id.back_button)
        buttonback.setOnClickListener {
            startActivity(Intent(this, MainMenuActivity::class.java))
        }

        rvWeapons = findViewById(R.id.rv_weapons)
        rvWeapons.setHasFixedSize(true)

        listWeapons.addAll(getListWeapons())
        Log.d("MapsActivity", "List Maps: ${listWeapons.size}") // Cek ukuran list Maps

        showRecyclerList()
    }

    private fun filterList(query: String?) {
        val filteredListWeapons = if (query.isNullOrEmpty()) {
            listWeapons
        } else {
            listWeapons.filter { heroes ->
                heroes.namaweapon.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))
            }
        }
        // Update RecyclerView with filtered list
        val adapter = ListWeaponsAdapter(filteredListWeapons)
        rvWeapons.adapter = adapter
    }

    private fun getListWeapons(): ArrayList<Weapons> {
        val dataImageWeapons = resources.obtainTypedArray(R.array.image_weapon)
        val dataNamaWeapons = resources.getStringArray(R.array.nama_weapons)

        val listWeapons = ArrayList<Weapons>()
        val size = minOf(dataNamaWeapons.size, dataImageWeapons.length())

        for (i in 0 until size) {
            val wepons = Weapons(
                dataNamaWeapons[i],
                dataImageWeapons.getResourceId(i,-1)
            )
            listWeapons.add(wepons)
        }
        dataImageWeapons.recycle()
        return listWeapons
    }

    private fun showRecyclerList() {
        rvWeapons.layoutManager = LinearLayoutManager(this)
        val adapter = ListWeaponsAdapter(listWeapons)
        rvWeapons.adapter = adapter
    }
}