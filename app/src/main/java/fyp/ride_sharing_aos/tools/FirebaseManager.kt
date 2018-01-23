package fyp.ride_sharing_aos.tools

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import fyp.ride_sharing_aos.model.User

/**
 * Created by lfreee on 21/1/2018.
 */
object FirebaseManager {

    private var fbAuth = FirebaseAuth.getInstance()
    private var User : User ?= null
    private var database = FirebaseDatabase.getInstance()
    private var dbRef = database.reference






    fun isLogin() : Boolean
    {
        return fbAuth.currentUser != null
    }

    fun UpdateUser()
    {


    }








}