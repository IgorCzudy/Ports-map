package com.example.portsmap.module

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.portsmap.R
import com.example.portsmap.databinding.ActivityCreateMapBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import kotlin.math.roundToInt


class CreateMapActivity : AppCompatActivity(), LocationListener, OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityCreateMapBinding

    private var markers: MutableList<Marker> = mutableListOf()

    private val locationPermissionCode = 2
    private lateinit var locationManager: LocationManager
    private lateinit var loc: Location


    @SuppressWarnings("MissingPermission")
    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }


    override fun onLocationChanged(location: Location) {
        loc = location
        mMap.addMarker(MarkerOptions()
            .position(LatLng(loc.latitude, loc.longitude))
            .title("Your localisation"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(loc.latitude, loc.longitude), 10f))
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateMapBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.title = intent.getStringExtra(EXTRA_MAP_TITLE)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mapFragment.view?.let {
            Snackbar.make(
                it, "Long Press to add Marker",
            Snackbar.LENGTH_INDEFINITE)
                .setAction("OK",{})
                .setActionTextColor(ContextCompat.getColor(this, android.R.color.white))
                .show()
        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_create_map, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Check tjht item is teh save menu
        if (item.itemId == R.id.miSave){
            Log.i("TAG","tapped on save")
            if (markers.isEmpty()){
                Toast.makeText(this, "thera must be att list one marker on the map", Toast.LENGTH_LONG).show()
                return true
            }
            val places = markers.map{marker -> Place(marker.title, marker.snippet, marker.position.latitude, marker.position.longitude)}
            val data = Intent()
            data.putExtra(EXTRA_USER_MAP, places[0])
            setResult(Activity.RESULT_OK, data)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        Log.i("Cratemap", "ready map")

        mMap = googleMap

        mMap.setOnInfoWindowClickListener { markersToDel ->
            markers.remove(markersToDel)
            markersToDel.remove()
        }

        mMap.setOnMapLongClickListener {latLng ->
            Log.i("Cratemap", "fdsad")
            showAlertDialog(latLng)
        }

        // Add a marker in Sydney and move the camera
        val gizycko = LatLng(54.03, 21.77)
        getLocation()
        //val gizycko = LatLng(loc.latitude, loc.longitude)

        //mMap.addMarker(MarkerOptions().position(gizycko).title("Marker in gizycko"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gizycko, 10f))
    }

    private fun showAlertDialog(latLng: LatLng) {
        val placeFormView = LayoutInflater.from(this)
            .inflate(R.layout.dialog_create_place, null)
        val dialog = AlertDialog.Builder(this).setTitle("Create a marker")
            .setView(placeFormView)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("OK",null).show()
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val title = placeFormView.findViewById<EditText>(R.id.etName).text.toString()
            val descrition = placeFormView.findViewById<EditText>(R.id.etDescrition).text.toString()
            if (title.trim().isEmpty() || descrition.trim().isEmpty()) {
                Toast.makeText(
                    this,
                    "Place must have non-empty title and descrition",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            val marker = mMap.addMarker(MarkerOptions().position(latLng).title(title).snippet(descrition))
            markers.add(marker)
            dialog.dismiss()
        }
    }



}