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
import java.util.Locale
import android.content.res.Configuration;
import fyp.ride_sharing_aos.R
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_language.*
import android.util.DisplayMetrics




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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment   // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_language, null)
        val radio = view.findViewById<RadioGroup>(R.id.language)
        radio.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
                // checkedId is the RadioButton selected

                if(checkedId == R.id.chinese) {

                    val resources = activity!!.getResources()
                    val config = resources.getConfiguration()
                    val dm = resources.getDisplayMetrics()
                    config.locale = Locale.TRADITIONAL_CHINESE
                    resources.updateConfiguration(config, dm)

                    Toast.makeText(context, "You chose Chinese", Toast.LENGTH_SHORT).show()
                }
                if(checkedId == R.id.english) {
                    val resources = activity!!.getResources()
                    val config = resources.getConfiguration()
                    val dm = resources.getDisplayMetrics()
                    config.locale = Locale.ENGLISH
                    resources.updateConfiguration(config, dm)

                    Toast.makeText(context, "You chose English", Toast.LENGTH_SHORT).show()
                }

            }
        })


        return view
    }


}// Required empty public constructor
