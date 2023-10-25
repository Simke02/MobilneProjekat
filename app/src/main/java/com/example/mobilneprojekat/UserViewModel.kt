package com.example.mobilneprojekat

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobilneprojekat.data.Profile
import com.example.mobilneprojekat.data.RepositoryFirebase
import com.example.mobilneprojekat.data.Score
import com.example.mobilneprojekat.data.ScoreDB
import com.example.mobilneprojekat.data.SportEvent
import com.example.mobilneprojekat.data.SportEventsDB
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import java.io.Console

class UserViewModel : ViewModel() {

    private val repository = RepositoryFirebase()

//    private val _loggedIn = MutableLiveData<Boolean?>()
//    val loggedIn: LiveData<Boolean?>
//        get() = _loggedIn

    private val _userReg = MutableLiveData<FirebaseUser?>()
    val userReg: LiveData<FirebaseUser?>
        get() = _userReg

    private val _addedProfile = MutableLiveData<Boolean?>()
    val addedProfile: LiveData<Boolean?>
        get() = _addedProfile

    private val _addedScore = MutableLiveData<Boolean?>()
    val addedScore: LiveData<Boolean?>
        get() = _addedScore

    private val _addedAvatar = MutableLiveData<Boolean?>()
    val addedAvatar: LiveData<Boolean?>
        get() = _addedAvatar

    private val _leaderboard = MutableLiveData<ArrayList<ScoreDB>?>()
    val leaderboard: LiveData<ArrayList<ScoreDB>?>
        get() = _leaderboard

    private val _userProfile = MutableLiveData<Profile?>()
    val userProfile: LiveData<Profile?>
        get() = _userProfile

    private val _addedSportEvent = MutableLiveData<Boolean?>()
    val addedSportEvent: LiveData<Boolean?>
        get() = _addedSportEvent

    private val _updatedScore = MutableLiveData<Boolean?>()
    val updatedScore: LiveData<Boolean?>
        get() = _updatedScore

    private val _sportEvents = MutableLiveData<ArrayList<SportEventsDB>?>()
    val sportEvents: LiveData<ArrayList<SportEventsDB>?>
        get() = _sportEvents

    private val _sportEvent = MutableLiveData<SportEvent?>()
    val sportEvent: LiveData<SportEvent?>
        get() = _sportEvent

    private val _joinedEvent = MutableLiveData<Boolean?>()
    val joinedEvent: LiveData<Boolean?>
        get() = _joinedEvent

    private val _score = MutableLiveData<Score?>()
    val score: LiveData<Score?>
        get() = _score

    fun login(email: String, password: String) {
        repository.login(email, password, OnCompleteListener { task ->
            if(task.isSuccessful){
                _userReg.value = task.result.user
            }
            else{
                _userReg.value = null
            }
        })
    }

    fun signup(email: String, password: String) {
        repository.signup(email, password, OnCompleteListener { task ->
            if(task.isSuccessful){
                _userReg.value = task.result.user
            }
            else{
                _userReg.value = null
            }
        })
    }

    fun addProfile(profile: Profile, uid: String){
        repository.addProfile(profile, uid, OnCompleteListener { task ->
            _addedProfile.value = task.isSuccessful
            })
    }

    fun addScore(score: Score, uid: String){
        repository.addScore(score, uid, OnCompleteListener { task ->
            _addedScore.value = task.isSuccessful
        })
    }

    fun addAvatar(byteArray: ByteArray, uid: String){
        repository.addAvatar(byteArray, uid, OnCompleteListener { task ->
            _addedAvatar.value = task.isSuccessful
        })
    }

    fun getLeaderboard(){
        repository.getLeaderboard().orderBy("points", Query.Direction.DESCENDING).addSnapshotListener(EventListener<QuerySnapshot>{ value, error ->
            if(error!=null){
                Log.w(TAG, "Listen failed.", error)
                _leaderboard.value = null
                return@EventListener
            }

            var leaderboardList: ArrayList<ScoreDB> = ArrayList<ScoreDB>()
            for(scr in value!!){
                var scoreItem: ScoreDB = scr.toObject(ScoreDB::class.java)
                leaderboardList.add(scoreItem)
            }
            _leaderboard.value = leaderboardList
        })
    }

    fun getProfile(uid: String){
        repository.getProfile(uid, OnCompleteListener { task ->
            if(task.isSuccessful){
                _userProfile.value = task.result!!.toObject<Profile>()
            }
            else{
                _userProfile.value = null
            }
        })
    }

    fun addSportEvent(sportEvent: SportEvent){
        repository.addSportEvent(sportEvent, OnCompleteListener{ task ->
            _addedSportEvent.value = task.isSuccessful
        })
    }

    fun updateScore(points: Double, uid: String){
        repository.updateScore(points, uid, OnCompleteListener{ task ->
            _updatedScore.value = task.isSuccessful
        })
    }

    fun getSportEvents(){
        repository.getSportEvents().addSnapshotListener(EventListener<QuerySnapshot>{ value, error ->
            if(error!=null){
                Log.w(TAG, "Listen failed.", error)
                _sportEvents.value = null
                return@EventListener
            }

            var sportEventsList: ArrayList<SportEventsDB> = ArrayList<SportEventsDB>()
            for(scr in value!!){
                var sportEventItem: SportEventsDB = scr.toObject(SportEventsDB::class.java)
                sportEventItem.id = scr.id
                sportEventsList.add(sportEventItem)
            }
            _sportEvents.value = sportEventsList
        })
    }

    fun getSportEvent(sportEventId: String){
        repository.getSportEvent(sportEventId, OnCompleteListener { task ->
            if(task.isSuccessful){
                _sportEvent.value = task.result!!.toObject<SportEvent>()
            }
            else{
                _sportEvent.value = null
            }
        })
    }

    fun joinEvent(username: String, sportEventId: String){
        repository.joinEvent(username, sportEventId, OnCompleteListener{ task ->
            _joinedEvent.value = task.isSuccessful
        })
    }

    fun getScore(uid: String){
        repository.getScore(uid, OnCompleteListener { task ->
            if(task.isSuccessful){
                _score.value = task.result!!.toObject<Score>()
            }
            else{
                _score.value = null
            }
        })
    }

//    fun updateProfile(uid: String){
//        repository.updateProfile(uid, OnCompleteListener{ task ->
//            _updatedScore.value = task.isSuccessful
//        })
//    }

}