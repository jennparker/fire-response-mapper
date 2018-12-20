package com.booisajerk.fireresponsemapper.presenter

import android.support.v7.app.AppCompatActivity
import com.booisajerk.fireresponsemapper.model.Model
import com.booisajerk.fireresponsemapper.network.IncidentAPI
import com.booisajerk.fireresponsemapper.utils.DEFAULT_INCIDENT_COUNT
import com.booisajerk.fireresponsemapper.view.interfaces.MapInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MapPresenter : AppCompatActivity() {
    private var disposable: Disposable? = null
    private var incidentList: ArrayList<Model.Incident> = ArrayList()
    private var mapInterface: MapInterface? = null

    private val incidentAPI by lazy {
        IncidentAPI.create()
    }

    fun onViewCreated(view: MapInterface) {
        mapInterface = view
    }

    fun requestIncidents() {
        disposable = incidentAPI.getIncidents(DEFAULT_INCIDENT_COUNT)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { retrievedIncidents ->
                    incidentList.addAll(retrievedIncidents)
                    mapInterface?.onIncidentsLoaded(incidentList)
                },
                { e ->
                    e.printStackTrace()
                    mapInterface?.onError(e)
                })
    }
}
