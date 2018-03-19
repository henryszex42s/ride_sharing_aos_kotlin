package fyp.ride_sharing_aos.fragement


import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import fyp.ride_sharing_aos.R
import fyp.ride_sharing_aos.R.string.more


/**
 * A simple [Fragment] subclass.
 * Use the [TransinfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransinfoFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_transinfo, container, false)
        val more = view.findViewById<TextView>(R.id.more)
        more.setMovementMethod(LinkMovementMethod.getInstance())
        return view


    }


}// Required empty public constructor
