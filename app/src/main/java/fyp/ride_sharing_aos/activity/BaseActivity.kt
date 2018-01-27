package fyp.ride_sharing_aos.activity

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
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


}