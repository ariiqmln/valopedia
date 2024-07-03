package com.dicoding.myrecyclerview

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.myrecyclerview.databinding.ActivityMainMenuBinding
import com.dicoding.myrecyclerview.maps.MapsActivity
import com.dicoding.myrecyclerview.save.MarkActivity
import com.dicoding.myrecyclerview.weapon.WeaponActivity

class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainMenuBinding // Perbarui tipe binding ini

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            ActivityMainMenuBinding.inflate(layoutInflater) // Perbarui ini untuk menggunakan ActivityMenuBinding
        setContentView(binding.root)
        setAction()

        val buttonback =findViewById<ImageView>(R.id.back_button)
        buttonback.setOnClickListener{
            startActivity(Intent(this,WelcomeActivity::class.java))
        }
    }

    private fun setAction() {
        binding.info.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.maps.setOnClickListener {
            startActivity(Intent(this,MapsActivity::class.java))
        }

        binding.weapons.setOnClickListener {
            startActivity(Intent(this, WeaponActivity::class.java))
        }

        binding.bookmarks.setOnClickListener {
            startActivity(Intent(this,MarkActivity::class.java))
        }
    }
}