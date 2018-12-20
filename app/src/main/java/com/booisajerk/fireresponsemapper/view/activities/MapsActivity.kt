package com.booisajerk.fireresponsemapper.view.activities

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import com.booisajerk.fireresponsemapper.R
import com.booisajerk.fireresponsemapper.model.Model
import com.booisajerk.fireresponsemapper.presenter.MapPresenter
import com.booisajerk.fireresponsemapper.utils.DEFAULT_ZOOM
import com.booisajerk.fireresponsemapper.utils.LOCATION_PERMISSION_REQUEST_CODE
import com.booisajerk.fireresponsemapper.utils.SEATTLE_LOCATION
import com.booisajerk.fireresponsemapper.view.interfaces.MapInterface
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : BaseActivity(), MapInterface, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private val mapPresenter = MapPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mapPresenter.onViewCreated(this)
        mapPresenter.requestIncidents()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)

        setUpMap()
    }

    override fun onMarkerClick(p0: Marker?) = false

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        map.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM))
            } else {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEATTLE_LOCATION, DEFAULT_ZOOM))
            }
        }
    }

    override fun onIncidentsLoaded(incidents: List<Model.Incident>) {
        for (loc in incidents) {
            map.addMarker(
                MarkerOptions()
                    .position(LatLng(loc.latitude, loc.longitude))
                    .title(loc.type)
                    .snippet(loc.address)
            )
        }
    }

    override fun onError(throwable: Throwable?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}