package fyp.ride_sharing_aos.tools

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.location.Location
import android.support.v7.app.AlertDialog
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import fyp.ride_sharing_aos.R
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat
import com.google.firebase.firestore.ServerTimestamp



/**
 * Created by lfreee on 19/1/2018.
 */

object Tools
{
    val DEBUG_LOG = true
    var currentLocation = LatLng(0.0,0.0)
    val currentTime = Timestamp(System.currentTimeMillis())
    var nearestLocation = ""

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
    fun convertDate(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd/MM/yyyy")
        return format.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun convertTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("HH:mm")
        return format.format(date)
    }

    fun printLog(TAG : String, Message : String)
    {
        if(DEBUG_LOG)
        Log.d(TAG," : "+Message)
    }

    fun findTheNearestLocation(c : Context) : String
    {
        val ustng = getDistanceFromLatLonInKm(currentLocation,coordinate_ustng)
        val ustsg = getDistanceFromLatLonInKm(currentLocation,coordinate_ustsg)
        val dh = getDistanceFromLatLonInKm(currentLocation,coordinate_dh)
        val ch = getDistanceFromLatLonInKm(currentLocation,coordinate_ch)
        val hh = getDistanceFromLatLonInKm(currentLocation,coordinate_hh)
        val ntk = getDistanceFromLatLonInKm(currentLocation,coordinate_ntk)

        val tempArray = arrayListOf(ustng,ustsg,dh,ch,hh,ntk)
        Log.d(TAG," findTheNearestLocation : "+tempArray)

        when(tempArray.min())
        {
            ustng -> nearestLocation = c.getString(R.string.location_HKUST_North)
            ustsg -> nearestLocation = c.getString(R.string.location_HKUST_South)
            dh -> nearestLocation = c.getString(R.string.location_Diamond_Hill)
            ch -> nearestLocation = c.getString(R.string.location_Choi_Hung)
            hh -> nearestLocation = c.getString(R.string.location_Hang_Hau)
            ntk -> nearestLocation = c.getString(R.string.location_Ngau_Tau_Kok)
        }
        return nearestLocation
    }




    fun getDistanceFromLatLonInKm (latLngA : LatLng,latLngB: LatLng) : Double {
        val locationA = Location("point A")
        locationA.latitude = latLngA.latitude
        locationA.longitude = latLngA.longitude
        val locationB = Location("point B")
        locationB.latitude = latLngB.latitude
        locationB.longitude = latLngB.longitude

        return locationA.distanceTo(locationB).toDouble()
    }




}
