package com.example.mobilneprojekat.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilneprojekat.R
import com.example.mobilneprojekat.UserViewModel
import com.example.mobilneprojekat.data.Profile
import com.example.mobilneprojekat.data.SportEvent

class SportEventFragment : Fragment() {

    private var sportEventId: String = ""
    private var viewModel = UserViewModel()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SportEventAdapter
    private var userProfile: Profile? = null
    private var uid: String? = null
    private var sportEvent: SportEvent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sportEventId = arguments?.getString("sportEventId")!!
        viewModel.getSportEvent(sportEventId)

        val activity = requireActivity() as MainActivity
        uid = activity.getUid()
        viewModel.getProfile(uid!!)

        viewModel.userProfile.observe(this, Observer { profile ->
            if(profile!=null)
                userProfile = profile
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_sport_event, container, false)

        val sportTypeText = view.findViewById<TextView>(R.id.sportTypeSEF)
        val authorText = view.findViewById<TextView>(R.id.authorSEF)
        val outdoorText = view.findViewById<TextView>(R.id.outdoorSEF)
        val descriptionText = view.findViewById<TextView>(R.id.descriptionSEF)

        viewModel.sportEvent.observe(viewLifecycleOwner, Observer { sportEv ->
            if(sportEv != null){

                sportEvent = sportEv

                sportTypeText.text = sportEv.sportType
                authorText.text = sportEv.author
                if(sportEv.outdoor)
                    outdoorText.text = "Outdoor"
                else
                    outdoorText.text = "Indoor"
                descriptionText.text = sportEv.about

                val users = sportEv.participants
                val layoutManager = LinearLayoutManager(context)
                recyclerView = view.findViewById(R.id.usersSEF)
                recyclerView.layoutManager = layoutManager
                recyclerView.setHasFixedSize(true)
                adapter = SportEventAdapter(users)
                recyclerView.adapter = adapter
            }
        })

        val join = view.findViewById<Button>(R.id.joinSEF)

        join.setOnClickListener{
            if(userProfile!=null){
                if(userProfile!!.username !in sportEvent!!.participants){
                    viewModel.joinEvent(userProfile!!.username, sportEventId)
                    viewModel.updateScore(5.0, uid!!)
                }else{
                    getParentFragmentManager().popBackStack()
                }
            }
        }

        viewModel.joinedEvent.observe(viewLifecycleOwner, Observer { joined ->
            if(joined != null && joined == true)
                getParentFragmentManager().popBackStack()
        })

        val returnButton = view.findViewById<Button>(R.id.returnSEF)

        returnButton.setOnClickListener {
            getParentFragmentManager().popBackStack()
        }

        return view
    }
}