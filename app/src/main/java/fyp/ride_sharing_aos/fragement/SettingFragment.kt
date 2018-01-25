package fyp.ride_sharing_aos.fragement

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Toast
import fyp.ride_sharing_aos.R
import fyp.ride_sharing_aos.activity.GetStartActivity

import fyp.ride_sharing_aos.model.User
import fyp.ride_sharing_aos.tools.FirebaseManager
import kotlinx.android.synthetic.main.fragment_setting.*


/**
 * Created by lfreee on 20/1/2018.
 */

class SettingFragment : Fragment(), View.OnClickListener{


    var User : User? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_setting, container, false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        FirebaseManager.UpdateUser()
        User = FirebaseManager.getUser()
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btu_setting_edit_profile.setOnClickListener(this)
        btu_setting_notification.setOnClickListener(this)
        btu_setting_language.setOnClickListener(this)
        btu_setting_about_us.setOnClickListener(this)
        btu_setting_logout.setOnClickListener(this)

        if(User != null)
        {
            tv_email.text = User?.email
            tv_username.text = User?.username
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btu_setting_edit_profile -> {
                Toast.makeText(context, "Setting Edit Profile", Toast.LENGTH_SHORT).show()
            }

            R.id.btu_setting_notification -> {
                Toast.makeText(context, "Setting Notification", Toast.LENGTH_SHORT).show()
            }

            R.id.btu_setting_language -> {
                Toast.makeText(context, "Setting Language", Toast.LENGTH_SHORT).show()
            }

            R.id.btu_setting_about_us -> {
                Toast.makeText(context, "Setting About Us", Toast.LENGTH_SHORT).show()

            }
            R.id.btu_setting_logout -> {
                Toast.makeText(context, "Setting Logout", Toast.LENGTH_SHORT).show()
                FirebaseManager.signOut()

           val intent = Intent(context, GetStartActivity::class.java)
            startActivity(intent)
            }
        }
    }


    }
