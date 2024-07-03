package com.dicoding.myrecyclerview.maps

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.myrecyclerview.R

class DetailMaps : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_map)

        val actionbar = supportActionBar
        actionbar?.title = "Detail Map"
        actionbar?.setDisplayHomeAsUpEnabled(true)

        val imgMapbg: ImageView = findViewById(R.id.map_bg)
        val imgMap: ImageView = findViewById(R.id.map_pic)
        val tvName: TextView = findViewById(R.id.map_name)
        val tvDescription: TextView = findViewById(R.id.map_desc)

        val name = intent.getStringExtra("EXTRA_MAP_NAME") ?: ""
        val description = intent.getStringExtra("EXTRA_MAP_DESC") ?: ""
        val imageResourceId = intent.getIntExtra("EXTRA_MAP_IMAGE_BG", 0)
        val image2ResourceId = intent.getIntExtra("EXTRA_MAP_IMAGE", 0)

        tvName.text = name
        tvDescription.text = description
        imgMapbg.setImageResource(imageResourceId)
        imgMap.setImageResource(image2ResourceId)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}