package fyp.ride_sharing_aos.fragement

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import fyp.ride_sharing_aos.R

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
        return inflater!!.inflate(R.layout.fragment_edit_profile, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event

}// Required empty public constructor
