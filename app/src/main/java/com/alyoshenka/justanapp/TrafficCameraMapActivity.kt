package com.alyoshenka.justanapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrafficCameraMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var camFeatures = mutableListOf<Features>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_traffic_camera_map)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
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
        mMap = googleMap

        addLocationMarker(
            47.69902,
            -122.33255,
            "North Seattle College",
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN),
            1.0f)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(47.69902, -122.33255), 8f))

        if(ContextCompat.checkSelfPermission(
                this@TrafficCameraMapActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this@TrafficCameraMapActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        } else {
            ActivityCompat.requestPermissions(
                this@TrafficCameraMapActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1)
        }

        getCameraMarkers()
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1 -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(ContextCompat.checkSelfPermission(
                            this@TrafficCameraMapActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                            if(location != null) {
                                val bd = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                                addLocationMarker(
                                    location.latitude,
                                    location.longitude,
                                    "My Location",
                                    bd,
                                    Float.MAX_VALUE)
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    LatLng(location.latitude, location.longitude),
                                    11f))
                            }

                        }
                    }
                } else {
                    Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    private fun getCameraMarkers()  {
        val camsApi = CamsApiClient()
        val camsList = camsApi.getCamsService().getCams()

        camsList.enqueue(object : Callback<CamsResponse> {
            override fun onResponse(
                call: Call<CamsResponse>,
                response: Response<CamsResponse>
            ) {
                if (response.isSuccessful) {
                    val cams = response.body()
                    if (cams != null) {
                        for (feat in cams.Features) {
                            camFeatures.add(feat)
                        }
                    }
                    addCameraMarkers()
                }
            }

            override fun onFailure(call: Call<CamsResponse>, t: Throwable) {
                Log.e(TAG, t.localizedMessage ?: "retrofit error")
            }
        })
    }

    private fun addCameraMarkers() {
        for (feat in camFeatures) {
            for (cam in feat.Cameras) {
                addLocationMarker(
                    feat.PointCoordinate[0],
                    feat.PointCoordinate[1],
                    cam.Description ?: "No Description")
            }
        }
    }

    private fun addLocationMarker(
        lat : Double,
        long : Double,
        title : String,
        bd : BitmapDescriptor? = null,
        zIndex : Float = 0.0f) {

        val position = LatLng(lat, long)
        val address = Geocoder(this)
            .getFromLocation(lat, long, 1)[0]
            .getAddressLine(0)

        mMap.addMarker(
            MarkerOptions()
            .position(position)
            .title(title)
            .snippet(address)
            .icon(bd)
            .zIndex(zIndex))
    }

    companion object {
        val TAG: String = TrafficCameraMapActivity::class.java.simpleName
    }
}