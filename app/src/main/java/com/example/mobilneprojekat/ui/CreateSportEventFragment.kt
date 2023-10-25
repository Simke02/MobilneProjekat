package com.example.mobilneprojekat.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.mobilneprojekat.R
import com.example.mobilneprojekat.UserViewModel
import com.example.mobilneprojekat.data.Profile
import com.example.mobilneprojekat.data.Score
import com.example.mobilneprojekat.data.SportEvent
import com.google.firebase.firestore.GeoPoint
import java.util.Calendar

class CreateSportEventFragment : Fragment() {

    private var viewModel: UserViewModel = UserViewModel()
    private var userProfile: Profile? = null
    private lateinit var sportType: String
    private lateinit var sportTypes: Array<String>
    private var lat: Double? = null
    private var lng: Double? = null
    private lateinit var location: GeoPoint
    private var uid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activity = requireActivity() as MainActivity
        uid = activity.getUid()
        viewModel.getProfile(uid!!)

        viewModel.userProfile.observe(this, Observer { profile ->
            if(profile!=null)
                userProfile = profile//Profile(profile.username, profile.name, profile.lastname, profile.phone)
        })

        sportTypes = resources.getStringArray(R.array.sportTypes)

        lat = arguments?.getDouble("lat")
        lng = arguments?.getDouble("lng")

        location = GeoPoint(lat!!, lng!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_sport_event, container, false)

        var addSportEvent = view.findViewById<Button>(R.id.addSportEventCSE)
        var sportSpinner = view.findViewById<Spinner>(R.id.spinnerCSE)

        sportSpinner.adapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, sportTypes)

        sportSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                sportType = "Other"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sportType = sportTypes[position]
            }
        }


        addSportEvent.setOnClickListener{
            val outdoorCheckBox = view.findViewById<CheckBox>(R.id.outdoorCSE)
            val outdoor = outdoorCheckBox.isChecked
            val aboutText = view.findViewById<TextView>(R.id.aboutTextCSE)
            val about = aboutText.text.toString()

            val date = com.google.firebase.Timestamp(Calendar.getInstance().time)
            val participants = listOf<String>(userProfile!!.username)

            val sportEvent = SportEvent(sportType, outdoor, about, dateAdded = date, location, userProfile!!.username, participants)

            Log.d("CSE UID", uid!!)
            //viewModel.updateProfile(uid!!)
            viewModel.addSportEvent(sportEvent)
            viewModel.updateScore(10.0, uid!!) //Ne RAdI
        }

        val returnButton = view.findViewById<Button>(R.id.returnCSE)

        returnButton.setOnClickListener {
            getParentFragmentManager().popBackStack()
        }

        viewModel.addedSportEvent.observe(viewLifecycleOwner, Observer { added ->
            if(added != null && added == true)
                getParentFragmentManager().popBackStack()
        })

        return view
    }

}