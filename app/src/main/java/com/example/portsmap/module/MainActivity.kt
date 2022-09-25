package com.example.portsmap.module

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.portsmap.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

const val EXTRA_USER_MAP = "EXTRA_USER_MAP"
private const val REQUEST_CODE = 1234
const val EXTRA_MAP_TITLE = "EXTRA_MAP_TITLE"

class MainActivity : AppCompatActivity() {

    lateinit var rvMaps: RecyclerView
    private lateinit var  places: MutableList<Place>
    private lateinit var  mapAdapter: MapsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        places = generateSampleData()

        rvMaps = findViewById(R.id.rvMaps)
        rvMaps.layoutManager = LinearLayoutManager(this)
        mapAdapter = MapsAdapter(this, places, object: MapsAdapter.OnClickListener{
            override fun OnItemClick(position: Int){
                val intent = Intent(this@MainActivity, DisplayMapActivity::class.java)
                intent.putExtra(EXTRA_USER_MAP, places[position])
                startActivity(intent)
            }
        })
        rvMaps.adapter = mapAdapter

        val fabCreateMap = findViewById<FloatingActionButton>(R.id.fabCreateMap)
        fabCreateMap.setOnClickListener{
            val intent = Intent(this@MainActivity, CreateMapActivity::class.java)
            intent.putExtra(EXTRA_MAP_TITLE, title)
            startActivityForResult(intent, REQUEST_CODE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            //Get new map data from the data
            val recivePlace = data?.getSerializableExtra(EXTRA_USER_MAP) as? Place
            if (recivePlace != null) {
                places.add(recivePlace)
            }


            mapAdapter.notifyItemInserted(places.size - 1)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun generateSampleData(): MutableList<Place> {
        return listOf(
            Place("Branner Hall", "Best dorm at Stanford", 37.426, -122.163),
            Place("Gates CS building", "Many long nights in this basement", 37.430, -122.173),
            Place("Pinkberry", "First date with my wife", 37.444, -122.170)
        ).toMutableList()
    }
}