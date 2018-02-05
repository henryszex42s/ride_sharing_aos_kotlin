package fyp.ride_sharing_aos.activity

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import fyp.ride_sharing_aos.Manifest
import fyp.ride_sharing_aos.R
import kotlinx.android.synthetic.main.progress_dialog.view.*

/**
 * Created by lfreee on 27/1/2018.
 */
abstract class BaseActivity : AppCompatActivity() {

    private var progressDialog : AlertDialog ?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
    }

    fun showProgressDialog(msg:String)
    {
        val builder = AlertDialog.Builder(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.progress_dialog,null)
        dialogView.message.text = msg
        builder.setView(dialogView)
        builder.setCancelable(false)

        progressDialog = builder.create()
        progressDialog!!.show()
    }

    fun dismissProgressDialog()
    {
        if(progressDialog == null)
        {
            return
        }
        progressDialog!!.dismiss()
    }

//    fun getUserLocationRequest()
//    {
//        // Here, thisActivity is the current activity
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//            } else {
//
//                // No explanation needed, we can request the permission.
//
//                ActivityCompat.requestPermissions(this,
//                        arrayOf(Manifest.permission.READ_CONTACTS),
//                        MY_PERMISSIONS_REQUEST_READ_CONTACTS)
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//
//
//        }
//
//
//}