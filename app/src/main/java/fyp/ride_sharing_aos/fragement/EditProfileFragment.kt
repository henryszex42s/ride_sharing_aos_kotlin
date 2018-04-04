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
import android.widget.Button
import fyp.ride_sharing_aos.tools.FirebaseManager


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




        update1.setOnClickListener(View.OnClickListener {
            FirebaseManager.editProfile(new_username.toString())
        })

        update2.setOnClickListener(View.OnClickListener {
            Toast.makeText(activity,
                    "Update2 Test" ,
                    Toast.LENGTH_SHORT).show();
        })

        //return inflater!!.inflate(R.layout.fragment_edit_profile, container, false)
        return view
    }

    // TODO: Rename method, update argument and hook method into UI event

}// Required empty public constructor
