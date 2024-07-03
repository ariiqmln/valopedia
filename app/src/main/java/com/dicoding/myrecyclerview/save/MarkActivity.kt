package com.dicoding.myrecyclerview.save

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.myrecyclerview.Hero
import com.dicoding.myrecyclerview.ListHeroAdapter
import com.dicoding.myrecyclerview.R
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

class MarkActivity : AppCompatActivity() {
    private lateinit var rvMarkHero: RecyclerView
    private val markHeroList = ArrayList<Hero>()
    private val savedVideoUrls = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_mark)

        rvMarkHero = findViewById(R.id.rv_marks)
        rvMarkHero.setHasFixedSize(true)
        rvMarkHero.layoutManager = LinearLayoutManager(this)

        val buttonback = findViewById<ImageView>(R.id.back_button)
        buttonback.setOnClickListener {
            finish()
        }

        val adapter = ListHeroAdapter(markHeroList, savedVideoUrls)
        rvMarkHero.adapter = adapter

        markHero()
    }

    private fun markHero() {
        val sharedPreferences = getSharedPreferences("mark_hero", MODE_PRIVATE)
        val entires = sharedPreferences.all
        val gson = Gson()

        val vidShared = getSharedPreferences("mark_video", MODE_PRIVATE)
        val vidEntires = vidShared.all

        for ((key, value) in entires) {
            if (value is String) {
                try {
                    val hero = gson.fromJson(value, Hero::class.java)
                    val videoUrl = vidEntires[key] as? String

                    if (videoUrl != null) {
                        markHeroList.add(hero)
                        savedVideoUrls.add(videoUrl)
                    } else {
                        Log.e("MarkActivity", "URL video tidak ditemukan: $key")
                    }
                } catch (e: JsonSyntaxException) {
                    Log.e("MarkActivity", "Error parsing JSON for key: $key")
                }
            } else {
                Log.e("MarkActivity", "Invalid data format for key: $key")
            }
        }
    }
}