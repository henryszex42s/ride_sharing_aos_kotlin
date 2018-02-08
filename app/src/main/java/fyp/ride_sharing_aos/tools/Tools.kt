package fyp.ride_sharing_aos.tools

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.location.Location
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import fyp.ride_sharing_aos.R
import kotlinx.android.synthetic.main.progress_dialog.view.*

/**
 * Created by lfreee on 19/1/2018.
 */

object Tools
{
    var currentLocation = Location("")

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
