package fyp.ride_sharing_aos.fragement


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import fyp.ride_sharing_aos.R


/**
 * A simple [Fragment] subclass.
 * Use the [TransinfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransinfoFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_transinfo, container, false)
    }


}// Required empty public constructor
