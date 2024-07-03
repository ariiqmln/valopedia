package com.dicoding.myrecyclerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.myrecyclerview.databinding.ActivityWelcomeBinding
import android.content.Intent

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAction()
    }

    private fun setAction() {
        binding.btnwelcome.setOnClickListener {
            startActivity(Intent(this, MainMenuActivity::class.java))
        }
    }
}