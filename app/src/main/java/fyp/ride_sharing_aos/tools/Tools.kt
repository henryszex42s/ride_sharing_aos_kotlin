package fyp.ride_sharing_aos.tools

import android.content.Context
import android.location.Location
import android.support.v7.app.AlertDialog
import com.google.android.gms.maps.model.LatLng
import fyp.ride_sharing_aos.R

/**
 * Created by lfreee on 19/1/2018.
 */

object Tools
{
    var currentLocation = Location("")

    enum class Location_Name {
        HKUST, Choi_Hung_MTR_Station, Hang_Hau_MTR_Station, Tseung_Kwan_O_MTR_Station
    }
    /* Coordinate of Locations*/
    val coordinate_ust = LatLng(22.336397, 114.265506)
    val coordinate_ch = LatLng(22.3349716, 114.2085751)
    val coordinate_hh = LatLng(22.3156009, 114.262199)
    val coordinate_tko = LatLng(22.3074385, 114.258921)


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

    fun showDialog(context: Context, title:String, msg: String,positive: String, negative : String) : AlertDialog.Builder
    {

            val builder = AlertDialog.Builder(context)
            builder.setIcon(R.drawable.app_icon)
            builder.setMessage(msg)
            builder.setCancelable(false)
            builder.setTitle(title)

            builder.setPositiveButton(positive
            ){ dialog, id -> dialog.cancel() }

            builder.setNegativeButton(negative
            ) { dialog, id -> dialog.cancel() }

            val alert = builder.create()
            alert.show()

        return builder
    }
}
