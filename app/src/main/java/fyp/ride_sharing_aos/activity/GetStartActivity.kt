package fyp.ride_sharing_aos.activity

import android.content.Intent
import android.os.Bundle
import fyp.ride_sharing_aos.BaseActivity
import fyp.ride_sharing_aos.R
import fyp.ride_sharing_aos.tools.FirebaseManager
import fyp.ride_sharing_aos.tools.Tools
import kotlinx.android.synthetic.main.activity_get_start.*

class GetStartActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_start)
        get_start_cl.setBackgroundResource(R.drawable.hkust_bgimg)
        supportActionBar?.hide()


        if(FirebaseManager.isLogin() && !FirebaseManager.isEmailVerified())
        {

            val error_msg: ArrayList<String> = ArrayList()
            error_msg.add(this.getString(R.string.login_validation_error))
            Tools.showDialog(this,getString(R.string.login__error),error_msg)
        }

        btn_login.setOnClickListener({
            val intent = Intent(this@GetStartActivity, LoginActivity::class.java)
            startActivity(intent)
//            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
        })

        btn_signup.setOnClickListener({
            val intent = Intent(this@GetStartActivity, SignupActivity::class.java)
            startActivity(intent)
//            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
        })
    }


    override fun onBackPressed() {
//       super.onBackPressed()
        moveTaskToBack(true);

    }


    }
