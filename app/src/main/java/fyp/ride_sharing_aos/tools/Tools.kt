package fyp.ride_sharing_aos.tools

import android.content.Context
import android.location.Location
import android.support.v7.app.AlertDialog
import com.google.android.gms.maps.model.LatLng
import fyp.ride_sharing_aos.R
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

/**
 * Created by lfreee on 19/1/2018.
 */

object Tools
{
    var currentLocation = Location("")
    val currentTime = Timestamp(System.currentTimeMillis())

    enum class Location_Name {
        HKUST_North_Gate, HKUST_South_Gate,Diamond_Hill_MTR_Station,Choi_Hung_MTR_Station, Hang_Hau_MTR_Station, Ngau_Tau_Kok_MTR_Station
    }
    /* Coordinate of Locations*/
    val coordinate_ustng = LatLng(22.3384087,114.262253)
    val coordinate_ustsg = LatLng(22.3330995,114.2632765)
    val coordinate_dh = LatLng(22.3401561,114.2018975)
    val coordinate_ch = LatLng(22.3349716, 114.2085751)
    val coordinate_hh = LatLng(22.315596,114.264393)
    val coordinate_ntk = LatLng(22.3154622,114.2191418)


//    val error_msg: ArrayList<String> = ArrayList()
//    error_msg.add(String)
    fun showDialog(context: Context, title:String, error_msg:ArrayList<String>)
{
    if (error_msg != null) {
        val builder = AlertDialog.Builder(context)

        var errorString: String = ""

        for (i in error_msg.listIterator()) {
            errorString += "\n\n"
            errorString += i
        }

        builder.setIcon(R.drawable.app_icon)
        builder.setMessage(errorString)
        builder.setCancelable(false)
        builder.setTitle(title)

        builder.setPositiveButton(
                "OK"
        ) { dialog, id -> dialog.cancel() }


        val alert = builder.create()
        alert.show()
        }
}

    fun convertTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy MM dd HH:mm:ss")
        return format.format(date)
    }
}
