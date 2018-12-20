package com.booisajerk.fireresponsemapper.view.interfaces

import com.booisajerk.fireresponsemapper.model.Model

interface IncidentView {

    fun onIncidentsLoaded(incidents: List<Model.Incident>)

    fun onError(throwable: Throwable?)
}