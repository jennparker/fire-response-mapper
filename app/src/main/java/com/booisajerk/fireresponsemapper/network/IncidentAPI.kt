package com.booisajerk.fireresponsemapper.network

import com.booisajerk.fireresponsemapper.model.Model
import com.booisajerk.fireresponsemapper.utils.BASE_URL
import io.reactivex.Observable
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface IncidentAPI {

    @GET("grwu-wqtk.json")
    fun getIncidents(
        @Query("\$limit") limit: Int
    ): Observable<List<Model.Incident>>

    companion object {
        fun create(): IncidentAPI {
            val retrofit = retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

            return retrofit.create(IncidentAPI::class.java)
        }
    }
}