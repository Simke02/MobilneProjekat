package com.example.mobilneprojekat.ui

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilneprojekat.R
import com.example.mobilneprojekat.data.ScoreDB
import com.example.mobilneprojekat.data.SportEventsDB

class SportEventsBoardAdapter(private val sportEvents: ArrayList<SportEventsDB>): RecyclerView.Adapter<SportEventsBoardAdapter.ViewHolder>() {

    private var context : Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_sportevents,
            parent, false)

        context = itemView.context

        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return sportEvents.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = sportEvents[position]

        val geocoder = Geocoder(context!!)
        var addressText : MutableList<Address>

        addressText = geocoder.getFromLocation(currentItem.location.latitude, currentItem.location.longitude, 1)!!

        holder.sportTypeText.text = currentItem.sportType
        holder.authorText.text = currentItem.author
        holder.locationText.text = addressText.get(0).getAddressLine(0) + " " + addressText.get(0).getLocality()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val sportTypeText: TextView = itemView.findViewById(R.id.sportTypeLS)
        val authorText: TextView = itemView.findViewById(R.id.authorLS)
        val locationText: TextView = itemView.findViewById(R.id.locationLS)
    }
}