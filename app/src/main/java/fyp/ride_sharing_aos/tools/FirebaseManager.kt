package fyp.ride_sharing_aos.tools

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fyp.ride_sharing_aos.model.User

/**
 * Created by lfreee on 21/1/2018.
 */
object FirebaseManager {

    private var fbAuth = FirebaseAuth.getInstance()
    private var fbUser : FirebaseUser ?= null
    private var database = FirebaseDatabase.getInstance()
    private var dbRef = database.reference
    private var mUserReference = database.getReference()
    private var mUserListener: ValueEventListener? = null

    private var UserObj : User ?= null


    fun isLogin() : Boolean
    {
        return fbAuth.currentUser != null
    }

    fun signOut()
    {
        fbAuth.signOut()
    }

    fun getUser() : User
    {
        return UserObj as User
    }


    fun UpdateUser()
    {
        fbUser = fbAuth.currentUser
        if(fbUser == null)
        {
            return
        }
        mUserReference.child("users").child(fbUser?.uid).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        UserObj = dataSnapshot.getValue(User::class.java)

                        if (UserObj == null) {
                            Log.e(TAG, "onDataChange: User data is null!")
                            return
                        }
                            Log.e(TAG, "onDataChange: User data is not null!")
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                        Log.e(TAG, "onCancelled: Failed to read user!")
                    }
                }

        )

    }








}