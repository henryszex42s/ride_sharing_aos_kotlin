package fyp.ride_sharing_aos.fragement

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import fyp.ride_sharing_aos.R
import fyp.ride_sharing_aos.model.Room
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import fyp.ride_sharing_aos.activity.LoginActivity
import android.content.Intent
import android.R.xml
import android.graphics.Color
import android.widget.Button
import fyp.ride_sharing_aos.tools.FirebaseManager
import fyp.ride_sharing_aos.tools.Tools
import kotlinx.android.synthetic.main.activity_signup.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [EditProfileFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [EditProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditProfileFragment : Fragment() {

    // TODO: Rename and change types of parameters


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater!!.inflate(R.layout.fragment_edit_profile, null)
        val update1 = view.findViewById<Button>(R.id.update1)
        val update2 = view.findViewById<Button>(R.id.update2)
        var valid = true
        val error_msg: ArrayList<String> = ArrayList()

        update1.setOnClickListener(View.OnClickListener {
            //username validation
            if ( (new_username.text.toString().length < 5 || new_username.text.toString().length > 8) || new_username.text.toString().isEmpty() ) {
                error_msg.add(getString(R.string.signup_username_error))
                Tools.showDialog(context, "Alert", error_msg)
            }
            else
                FirebaseManager.editProfile(new_username.text.toString())
        })

        update2.setOnClickListener(View.OnClickListener {
            val error_msg: ArrayList<String> = ArrayList()
            var valid = true

            //password validation
            if(new_password.text.toString() == repeat_password.text.toString()) {

                //Password Validation
                if (new_password.text.toString().length < 6 || new_password.text.toString().isEmpty()) {
                    error_msg.add(getString(R.string.signup_password_error))
                    valid = false
                }

                if ( !new_password.text.toString().matches("[0-9]+".toRegex()) && (new_password.text.toString().matches("[a-z]+".toRegex()) || new_password.text.toString().matches("[A-Z]+".toRegex()))) {
                    error_msg.add(getString(R.string.signup_password_error2))

                    valid = false
                }
                if (valid) {
                    FirebaseManager.changePassword(new_password.text.toString(), old_password.text.toString(), activity)
                    error_msg.add(getString(R.string.signup_password_error2))
                }
                Tools.showDialog(context, "Alert", error_msg)
            }
            else {
                error_msg.add(getString(R.string.edit_pro_password_error))
                Tools.showDialog(context, "Alert", error_msg)
            }

        })

        return view
    }

    // TODO: Rename method, update argument and hook method into UI event

}// Required empty public constructor
