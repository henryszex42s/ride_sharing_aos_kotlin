package fyp.ride_sharing_aos.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import fyp.ride_sharing_aos.R
import kotlinx.android.synthetic.main.activity_get_start.*

class GetStartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_start)
        supportActionBar?.hide()


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
    }
