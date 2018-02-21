package fyp.ride_sharing_aos.model


/**
 * Created by lfreee on 21/1/2018.
 */
class Room {


    var start : String? = null
    var destination  : String? = null
    var numOfPeople  : Int? = null
    var maleFil  : Boolean? = null
    var femaleFil   : Boolean? = null
    var createtime  : Int? = null
    var prefertime : Int? = null
    var roomname : String? = null
    var uid1 : String? = null
    var uid2 : String? = null
    var uid3 : String? = null
    var uid4 : String? = null


    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    constructor(start: String?, destination: String?, numOfPeople: Int?, maleFil: Boolean?, femaleFil: Boolean?, createtime: Int, prefertime: Int,roomname : String? , uid1: String, uid2: String, uid3:String, uid4: String) {
        this.start = start
        this.destination = destination
        this.numOfPeople = numOfPeople
        this.maleFil = maleFil
        this.femaleFil = femaleFil
        this.createtime = createtime
        this.prefertime = prefertime
        this.roomname = roomname
        this.uid1 = uid1
        this.uid2 = uid2
        this.uid3 = uid3
        this.uid4 = uid4
    }




}