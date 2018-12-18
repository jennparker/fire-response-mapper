package com.booisajerk.fireresponsemapper.model

object Model {
    data class Incident(
        val type: String,
        val datetime: String,
        val incident_number: String,
        val address: String,
        val latitude: Double,
        val longitude: Double
    )
}