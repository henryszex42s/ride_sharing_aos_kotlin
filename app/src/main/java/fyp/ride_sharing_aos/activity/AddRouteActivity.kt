package fyp.ride_sharing_aos.activity

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
import android.graphics.Color
import com.google.android.gms.maps.model.PolylineOptions
import fyp.ride_sharing_aos.model.Room
import fyp.ride_sharing_aos.tools.FirebaseManager
import fyp.ride_sharing_aos.tools.Tools


class AddRouteActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    var start_Location = LatLng(0.0,0.0)
    var des_Location = LatLng(0.0,0.0)

    //Info for room object
    var starting_Place =""
    var des_Place =""
    var numOfPeople = 0
    var createtime = 0
    var prefertime = 0
    var m_Only = false
    var f_Only = false

    //Filter Value
    val options = arrayOf("Male Only","Female Only", "Student Only", "Staff Only")
    var isCheck = booleanArrayOf(false,false,false,false)
    var yourChoices : MutableList<Int> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_route)
        supportActionBar?.hide()

        /* Declare map*/
        val mapFragment = fragmentManager
                .findFragmentById(R.id.map) as MapFragment
        mapFragment.getMapAsync(this)

        setUpFilter()
        setUpSpinner()


        create.setOnClickListener{
            showProgressDialog(getString(R.string.progress_loading))
            var room = Room(starting_Place, des_Place, numOfPeople, m_Only, f_Only, createtime, prefertime,roomname.text.toString(), "","","","")
            FirebaseManager.createRoom(room, {afterCreateRoom()})
        }
  }


    fun afterCreateRoom()
    {
        dismissProgressDialog()
        if(!FirebaseManager.UserObj!!.chatsession.equals(""))
        {
            Toast.makeText(this,
                    "Room is created" ,
                    Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,
                    "Room is not created" ,
                    Toast.LENGTH_SHORT).show();
        }
    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //Add a marker in UST (Default map position is UST)
        val ust = LatLng(22.336397, 114.265506)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ust))
    }

    fun setUpFilter()
    {

        filter.setOnClickListener{

            val filter_alert = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.filter_dialog, null)
            filter_alert.setView(dialogView)

            filter_alert.setTitle("Filter (Optional)")

            // radio button: male only, female only
            val radio = dialogView.findViewById<RadioGroup>(R.id.gender_only)
            radio.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
                override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
                    // checkedId is the RadioButton selected
                    if(checkedId == R.id.none) {
                        m_Only = false
                        f_Only = false

                        Toast.makeText(this@AddRouteActivity, "None: "+ m_Only + f_Only, Toast.LENGTH_SHORT).show()
                    }
                    if(checkedId == R.id.m_only) {
                        m_Only = true
                        f_Only = false

                        Toast.makeText(this@AddRouteActivity, "Male Only: "+ m_Only + f_Only, Toast.LENGTH_SHORT).show()
                    }
                    if(checkedId == R.id.f_only) {
                        m_Only = false
                        f_Only = true

                        Toast.makeText(this@AddRouteActivity, "female only: "+ m_Only + f_Only, Toast.LENGTH_SHORT).show()
                    }

                }
            })

            // seekbar: #passengers
            var seek = dialogView.findViewById<SeekBar>(R.id.pplseek)
            var seektext = dialogView.findViewById<TextView>(R.id.seektext)

            seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    seektext.text = "Number of passenger: $progress"
                }

                override fun onStartTrackingTouch(arg0: SeekBar) {
                    //do something

                    // TODO Auto-generated method stub
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    //do something
                }
            })

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
                            "You Chose ",
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
    fun setUpSpinner()
    {
        /* Spinner */
        val spinnerItems = resources.getStringArray(R.array.start_choices)
        // Start_spinner
        start_spin.adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, spinnerItems)
        start_spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                // start: UST
                when {
                    parent.getSelectedItem().toString() == "HKUST" -> {
                        start_Location = Tools.coordinate_ust
                        starting_Place = resources.getString(R.string.location_HKUST_North)
                        mMap.addMarker(MarkerOptions().position(Tools.coordinate_ust).title("Marker in HKU"))
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(Tools.coordinate_ust))
                    }
                // start: Choi Hung
                    parent.getSelectedItem().toString() == "Choi Hung MTR station" -> {
                        start_Location = Tools.coordinate_ch
                        starting_Place = resources.getString(R.string.location_Choi_Hung)
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(Tools.coordinate_ch))
                        mMap.addMarker(MarkerOptions().position(Tools.coordinate_ch).title("Marker in Choi Hung MTR station"))
                    }
                // start: Hang Hau
                    parent.getSelectedItem().toString() == "Hang Hau MTR station" -> {
                        start_Location = Tools.coordinate_hh
                        starting_Place = resources.getString(R.string.location_Hang_Hau)
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(Tools.coordinate_hh))
                        mMap.addMarker(MarkerOptions().position(Tools.coordinate_hh).title("Marker in Hang Hau MTR Station"))
                    }
                // start: TKO
                    parent.getSelectedItem().toString() == "Tseung Kwan O MTR station" -> {
                        start_Location = Tools.coordinate_tko
                        starting_Place = resources.getString(R.string.location_Ngau_Tau_Kok)
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(Tools.coordinate_tko))
                        mMap.addMarker(MarkerOptions().position(Tools.coordinate_tko).title("Marker in Tseung Kwan O MTR station"))
                    }
                }

                if(!des_Location.equals(start_Location))
                {
                    writeLineInMap(start_Location,des_Location)
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                /*Do something if nothing selected*/
            }
        }

        //des_spinner
        val spinnerItems2 = resources.getStringArray(R.array.des_choices)

        des_spin.adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, spinnerItems2)
        des_spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                //Des: UST
                if (parent.getSelectedItem().toString() == "HKUST") {
                    des_Location = Tools.coordinate_ust
                    des_Place = resources.getString(R.string.location_HKUST_North)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(Tools.coordinate_ust))
                    mMap.addMarker(MarkerOptions().position(Tools.coordinate_ust).title("Marker in HKUST"))
                }
                //Des: Choi hung
                if (parent.getSelectedItem().toString() == "Choi Hung MTR station") {
                    des_Location = Tools.coordinate_ch
                    des_Place = resources.getString(R.string.location_Choi_Hung)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(Tools.coordinate_ch))
                    mMap.addMarker(MarkerOptions().position(Tools.coordinate_ch).title("Marker in Choi Hung MTR station"))
                }
                //Des: Hang Hau
                if (parent.getSelectedItem().toString() == "Hang Hau MTR station") {
                    des_Location = Tools.coordinate_hh
                    des_Place = resources.getString(R.string.location_Hang_Hau)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(Tools.coordinate_hh))
                    mMap.addMarker(MarkerOptions().position(Tools.coordinate_hh).title("Marker in Hang Hau MTR station"))
                }
                //Des: TKO
                if (parent.getSelectedItem().toString() == "Tseung Kwan O MTR station") {
                    des_Location = Tools.coordinate_tko
                    des_Place = resources.getString(R.string.location_Ngau_Tau_Kok)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(Tools.coordinate_tko))
                    mMap.addMarker(MarkerOptions().position(Tools.coordinate_tko).title("Marker in Tseung Kwan O MTR station"))
                }

                if(!des_Location.equals(start_Location))
                {
                    writeLineInMap(start_Location,des_Location)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                /*Do something if nothing selected*/
            }
        }

    }
    fun writeLineInMap(start : LatLng, dest : LatLng)
    {
        mMap.addPolyline(PolylineOptions()
                .add(start,dest)
                .width(5f)
                .color(Color.RED))

    }
}


