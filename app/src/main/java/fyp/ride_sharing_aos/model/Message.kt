package fyp.ride_sharing_aos.model

import java.security.Timestamp

/**
 * Created by lfreee on 21/2/2018.
 */

class Message {

        var message : String? = null
        var send_time : Timestamp? = null
        var sender : String? = null


    constructor() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        constructor(message: String?, send_time: Timestamp?, sender: String?) {
            this.message = message
            this.send_time = send_time
            this.sender = sender
        }



}