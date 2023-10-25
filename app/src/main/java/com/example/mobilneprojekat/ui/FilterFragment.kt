package com.example.mobilneprojekat.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.example.mobilneprojekat.R
import java.util.Calendar
import java.util.Date

class FilterFragment : Fragment() {

    private var authorFilter: String = ""
    private var sportTypesFilter: ArrayList<String> = ArrayList<String>()
    private var outdoorFilter: String = ""
    private var date : Date? = null
    private var radius: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authorFilter = arguments?.getString("author")!!
        sportTypesFilter = arguments?.getStringArrayList("sportTypes")!!
        outdoorFilter = arguments?.getString("outdoor")!!
        date = arguments?.getSerializable("date") as? Date
        radius = arguments?.getDouble("radius")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_filter, container, false)

        val authorInput = view.findViewById<TextView>(R.id.authorsAF)
        authorInput.text = authorFilter
        val basketballCheck = view.findViewById<CheckBox>(R.id.basketballAF)
        if("Basketball" in sportTypesFilter)
            basketballCheck.isChecked = true
        val footballCheck = view.findViewById<CheckBox>(R.id.footballAF)
        if("Football" in sportTypesFilter)
            footballCheck.isChecked = true
        val tennisCheck = view.findViewById<CheckBox>(R.id.tennisAF)
        if("Tennis" in sportTypesFilter)
            tennisCheck.isChecked = true
        val volleyballCheck = view.findViewById<CheckBox>(R.id.volleyballAF)
        if("Volleyball" in sportTypesFilter)
            volleyballCheck.isChecked = true
        val handballCheck = view.findViewById<CheckBox>(R.id.handballAF)
        if("Handball" in sportTypesFilter)
            handballCheck.isChecked = true
        val otherCheck = view.findViewById<CheckBox>(R.id.otherAF)
        if("Other" in sportTypesFilter)
            otherCheck.isChecked = true
        val outdoorCheck = view.findViewById<CheckBox>(R.id.outdoorAF)
        if("Out" == outdoorFilter)
            outdoorCheck.isChecked = true
        val indoorCheck = view.findViewById<CheckBox>(R.id.indoorAF)
        if("In" == outdoorFilter)
            indoorCheck.isChecked = true
        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
        if(date!=null)
            calendarView.date = date!!.time
        val radiusNumber = view.findViewById<EditText>(R.id.radiusAF)
        if(radius!=0.0)
            radiusNumber.setText(radius.toString())

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)

            date = calendar.time
        }

        val addFilter = view.findViewById<Button>(R.id.addFilters)

        addFilter.setOnClickListener{
            sportTypesFilter = ArrayList<String>()

            if(basketballCheck.isChecked)
                sportTypesFilter.add("Basketball")
            if(footballCheck.isChecked)
                sportTypesFilter.add("Football")
            if(tennisCheck.isChecked)
                sportTypesFilter.add("Tennis")
            if(volleyballCheck.isChecked)
                sportTypesFilter.add("Volleyball")
            if(handballCheck.isChecked)
                sportTypesFilter.add("Handball")
            if(otherCheck.isChecked)
                sportTypesFilter.add("Other")
            if(basketballCheck.isChecked)
                sportTypesFilter.add("Basketball")
            authorFilter = authorInput.text.toString()

            if(outdoorCheck.isChecked && !indoorCheck.isChecked)
                outdoorFilter = "Out"
            else if(!outdoorCheck.isChecked && indoorCheck.isChecked)
                outdoorFilter = "In"
            else
                outdoorFilter = ""
            if(radiusNumber.text.isNotEmpty())
                radius = radiusNumber.text.toString().toDoubleOrNull()!!
            else
                radius = 0.0

            val fragment = MapFragment()

            val bundle = Bundle()
            bundle.putString("author", authorFilter)
            bundle.putStringArrayList("sportTypes",sportTypesFilter)
            bundle.putString("outdoor", outdoorFilter)
            if(date!=null)
                bundle.putSerializable("date", date)
            bundle.putDouble("radius", radius)
            fragment.arguments = bundle

            val activity = requireActivity() as MainActivity
            val fragmentManager = activity.getFragmentManagerMA()
            val transaction = fragmentManager.beginTransaction()

            transaction.replace(R.id.frame_layout, fragment)
            transaction.addToBackStack("filter")
            transaction.commit()
        }

        val returnButton = view.findViewById<Button>(R.id.returnAF)

        returnButton.setOnClickListener {
            getParentFragmentManager().popBackStack()
        }

        return view
    }

}