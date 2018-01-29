package fyp.ride_sharing_aos.fragement

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceFragment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import fyp.ride_sharing_aos.R
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_language.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LanguageFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LanguageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LanguageFragment : Fragment(){

    //PreferenceFragment
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // same as filter, doesnt work

        val v = layoutInflater.inflate(R.layout.fragment_language, null)
        var seek = v.findViewById<RadioGroup>(R.id.language)
        seek.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
           override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
                // checkedId is the RadioButton selected

                 Toast.makeText(context, "chinese", Toast.LENGTH_SHORT).show()

            }
        })


    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_language, container, false)
    }


}// Required empty public constructor
