package com.example.mobilneprojekat.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import java.io.Serializable

data class SportEvent(
    val sportType: String = "",
    val outdoor: Boolean = true,
    val about: String = "",
    val dateAdded: Timestamp = Timestamp.now(),
    val location: GeoPoint = GeoPoint(0.0,0.0),
    val author: String = "",
    val participants: List<String> = listOf()
)

class SportEventsDB(var id: String,
                    var sportType: String,
                    val outdoor: Boolean,
                    val about: String,
                    val dateAdded: Timestamp,
                    val location: GeoPoint,
                    val author: String,
                    val participants: List<String>) : Serializable {
    constructor():this("","",false, "", Timestamp.now(),
        GeoPoint(0.0,0.0), "", emptyList()
    )
}