package fyp.ride_sharing_aos.tools

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import fyp.ride_sharing_aos.model.Room
import fyp.ride_sharing_aos.model.User
import java.util.stream.Collectors

/**
 * Created by lfreee on 21/1/2018.
 */
object FirebaseManager {

    private var fbAuth = FirebaseAuth.getInstance()
    private var fbUser : FirebaseUser ?= null
    private var db = FirebaseFirestore.getInstance()


//    private var database = FirebaseDatabase.getInstance()
//    private var dbReference = database.reference

    private var UserObj : User ?= null
    private val RoomList: MutableList<Room> = mutableListOf()


    fun isLogin() : Boolean
    {
        return fbAuth.currentUser != null
    }

    fun signOut()
    {

        fbAuth.signOut()
        fbUser = fbAuth.currentUser
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

    fun createRoom(newRoom : Room)
    {
//        Realtime Database Code
//        val roomList = database.reference.child("room").push()
//        roomList.setValue(newRoom)

        db.collection("room")
                .add(newRoom)
                .addOnSuccessListener({ documentReference ->
                    Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.id)
                })
                .addOnFailureListener(
                        {
                    e -> Log.w(TAG, "Error adding document", e)
                })
    }


    fun updateUser()
    {
        fbUser = fbAuth.currentUser
        if(fbUser == null)
        {
            return
        }

        val docRef = db.collection("user").document(fbUser!!.uid)

        docRef.get().addOnSuccessListener({ documentSnapshot ->
            UserObj = documentSnapshot.toObject(User::class.java)
        })

        docRef.get().addOnFailureListener({
            e -> Log.w(TAG, "Error reading document", e)
         })

//        Realtime Database Code
//        dbReference.child("users").child(fbUser?.uid).addListenerForSingleValueEvent(
//                object : ValueEventListener {
//                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//                        UserObj = dataSnapshot.getValue(User::class.java)
//
//                        if (UserObj == null) {
//                            Log.e(TAG, "onDataChange: User data is null!")
//                            return
//                        }
//                            Log.e(TAG, "onDataChange: User data is not null!")
//                    }
//                    override fun onCancelled(error: DatabaseError) {
//                        // Failed to read value
//                        Log.e(TAG, "onCancelled: Failed to read user!")
//                    }
//                }
//        )
    }



    fun updateRoomListListener(callback: (Any)->Unit)
    {

        val docRef = db.collection("room")

        docRef.get().addOnCompleteListener({ task ->
                    if (task.isSuccessful)
                    {
                        RoomList.clear()
                        for (doc in task.result) {
                            val note = doc.toObject<Room>(Room::class.java)
                            RoomList.add(note)
                        }
                         if (RoomList.isEmpty()) {
                            Log.e(TAG, "onDataChange: RoomList data is null!")
                        }
                        callback(Unit)
                        Log.e(TAG, "onDataChange: RoomList data is not null!")
                    }
                    else
                    {
                        Log.d(TAG, "onDataChange : Error getting documents: ", task.exception)
                    }
        })


//        Realtime Database Code
//        dbReference.child("room").addValueEventListener(
//                object : ValueEventListener {
//                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//                        RoomList.clear()
//                        dataSnapshot.children.mapNotNullTo(RoomList) { it.getValue<Room>(Room::class.java) }
//                        if (RoomList.isEmpty()) {
//                            Log.e(TAG, "onDataChange: RoomList data is null!")
//                            return
//                        }
//                        callback(Unit)
//                        Log.e(TAG, "onDataChange: RoomList data is not null!")
//
//                    }
//                    override fun onCancelled(error: DatabaseError) {
//                        // Failed to read value
//                        Log.e(TAG, "onCancelled: Failed to read user!")
//                    }
//                }
//
//        )
    }




}