package com.booisajerk.fireresponsemapper.view.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.booisajerk.fireresponsemapper.R
import com.booisajerk.fireresponsemapper.model.IncidentResponse


class IncidentAdapter : RecyclerView.Adapter<IncidentAdapter.IncidentViewHolder>() {


    private var dataSource: List<IncidentResponse>? = null

    fun setDataSource(dataSource: List<IncidentResponse>) {
        this.dataSource = dataSource
        notifyDataSetChanged()
    }

    class IncidentViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var incidentItem: IncidentResponse? = null

        private val incidentType: TextView by lazy {
            itemView.findViewById(R.id.type) as TextView
        }

        private val incidentAddress: TextView by lazy {
            itemView.findViewById(R.id.address) as TextView
        }

        private val incidentDateTime: TextView by lazy {
            itemView.findViewById(R.id.date_time) as TextView
        }

        private val incidentId: TextView by lazy {
            itemView.findViewById(R.id.incident_id) as TextView
        }

        fun bind(incident: IncidentResponse) {
            this.incidentItem = incident

            incidentType.text = incident.type
            incidentAddress.text = incident.address
            incidentDateTime.text = incident.datetime
            incidentId.text = incident.incident_number
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): IncidentViewHolder {
        val vCardView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_item, viewGroup, false)

        return IncidentViewHolder(vCardView)
    }

    override fun onBindViewHolder(viewHolder: IncidentViewHolder, position: Int) {
        val incidentItem: IncidentResponse = dataSource!![position]
        viewHolder.bind(incidentItem)
    }

    override fun getItemCount(): Int {
        return dataSource?.size ?: 0
    }
}