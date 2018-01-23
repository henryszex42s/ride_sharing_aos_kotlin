package fyp.ride_sharing_aos.fragement

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import fyp.ride_sharing_aos.R
import fyp.ride_sharing_aos.activity.GetStartActivity
import kotlinx.android.synthetic.main.fragment_member.*

/**
 * Created by lfreee on 20/1/2018.
 */

class SettingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var fbAuth = FirebaseAuth.getInstance()
        btu_setting_logout.setOnClickListener{
            fbAuth.signOut()
            val intent = Intent(this.context, GetStartActivity::class.java)
            startActivity(intent)
        }

        return inflater!!.inflate(R.layout.fragment_member, container, false)
    }

    override fun onStart() {
        super.onStart()


    }



}
