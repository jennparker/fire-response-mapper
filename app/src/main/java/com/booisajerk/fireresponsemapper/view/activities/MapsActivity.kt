package com.booisajerk.fireresponsemapper.view.activities

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import com.booisajerk.fireresponsemapper.R
import com.booisajerk.fireresponsemapper.model.Model
import com.booisajerk.fireresponsemapper.network.IncidentInterface
import com.booisajerk.fireresponsemapper.utils.DEFAULT_ZOOM
import com.booisajerk.fireresponsemapper.utils.LOCATION_PERMISSION_REQUEST_CODE
import com.booisajerk.fireresponsemapper.utils.SEATTLE_LOCATION
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class MapsActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    override fun onMarkerClick(p0: Marker?) = false

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    private var incidentList: List<Model.Incident> = emptyList()

    private var disposable: Disposable? = null

    private val incidentInterface by lazy {
        IncidentInterface.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        requestIncidents()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)

        setUpMap()
    }

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

    private fun requestIncidents() {
        disposable = incidentInterface.getIncidents(25)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { retrievedIncidents ->
                    for (loc in retrievedIncidents) {
                        map.addMarker(
                            MarkerOptions()
                                .position(LatLng(loc.latitude, loc.longitude))
                                .title(loc.type)
                                .snippet(loc.address)
                        )
                    }
                },
                { e ->
                    e.printStackTrace()
                })
    }
}