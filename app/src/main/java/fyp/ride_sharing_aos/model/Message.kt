package fyp.ride_sharing_aos.model

import java.security.Timestamp

/**
 * Created by lfreee on 21/2/2018.
 */

class Message {

        var message : String? = null
        var send_time : Long? = null
        var sender : String? = null
        var type : Int? = null


    constructor() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        constructor(message: String?, send_time: Long?, sender: String?, type : Int?) {
            this.message = message
            this.send_time = send_time
            this.sender = sender
            this.type = type
        }



}