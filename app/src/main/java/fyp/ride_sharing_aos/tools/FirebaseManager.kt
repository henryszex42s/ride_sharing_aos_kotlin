package fyp.ride_sharing_aos.tools

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import fyp.ride_sharing_aos.model.Message
import fyp.ride_sharing_aos.model.Room
import fyp.ride_sharing_aos.model.User
import com.google.firebase.auth.EmailAuthProvider
import fyp.ride_sharing_aos.R

/**
 * Created by lfreee on 21/1/2018.
 */

object FirebaseManager {

    private val TAG ="FirebaseManager"
    private var fbAuth = FirebaseAuth.getInstance()
    private var fbUser : FirebaseUser ?= null
    private var db = FirebaseFirestore.getInstance()


    val REQUEST_TYPE_CREATE_ROOM = "1"
    val REQUEST_TYPE_JOIN_ROOM = "2"
    val REQUEST_TYPE_LEAVE_ROOM = "3"
    val REQUEST_TYPE_DISMISS_ROOM = "4"

    val MESSAGE_TYPE_SYSTEM = "0"
    val MESSAGE_TYPE_USER = "1"


    lateinit private var MessageListenerVal : ListenerRegistration
    lateinit private var UserListenerVal : ListenerRegistration
    lateinit private var RoomListenerVal : ListenerRegistration
    lateinit private var RoomListListenerVal : ListenerRegistration

    //For Filter
    var  startingFilterValue = "None"
    var  destinationFilterValue = "None"
    var genderFilterValue = "None"
    var minPassengersFilterValue = 0

//    private var database = FirebaseDatabase.getInstance()
//    private var dbReference = database.reference

    var UserObj : User ?= null
    private val RoomList    = mutableListOf<Room>()
    val MessageList = mutableListOf<Message>()
    var currentUserSession =""
    var RoomObj :Room ?=null

    var AutoMatchRoomIDTemp : String? = null

    fun isLogin() : Boolean
    {
        fbUser = fbAuth.currentUser
        return fbUser != null
    }

    fun isEmailVerified() : Boolean
    {
        if(fbAuth.currentUser != null)
            return fbAuth.currentUser!!.isEmailVerified
        else
            return false
    }

    fun signOut()
    {
        fbAuth.signOut()
        fbUser = fbAuth.currentUser
        UserObj = null
    }

    fun getUser() : User?
    {
        return UserObj
    }

    fun getUserID() : String?
    {
        if(UserObj != null)
        {
            return UserObj!!.uid

        }
        return ""
    }

    fun getEmail() : String?
    {
        if(UserObj != null)
        {
            return UserObj!!.email

        }
        return ""
    }


    fun isRoomIDValid() : Boolean
    {
        return (getRoomID() != "" && getRoomID() != "-1")
    }

    fun getRoomList() : MutableList<Room>
    {
        return RoomList
    }

    fun getRoomID() : String
    {
        return UserObj!!.chatsession!!
    }

    fun getGender() : String
    {
        return UserObj!!.gender!!
    }


    fun detachUserListener()
    {
        UserListenerVal.remove()
    }
    fun detachRoomListener()
    {
        RoomListenerVal.remove()
    }
    fun detachMessageListener()
    {
        MessageListenerVal.remove()
    }
    fun detachRoomListListener()
    {
        RoomListListenerVal.remove()

    }

    fun uidGetUserID(uid:String, callback: (String) -> Unit)
    {
        db.collection("user").document(uid).get().addOnSuccessListener { documentReference ->
            val u_name = documentReference.get("username").toString()
            callback(u_name)
        }.addOnFailureListener {
            val u_name =""
            callback(u_name)
        }

    }

    fun ridGetGender(rid:String , callback:(String) -> Unit)
    {
        db.collection("room").document(rid).get().addOnSuccessListener { documentReference ->
            val femaleFil = documentReference.get("femaleFil").toString().toBoolean()
            val maleFil = documentReference.get("maleFil").toString().toBoolean()

            if(femaleFil)
            {
                callback("femaleFil")
            }
            else if(maleFil)
            {
                callback("maleFil")
            }
            else
            {
                callback("none")
            }

        }.addOnFailureListener {
            callback("none")
        }
    }



    fun editProfile(newUserName : String)
    {

        //1. Create a Listener to listener the change in UserObj, if Chatsession is changed, the callback will be trigger.

        val data = HashMap<String, Any>()
        data.put("username", newUserName)
        //2.We update the room obj  with type = 2 and value = <Current User ID> to trigger the firebase cloud function to update the UserObj
        db.collection("user")
                .document(getUserID()!!)
                .update(data)
                .addOnSuccessListener{
                    Log.d(TAG, "SuccessListener: ")

                }
                .addOnFailureListener{
                    Log.d(TAG, "addOnFailureListener: ")

                }
    }


    fun changePassword(newPassword : String, oldPassword: String, c : Context)
    {
        val credential = EmailAuthProvider.getCredential(getEmail()!!, oldPassword)
        fbAuth.currentUser!!.reauthenticate(credential).addOnSuccessListener {
            fbAuth.currentUser!!.updatePassword(newPassword).addOnSuccessListener {
                Toast.makeText(c, c.getString(R.string.updatePass_Success) , Toast.LENGTH_SHORT).show();
            }
            .addOnFailureListener {
                Toast.makeText(c, c.getString(R.string.updatePass_Fail) , Toast.LENGTH_SHORT).show();
            }
        }

    }












    fun resetUserChatsession()
    {
        val data = HashMap<String, Any>()
        data.put("chatsession", "")

        db.collection("user")
                .document(getUserID()!!)
                .update(data)
                .addOnSuccessListener{
                    Log.d(TAG, "addOnSuccessListener: ")
                }
                .addOnFailureListener{
                    Log.d(TAG, "addOnFailureListener: ")
                }
    }

    fun createRoom(newRoom : Room, callback: (Any)->Unit)
    {
//        Realtime Database Code
//        val roomList = database.reference.child("room").push()
//        roomList.setValue(newRoom)

        //1. Create a Listener to listener the change in UserObj, if Chatsession is changed, the callback will be trigger.
            UserJoinOrLeaveRoomListener(callback)

        //2.We Create a room with type = 1 to trigger the firebase cloud function to update the UserObj
            db.collection("room")
                    .add(newRoom)
                    .addOnSuccessListener({ documentReference ->
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.id)
                        if(UserObj != null)
                        {
                            UserObj!!.chatsession = documentReference.id
                        }
                    })
                    .addOnFailureListener(
                            {
                                e -> Log.w(TAG, "Error adding document", e)
                            })
    }

    fun joinRoom(roomid : String, callback: (Any) -> Unit)
    {

        //1. Create a Listener to listener the change in UserObj, if Chatsession is changed, the callback will be trigger.
        UserJoinOrLeaveRoomListener(callback)

        val data = HashMap<String, Any>()
        data.put("type", REQUEST_TYPE_JOIN_ROOM)
        data.put("value", UserObj!!.uid!!)

        //2.We update the room obj  with type = 2 and value = <Current User ID> to trigger the firebase cloud function to update the UserObj
        db.collection("room")
                .document(roomid)
                .update(data)
                .addOnSuccessListener{
                    Log.d(TAG, "SuccessListener: ")
                }
                .addOnFailureListener{
                    Log.d(TAG, "addOnFailureListener: ")
                }
    }

    fun lockRoom(callback: (Boolean) -> Unit)
    {

        if(FirebaseManager.RoomObj!!.uid1 != FirebaseManager.getUserID())
        {
            callback(false)
        }


        val data = HashMap<String, Any>()
        data.put("locked", true)

        db.collection("room")
                .document(getRoomID())
                .update(data)
                .addOnSuccessListener{
                    Log.d(TAG, "SuccessListener: ")
                    callback(true)
                }
                .addOnFailureListener{
                    Log.d(TAG, "addOnFailureListener: ")
                    callback(true)

                }
    }


    fun leaveChatRoom(callback: (Any)->Unit)
    {

        UserJoinOrLeaveRoomListener(callback)

        val data = HashMap<String, Any>()
        data.put("type", REQUEST_TYPE_LEAVE_ROOM)
        data.put("value", UserObj!!.uid!!)

        db.collection("room")
                .document(getRoomID())
                .update(data)
                .addOnSuccessListener{
                    Log.d(TAG, "SuccessListener: ")
                }
                .addOnFailureListener{
                    Log.d(TAG, "addOnFailureListener: ")
                }
    }


    fun MessageUpdateListener(callback: (Any)->Unit,subscribe : Boolean)
    {
        if(subscribe)
        {
            MessageListenerVal = db.collection("room").document(getRoomID()).collection("chat").orderBy("send_time",Query.Direction.ASCENDING)
                    .addSnapshotListener(EventListener<QuerySnapshot> { snapshots, e ->
                        if (e != null) {
                            Log.w(TAG, "listen:error", e)
                            return@EventListener
                        }
                        MessageList.clear()
                        for (doc in snapshots) {
                            val note = doc.toObject(Message::class.java)
                            MessageList.add(note)
                        }
                        callback(Unit)
                    })
        }
        else
        {
            MessageListenerVal.remove()
        }
    }

    fun UserJoinOrLeaveRoomListener(callback: (Any)->Unit)
    {
        UserListenerVal = db.collection("user").document(getUserID()!!)
                .addSnapshotListener(EventListener<DocumentSnapshot> { snapshots, e ->

                    if (e != null) {
                        Log.w(TAG, "UserJoinOrLeaveRoomListener :error", e)
                        return@EventListener
                    }

                    UserObj = snapshots.toObject(User::class.java)

                    Log.d(TAG, "Before Updated CurrentUserSession: "+ currentUserSession)

                    //If user tried to Join the Room, the val must be changed by the firebase cloud function
                    if(!(UserObj!!.chatsession.equals(currentUserSession)))
                    {
                        //The User tried to join room, process the result in callback function
                        callback(Unit)
                    }

                    //Update the current user chatsession.
                    currentUserSession = UserObj!!.chatsession!!
                    Log.d(TAG, "Updated CurrentUserSession: "+ currentUserSession)

                })
    }

    fun RoomObjUpdateListener(callback: (Any)->Unit)
    {
        RoomListenerVal = db.collection("room").document(getRoomID())
                .addSnapshotListener(EventListener<DocumentSnapshot> { snapshots, e ->
                    if (e != null) {
                        Log.w(TAG, "RoomObjUpdateListener :error", e)
                        return@EventListener
                    }
                    RoomObj = snapshots.toObject(Room::class.java)
                    callback(Unit)
                })
    }







    fun sendMessage(newMessage: HashMap<String, Any>)
    {

        db.collection("room")
                .document(getRoomID())
                .collection("chat")
                .add(newMessage)
                .addOnSuccessListener({ documentReference ->
                    Log.d(TAG, "Message DocumentSnapshot written with ID: " + documentReference.id)

                })

                .addOnFailureListener(
                 {
                            e -> Log.w(TAG, "Error adding document", e)
                 })


    }


    fun updateUserObj()
    {

        if(!isLogin())
        {
            return
        }

        val docRef = db.collection("user").document(fbUser!!.uid)
        docRef.get().addOnSuccessListener({ documentSnapshot ->
            UserObj = documentSnapshot.toObject(User::class.java)
            currentUserSession = UserObj!!.chatsession!!
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
        val searchQueries = makeQueries()

//        detachRoomListListener()

        RoomListListenerVal = searchQueries.addSnapshotListener(EventListener<QuerySnapshot>{ snapshots, e ->

            if (e == null)
            {
                RoomList.clear()

                for (doc in snapshots) {
                    val note = doc.toObject<Room>(Room::class.java)
                    RoomList.add(note)
                }

                if (RoomList.isEmpty()) {
                    Log.e(TAG, "onRoomListDataChange: RoomList Data is null!")
                }
                else
                {
                    Log.e(TAG, "onRoomListDataChange: RoomList Data Received")

                }

                callback(Unit)
            }
            else
            {
                Log.d(TAG, "onDataChange : Error getting documents: ", e)
                callback(Unit)
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

    fun makeQueries() : Query
    {
        var queries : Query
        queries = db.collection("room")

        if(startingFilterValue != "None")
        {
            queries = queries.whereEqualTo("start",startingFilterValue)
        }

        if(destinationFilterValue != "None")
        {
            queries = queries.whereEqualTo("destination",destinationFilterValue)

        }

        if(genderFilterValue != "None")
        {
            if(genderFilterValue == "Male")
            {
                queries = queries.whereEqualTo("maleFil",true)
                queries = queries.whereEqualTo("femaleFil",false)
            }
            if(genderFilterValue == "Female")
            {
                queries = queries.whereEqualTo("maleFil",false)
                queries = queries.whereEqualTo("femaleFil",true)
            }
        }

        if(minPassengersFilterValue != 0)
        {
            queries = queries.whereGreaterThanOrEqualTo("numberOfPeople",minPassengersFilterValue)
        }

        queries = queries.whereEqualTo("locked",false)
        queries = queries.orderBy("createtime")
        return queries
    }


    fun autoMatching(callback: (Any) -> Unit)
    {
        var queries : Query
        queries = db.collection("room")
        var autoMatchingRoomList = mutableListOf<Room>()

        if(startingFilterValue != "None")
        {
            queries = queries.whereEqualTo("start",startingFilterValue)
        }
        if(destinationFilterValue != "None")
        {
            queries = queries.whereEqualTo("destination",destinationFilterValue)
        }

        queries = queries.whereEqualTo("locked",false)
        queries = queries.orderBy("createtime")



        queries.get().addOnCompleteListener({ task ->
            if (task.isSuccessful)
            {
                autoMatchingRoomList.clear()

                for (doc in task.result) {
                    val note = doc.toObject<Room>(Room::class.java)
                    autoMatchingRoomList.add(note)
                }

                if (autoMatchingRoomList.isEmpty()) {
                    Log.e(TAG, "onDataChange: RoomList data is null!")
                    AutoMatchRoomIDTemp = ""
                }
                else
                {
                    Log.e(TAG, "onDataChange: RoomList data is not null!")
                    AutoMatchRoomIDTemp = autoMatchingRoomList[0].roomid!!
                }
                callback(Unit)
            }
            else
            {
                Log.d(TAG, "onDataChange : Error getting documents: ", task.exception)
                AutoMatchRoomIDTemp = ""
                callback(Unit)
            }
        })

    }








}