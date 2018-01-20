package fyp.ride_sharing_aos.tools

import android.app.Activity
import android.content.Context
import android.support.v7.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import fyp.ride_sharing_aos.R

/**
 * Created by lfreee on 19/1/2018.
 */

object Tools
{


//    val error_msg: ArrayList<String> = ArrayList()
//    error_msg.add(String)

    fun showDialog(context: Context, title:String, error_msg:ArrayList<String>)
    {
        if(error_msg != null) {
            val builder = AlertDialog.Builder(context)

            var errorString :String =""

            for (i in error_msg.listIterator())
            {
                errorString += "\n\n"
                errorString +=  i
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

    fun showProgressDialog(context: Context, title:String, msg:String)
    {

    }










}