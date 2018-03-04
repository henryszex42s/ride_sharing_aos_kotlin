package fyp.ride_sharing_aos.tools

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ServerValue
import com.google.firebase.firestore.*
import fyp.ride_sharing_aos.model.Message
import fyp.ride_sharing_aos.model.Room
import fyp.ride_sharing_aos.model.User

/**
 * Created by lfreee on 21/1/2018.
 */
object FirebaseManager {

    private var fbAuth = FirebaseAuth.getInstance()
    private var fbUser : FirebaseUser ?= null
    private var db = FirebaseFirestore.getInstance()


    val REQUEST_TYPE_CREATE_ROOM = "0"
    val REQUEST_TYPE_JOIN_ROOM = "1"
    val REQUEST_TYPE_LEAVE_ROOM = "2"
    val REQUEST_TYPE_DISMISS_ROOM = "3"

    val MESSAGE_TYPE_SYSTEM = "0"
    val MESSAGE_TYPE_USER = "1"


    lateinit private var MessageListenerVal : ListenerRegistration
    lateinit private var UserListenerVal : ListenerRegistration
    lateinit private var RoomListenerVal : ListenerRegistration

    //For Filter
    var  startingFilterValue = "None"
    var  destinationFilterValue = "None"
    var genderFilterValue = "None"
    var minPassengersFilterValue = "None"

//    private var database = FirebaseDatabase.getInstance()
//    private var dbReference = database.reference

    var UserObj : User ?= null
    private val RoomList    = mutableListOf<Room>()
    val MessageList = mutableListOf<Message>()
    var currentUserSession =""
    var RoomObj :Room ?=null

    var createdTimestamp = ServerValue.TIMESTAMP



    private val lock = java.lang.Object()

    fun isLogin() : Boolean
    {
        fbUser = fbAuth.currentUser
        return fbUser != null

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

    fun getUserID() : String?
    {
        return UserObj!!.uid
    }

    fun getRoomList() : MutableList<Room>
    {
        return RoomList
    }

    fun getRoomID() : String
    {
        return UserObj!!.chatsession!!
    }

    fun isRoomIDValid() : Boolean
    {
        return (getRoomID() != "" && getRoomID() != "-1")
    }



    fun detachUserListener()
    {
        UserListenerVal.remove()
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

        UserJoinRoomListener(callback)

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

    fun JoinRoom(roomid : String, callback: (Any) -> Unit)
    {
        UserJoinRoomListener(callback)

        val data = HashMap<String, Any>()
        data.put("type", REQUEST_TYPE_JOIN_ROOM)
        data.put("value", UserObj!!.uid!!)

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

    fun UserJoinRoomListener(callback: (Any)->Unit)
    {
        UserListenerVal = db.collection("user").document(getUserID()!!)
                .addSnapshotListener(EventListener<DocumentSnapshot> { snapshots, e ->

                    if (e != null) {
                        Log.w(TAG, "UserJoinRoomListener :error", e)
                        return@EventListener
                    }

                    UserObj = snapshots.toObject(User::class.java)
                    Log.d(TAG, "Before Updated CurrentUserSession"+ currentUserSession)

                    //If user tried to Join the Room, the val must be changed
                    if(!(UserObj!!.chatsession.equals(currentUserSession)))
                    {
                        //The User is tried to join room, process the result in callback function
                        callback(Unit)

                    }
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







    fun addMessage( newMessage: HashMap<String, Any>)
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


    fun updateUser()
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



    fun updateRoomList(callback: (Any)->Unit)
    {

        val searchQueries = makeQueries()

        searchQueries.get().addOnCompleteListener({ task ->
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
                Log.e(TAG, "onDataChange: RoomList data is not null!")
                callback(Unit)
            }
            else
            {
                Log.d(TAG, "onDataChange : Error getting documents: ", task.exception)
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
            queries = queries.whereEqualTo("destination",destinationFilterValue)
        }
        if(minPassengersFilterValue != "None")
        {
            queries = queries.whereEqualTo("numberOfPeople",minPassengersFilterValue)
        }
        return queries
    }




}