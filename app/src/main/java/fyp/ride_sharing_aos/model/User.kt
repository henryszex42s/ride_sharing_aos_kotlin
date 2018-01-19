package fyp.ride_sharing_aos.model

/**
 * Created by lfreee on 7/1/2018.
 */


class User{

    var username : String? = null
    var uid : String? = null
    var email : String? = null
    var identity : String? = null
    var gender : String? = null
    var chatsession : String? = null


    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    constructor(username: String?, uid: String?, email: String?, identity: String?, gender: String?) {
        this.username = username
        this.uid = uid
        this.email = email
        this.identity = identity
        this.gender = gender
        this.chatsession =""
    }













}