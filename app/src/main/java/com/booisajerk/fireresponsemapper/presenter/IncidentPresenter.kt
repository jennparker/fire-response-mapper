package com.booisajerk.fireresponsemapper.presenter

import com.booisajerk.fireresponsemapper.model.IncidentResponse
import rx.Observable

class IncidentPresenter() {

    fun getIncidents(): Observable<List<IncidentResponse>> {
        return Observable.create { subscriber ->

            //TODO remove fake data set eventually
            val incidents = mutableListOf<IncidentResponse>()
            for (i in 1..10) {
                println("adding fake incidents")

                incidents.add(
                    IncidentResponse(
                        "Emergency", "11/24/2018 04:0$i PM",
                        "DM-7659284$i",
                        "123$i Main",
                        "47.666",
                        "122.5445"

                    )
                )
            }
            subscriber.onNext(incidents)
        }
    }
}

