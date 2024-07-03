package com.dicoding.myrecyclerview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import java.util.Locale
import java.util.concurrent.CountDownLatch

class MainActivity : AppCompatActivity() {
    private val videoPaths = arrayOf(
        "reyna.mp4",
        "jett.mp4",
        "yoru.mp4",
        "phionix.mp4",
        "raze.mp4",
        "neon.mp4",
        "iso.mp4",
        "viper.mp4",
        "brimstone.mp4",
        "omen.mp4",
        "astra.mp4",
        "harbour.mp4",
        "clove.mp4",
        "cypher.mp4",
        "killjoy.mp4",
        "sage.mp4",
        "chamber.mp4",
        "deadlock.mp4",
        "sova.mp4",
        "breach.mp4",
        "skye.mp4",
        "kayo.mp4",
        "fade.mp4",
        "gecko.mp4"
    )

    private lateinit var videoUrls: MutableList<String> // Tambahkan ini
    private lateinit var searchView: SearchView
    private lateinit var rvHeroes: RecyclerView
    private val list = ArrayList<Hero>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchView=findViewById(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })

        val buttonback =findViewById<ImageView>(R.id.back_button)
        buttonback.setOnClickListener{
            startActivity(Intent(this,MainMenuActivity::class.java))
        }

        // Inisialisasi Firebase
        FirebaseApp.initializeApp(this)

        val prfileImage : ImageView = findViewById(R.id.foto_about)
        prfileImage.setOnClickListener{
            val intent = Intent(this, Author::class.java)
            startActivity(intent)
        }

        rvHeroes = findViewById(R.id.rv_hero)
        rvHeroes.setHasFixedSize(true)

        list.addAll(getListHeroes())
        Log.d("MainActivity", "List Heroes: ${list.size}") // Cek ukuran list heroes

        fetchVideoUrls() // Ini akan memanggil showRecyclerList() setelah selesai
    }

    private fun filterList(query: String?) {
        val filteredListFauna = if (query.isNullOrEmpty()) {
            list
        } else {
            list.filter { heroes ->
                heroes.name.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))
            }
        }
        // Update RecyclerView with filtered list
        val adapter = ListHeroAdapter(ArrayList(filteredListFauna), videoUrls)
        rvHeroes.adapter = adapter
    }

    private fun fetchVideoUrls() {
        val storageReference = FirebaseStorage.getInstance().reference
        videoUrls = MutableList(videoPaths.size) { "" } // Inisialisasi dengan ukuran yang sama dan nilai default
        val latch = CountDownLatch(videoPaths.size)

        videoPaths.forEachIndexed { index,path ->
            val fullPath = "$path"
            Log.d("MainActivity", "Trying to fetch URL for: $fullPath")
            val videoRef = storageReference.child(fullPath)
            videoRef.downloadUrl.addOnSuccessListener { uri ->
                videoUrls[index] = uri.toString()
                latch.countDown()
                if (latch.count == 0L) { // Cek jika semua URL sudah di-fetch
                    runOnUiThread {
                        Log.d("MainActivity", "All URLs fetched, updating RecyclerView")
                        showRecyclerList() // Memperbarui RecyclerView di thread UI
                    }
                }
            }.addOnFailureListener { exception ->
                Log.e("MainActivity", "Error getting video URL for $fullPath", exception)
                latch.countDown()
            }
        }
    }

    private fun showRecyclerList() {
        Log.d("MainActivity", "Preparing to show RecyclerView with ${list.size} heroes and ${videoUrls.size} URLs")
        if (list.isNotEmpty() && videoUrls.isNotEmpty()) {
            rvHeroes.layoutManager = LinearLayoutManager(this)
            val listHeroAdapter = ListHeroAdapter(list, videoUrls)
            rvHeroes.adapter = listHeroAdapter
            Log.d("MainActivity", "RecyclerView should now be visible")
        } else {
            Log.d("MainActivity", "Data is missing: list size = ${list.size}, videoUrls size = ${videoUrls.size}")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_list -> {
                rvHeroes.layoutManager = LinearLayoutManager(this)
            }
            R.id.action_grid -> {
                rvHeroes.layoutManager = GridLayoutManager(this, 2)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getListHeroes(): ArrayList<Hero> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val dataRole = resources.getStringArray(R.array.data_role)
        val dataOrigin = resources.getStringArray(R.array.data_origin)
        val databg = resources.obtainTypedArray(R.array.data_photobg)
        val datatamnel = resources.getStringArray(R.array.data_tamnel)
        val datasound = resources.obtainTypedArray(R.array.data_sound)
        val dataDeskripsividio = resources.getStringArray(R.array.data_deskripsividio)

        val listHero = ArrayList<Hero>()
        for (i in dataName.indices) {
            val hero = Hero(
                dataName[i],
                dataDescription[i],
                dataPhoto.getResourceId(i, -1),
                dataRole[i],
                dataOrigin[i],
                databg.getResourceId(i, -1),
                datatamnel[i],
                datasound.getResourceId(i, -1),
                dataDeskripsividio[i],
            )
            listHero.add(hero)
        }
        return listHero
    }
}