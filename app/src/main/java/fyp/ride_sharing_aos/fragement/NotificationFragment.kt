package fyp.ride_sharing_aos.fragement


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fyp.ride_sharing_aos.R




/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NotificationFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NotificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotificationFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater!!.inflate(R.layout.fragment_notification, null)
        return view
    }


}// Required empty public constructor
