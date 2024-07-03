package com.dicoding.myrecyclerview.maps

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.myrecyclerview.Author
import com.dicoding.myrecyclerview.Hero
import com.dicoding.myrecyclerview.MainMenuActivity
import com.dicoding.myrecyclerview.R
import java.util.Locale

class MapsActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var rvMaps: RecyclerView
    private val listMaps = ArrayList<Maps>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

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

        val prfileImage: ImageView = findViewById(R.id.foto_about)
        prfileImage.setOnClickListener {
            val intent = Intent(this, Author::class.java)
            startActivity(intent)
        }

        rvMaps = findViewById(R.id.rv_hero)
        rvMaps.setHasFixedSize(true)

        listMaps.addAll(getListMaps())
        Log.d("MapsActivity", "List Maps: ${listMaps.size}") // Cek ukuran list Maps

        showRecyclerList()
    }

    private fun filterList(query: String?) {
        val filteredListMaps = if (query.isNullOrEmpty()) {
            listMaps
        } else {
            listMaps.filter { heroes ->
                heroes.name.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))
            }
        }
        // Update RecyclerView with filtered list
        val adapter = ListMapsAdapter(filteredListMaps)
        rvMaps.adapter = adapter
    }

    private fun getListMaps(): ArrayList<Maps> {
        val dataPhotoBg = resources.obtainTypedArray(R.array.image_maps_bg)
        val dataPhoto = resources.obtainTypedArray(R.array.image_maps)
        val dataName = resources.getStringArray(R.array.nama_map)
        val dataDescription = resources.getStringArray(R.array.desc_map)
        val dataDescriptionThum = resources.getStringArray(R.array.desc_map_tamnel)

        val listMaps = ArrayList<Maps>()
        val size = minOf(dataPhotoBg.length(), dataPhoto.length(), dataName.size, dataDescription.size)

        for (i in 0 until size) {
            val map = Maps(
                dataPhotoBg.getResourceId(i, -1),
                dataPhoto.getResourceId(i, -1),
                dataName[i],
                dataDescription[i],
                dataDescriptionThum[i]
            )
            listMaps.add(map)
        }

        dataPhotoBg.recycle()
        dataPhoto.recycle()

        return listMaps
    }

    private fun showRecyclerList() {
        rvMaps.layoutManager = LinearLayoutManager(this)
        val adapter = ListMapsAdapter(listMaps)
        rvMaps.adapter = adapter
    }
}