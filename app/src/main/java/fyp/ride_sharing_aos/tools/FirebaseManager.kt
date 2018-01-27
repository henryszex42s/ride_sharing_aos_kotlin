package fyp.ride_sharing_aos.tools

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fyp.ride_sharing_aos.model.Room
import fyp.ride_sharing_aos.model.User
import java.util.stream.Collectors

/**
 * Created by lfreee on 21/1/2018.
 */
object FirebaseManager {

    private var fbAuth = FirebaseAuth.getInstance()
    private var database = FirebaseDatabase.getInstance()
    private var fbUser : FirebaseUser ?= null


    private var dbReference = database.reference

    private var UserObj : User ?= null

//    private var RoomListMap: Map<String, Room>? = null

    private val RoomList: MutableList<Room> = mutableListOf()


    fun isLogin() : Boolean
    {
        return fbAuth.currentUser != null
    }

    fun signOut()
    {
        fbAuth.signOut()
    }

    fun getUser() : User?
    {
        return UserObj
    }


    fun getRoomList() : MutableList<Room>
    {
        return RoomList
    }

    fun roomListisEmpty() : Boolean
    {
        return RoomList.isEmpty()
    }


    fun updateUser()
    {
        fbUser = fbAuth.currentUser
        if(fbUser == null)
        {
            return
        }

        dbReference.child("users").child(fbUser?.uid).addListenerForSingleValueEvent(
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



    fun setRoomListListener(callback: (Any)->Unit)
    {
        dbReference.child("room").addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        RoomList.clear()
                        dataSnapshot.children.mapNotNullTo(RoomList) { it.getValue<Room>(Room::class.java) }
                        if (RoomList.isEmpty()) {
                            Log.e(TAG, "onDataChange: RoomList data is null!")
                            return
                        }
                        callback(Unit)
                        Log.e(TAG, "onDataChange: RoomList data is not null!")

                    }
                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                        Log.e(TAG, "onCancelled: Failed to read user!")
                    }
                }

        )
    }
}