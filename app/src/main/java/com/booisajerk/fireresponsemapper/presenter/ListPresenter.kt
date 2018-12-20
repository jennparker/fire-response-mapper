package com.booisajerk.fireresponsemapper.presenter

import android.support.v7.app.AppCompatActivity
import com.booisajerk.fireresponsemapper.model.Model
import com.booisajerk.fireresponsemapper.network.IncidentAPI
import com.booisajerk.fireresponsemapper.utils.DEFAULT_INCIDENT_COUNT
import com.booisajerk.fireresponsemapper.view.interfaces.ListInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ListPresenter : AppCompatActivity() {
    private var disposable: Disposable? = null
    private var listInterface: ListInterface? = null
    private var incidentList: ArrayList<Model.Incident> = ArrayList()

    private val incidentAPI by lazy {
        IncidentAPI.create()
    }

    fun onViewCreated(view: ListInterface) {
        listInterface = view
    }

    fun requestIncidents() {
        disposable = incidentAPI.getIncidents(DEFAULT_INCIDENT_COUNT)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { retrievedIncidents ->
                    incidentList.addAll(retrievedIncidents)
                    listInterface?.onIncidentsLoaded(incidentList)
                },
                { e ->
                    e.printStackTrace()
                    listInterface?.onError(e)
                })
    }
}