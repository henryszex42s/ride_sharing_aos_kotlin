package fyp.ride_sharing_aos.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.*
import fyp.ride_sharing_aos.R
import android.widget.SeekBar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.activity_add_route.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.MapFragment
import android.R.attr.apiKey



class AddRouteActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_route)
        supportActionBar?.hide()

        spinner()
        filter()

        /* Declare map*/
        val mapFragment = fragmentManager
                .findFragmentById(R.id.map) as MapFragment
        mapFragment.getMapAsync(this)


  }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //add a marker in UST
        val ust = LatLng(22.336397, 114.265506)
        mMap.addMarker(MarkerOptions().position(ust).title("Marker in HKUST"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ust))

        //add a marker in CH MTR
        val ch = LatLng(22.3349716,114.2085751)
        mMap.addMarker(MarkerOptions().position(ch).title("Marker in Choi Hung MTR Station"))

        //add a marker in HH MTR
        val hh = LatLng(22.3156009,114.262199)
        mMap.addMarker(MarkerOptions().position(hh).title("Marker in Hang Hau MTR Station"))


/*
        val context = GeoApiContext.Builder()
                .apiKey("AIzaSyDUCJ7mosxKtkDesQQNqcmvnMn9e4cqDco")
                .build()*/
    }


    fun spinner(){

        /* spinner */
        val spinnerItems = resources.getStringArray(R.array.start_choices)
        start_spin.adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, spinnerItems)
        start_spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                /*Do something if nothing selected*/
            }
        }


        val spinnerItems2 = resources.getStringArray(R.array.des_choices)
        des_spin.adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, spinnerItems2)
        des_spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                /*Do something if nothing selected*/
            }

        }
    }
    fun filter(){

        /*filter*/
        val options = arrayOf("Male Only","Female Only", "Student Only", "Staff Only")
        var isCheck = booleanArrayOf(false,false,false,false)
        var yourChoices : MutableList<Int> = arrayListOf()

        filter.setOnClickListener{

            val filter_alert = AlertDialog.Builder(this)
            filter_alert.setView(R.layout.filter_dialog)

            filter_alert.setTitle("Filter (Optional)")
/*
          val seek = SeekBar(this)
          seek.max = 4
          seek.keyProgressIncrement = 1
          filter_alert.setView(seek)   */


            val v = layoutInflater.inflate(R.layout.filter_dialog, null)
            var seek = v.findViewById<SeekBar>(R.id.pplseek)
            var temp2 = v.findViewById<TextView>(R.id.seektext)

            seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    temp2.text = "Number of passenger: $progress"
                }


                override fun onStartTrackingTouch(arg0: SeekBar) {
                    //do something

                    // TODO Auto-generated method stub
                }


                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    //do something

                }
            })





/*
          filter_alert.setMultiChoiceItems(options, isCheck, DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
              if (isChecked) {
                  yourChoices.add(which)
              } else {
                  yourChoices.remove(which)
              }

          })*/

            filter_alert.setPositiveButton(
                    "OK"
            ) { dialog, id ->

                val size = yourChoices.size
                var str = ""
                if(size > 0) {
                    for (i in 0 until size) {
                        str += options[yourChoices[i]] + ", "
                    }
                    Toast.makeText(this,
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

            filter_alert.setCancelable(true)
            val alert = filter_alert.create()
            alert.show()

        }

    }
}
