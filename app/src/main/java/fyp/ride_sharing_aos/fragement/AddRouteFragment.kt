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
 * [AddRouteFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AddRouteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddRouteFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_add_route, container, false)
    }

    override fun onStart() {
        super.onStart()
    }


}// Required empty public constructor
