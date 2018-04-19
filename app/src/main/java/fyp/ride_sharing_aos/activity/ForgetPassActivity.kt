package fyp.ride_sharing_aos.activity

import android.os.Bundle
import com.google.android.gms.gcm.Task
import com.google.firebase.auth.FirebaseAuth
import fyp.ride_sharing_aos.BaseActivity
import fyp.ride_sharing_aos.R
import fyp.ride_sharing_aos.tools.Tools
import kotlinx.android.synthetic.main.activity_pass.*

/**
 * Created by lfreee on 18/4/2018.
 */
class ForgetPassActivity : BaseActivity(){

    private lateinit var mAuth : FirebaseAuth

    private val TAG = "ForgetPassActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pass)
        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        submit_button.setOnClickListener{
            val email = email.text.toString()

            mAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                        if(task.isSuccessful)
                        {
                            val error_msg: ArrayList<String> = ArrayList()
                            error_msg.add(this.getString(R.string.forgotpass_sent))
                            Tools.showDialog(this,getString(R.string.reset_title),error_msg)
                        }
                        else
                        {
                            val error_msg: ArrayList<String> = ArrayList()
                            error_msg.add(this.getString(R.string.forgotpass_not_sent))
                            Tools.showDialog(this,getString(R.string.reset_title),error_msg)
                        }
                    }
        }


    }

}