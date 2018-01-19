package fyp.ride_sharing_aos.fragement

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter

import fyp.ride_sharing_aos.R
import kotlinx.android.synthetic.main.fragment_add_route.*
import android.content.DialogInterface
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.widget.Toast
import java.nio.file.Files.size
import java.nio.file.Files.size









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
       override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val spinnerItems = resources.getStringArray(R.array.start_choices)
            start_spin.adapter = ArrayAdapter<String>(activity,
                    android.R.layout.simple_list_item_1, spinnerItems)
           start_spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    /*Do something if nothing selected*/
                }
            }


           val spinnerItems2 = resources.getStringArray(R.array.des_choices)
           des_spin.adapter = ArrayAdapter<String>(activity,
                   android.R.layout.simple_list_item_1, spinnerItems2)
           des_spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
               override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
               }

               override fun onNothingSelected(parent: AdapterView<*>) {
                   /*Do something if nothing selected*/
               }

           }

           val options = arrayOf("Male Only","Female Only", "Student Only", "Staff Only")
           var isCheck = booleanArrayOf(false,false,false,false)
          var yourChoices : MutableList<Int> = arrayListOf()
           filter.setOnClickListener{
               val filter_alert = AlertDialog.Builder(activity)
               filter_alert.setTitle("Filter (Optional)")
               filter_alert.setMultiChoiceItems(options, isCheck, DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                   if (isChecked) {
                       yourChoices.add(which)
                   } else {
                       yourChoices.remove(which)
                   }

               })

               filter_alert.setCancelable(true)
               filter_alert.setPositiveButton(
                       "OK"
               ) { dialog, id ->

                   val size = yourChoices.size
                   var str = ""
                   if(size > 0) {
                       for (i in 0 until size) {
                           str += options[yourChoices[i]] + ", "
                       }
                       Toast.makeText(activity,
                               "You Chose " + str,
                               Toast.LENGTH_SHORT).show();
                   }

               }
               filter_alert.setNegativeButton(
                       "Reset"
               ) { dialog, id ->
                   isCheck = booleanArrayOf(false,false,false,false)
                   yourChoices.clear()
               }
               val alert = filter_alert.create()
               alert.show()
           }


        }



    override fun onStart() {
        super.onStart()
    }


}// Required empty public constructor
