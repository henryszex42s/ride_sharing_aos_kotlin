package fyp.ride_sharing_aos.activity

import android.os.Bundle
import android.support.v7.app.AlertDialog
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
import com.jaredrummler.materialspinner.MaterialSpinner
import fyp.ride_sharing_aos.BaseActivity
import fyp.ride_sharing_aos.tools.FirebaseManager.REQUEST_TYPE_CREATE_ROOM


class AddRouteActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    var start_Location = LatLng(0.0,0.0)
    var des_Location = LatLng(0.0,0.0)
    var default_location = LatLng(0.0,0.0)

    //Info for room object
    var starting_Place =""
    var des_Place =""
    var numOfPeople = 0
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
            if(start_Location == des_Location)
                Toast.makeText(this@AddRouteActivity, "Location must not be the same", Toast.LENGTH_SHORT).show()
            else{
                showProgressDialog(getString(R.string.progress_loading))
                val room = Room(REQUEST_TYPE_CREATE_ROOM,FirebaseManager.getUserID(),"",starting_Place, des_Place, numOfPeople, m_Only, f_Only, Tools.currentTime.time, Tools.currentTime.time,roomname.text.toString(), "","","","")
                FirebaseManager.createRoom(room, {afterCreateRoom()})
            }
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

       // val spinnerItems = resources.getStringArray(R.array.start_choices)
        /*start_spin.adapter = ArrayAdapter<String>(this,
               android.R.layout.simple_list_item_1, spinnerItems)
        start_spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

    }
*/
        // Start_spinner
        val start_spinner = findViewById<MaterialSpinner>(R.id.start_spin)

        start_spinner.setItems("",resources.getString(R.string.location_HKUST_North), resources.getString(R.string.location_HKUST_South),resources.getString(R.string.location_Diamond_Hill),resources.getString(R.string.location_Choi_Hung),resources.getString(R.string.location_Hang_Hau), resources.getString(R.string.location_Ngau_Tau_Kok))
        start_spinner.setOnItemSelectedListener { view, position, id, item ->
            when {
            // start: USTNG
                position == 1 -> {
                    start_Location = Tools.coordinate_ustng
                    starting_Place = resources.getString(R.string.location_HKUST_North)
                    mMap.addMarker(MarkerOptions().position(Tools.coordinate_ustng).title("Marker in HKUST NG"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(Tools.coordinate_ustng))
                }
            // start: USTSG
                position == 2 -> {
                    start_Location = Tools.coordinate_ustsg
                    starting_Place = resources.getString(R.string.location_HKUST_South)
                    mMap.addMarker(MarkerOptions().position(Tools.coordinate_ustsg).title("Marker in HKUST SG"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(Tools.coordinate_ustsg))
                }
            // start: dh
                position == 3 -> {
                    start_Location = Tools.coordinate_dh
                    starting_Place = resources.getString(R.string.location_Diamond_Hill)
                    mMap.addMarker(MarkerOptions().position(Tools.coordinate_dh).title("Marker in dh"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(Tools.coordinate_dh))
                }
            // start: Choi Hung
                position == 4  -> {
                    start_Location = Tools.coordinate_ch
                    starting_Place = resources.getString(R.string.location_Choi_Hung)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(Tools.coordinate_ch))
                    mMap.addMarker(MarkerOptions().position(Tools.coordinate_ch).title("Marker in Choi Hung MTR station"))
                }
            // start: Hang Hau
                position == 5 -> {
                    start_Location = Tools.coordinate_hh
                    starting_Place = resources.getString(R.string.location_Hang_Hau)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(Tools.coordinate_hh))
                    mMap.addMarker(MarkerOptions().position(Tools.coordinate_hh).title("Marker in Hang Hau MTR Station"))
                }
            // start: NTK
                position == 6 -> {
                    start_Location = Tools.coordinate_ntk
                    starting_Place = resources.getString(R.string.location_Ngau_Tau_Kok)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(Tools.coordinate_ntk))
                    mMap.addMarker(MarkerOptions().position(Tools.coordinate_ntk).title("Marker in NTK MTR station"))
                }
            }

            if((start_Location != default_location) && des_Location != default_location)
                writeLineInMap(start_Location,des_Location)

        }



        //des_spinner
        val des_spinner = findViewById<MaterialSpinner>(R.id.des_spin)

//      des_spinner.setItems("","HKUST North Gate", "HKUST South Gate","Diamond Hill MTR_Station","Choi Hung MTR Station","Hang Hau_MTR Station", "Ngau Tau Kok MTR Station")

        des_spinner.setItems("",resources.getString(R.string.location_HKUST_North), resources.getString(R.string.location_HKUST_South),resources.getString(R.string.location_Diamond_Hill),resources.getString(R.string.location_Choi_Hung),resources.getString(R.string.location_Hang_Hau), resources.getString(R.string.location_Ngau_Tau_Kok))
        des_spinner.setOnItemSelectedListener { view, position, id, item ->

            when {
            // start: USTNG
                position == 1 -> {
                    des_Location = Tools.coordinate_ustng
                    des_Place = resources.getString(R.string.location_HKUST_North)
                    mMap.addMarker(MarkerOptions().position(Tools.coordinate_ustng).title("Marker in HKUST NG"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(Tools.coordinate_ustng))
                }
            // start: USTSG
                position == 2 -> {
                    des_Location = Tools.coordinate_ustsg
                    des_Place = resources.getString(R.string.location_HKUST_South)
                    mMap.addMarker(MarkerOptions().position(Tools.coordinate_ustsg).title("Marker in HKUST SG"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(Tools.coordinate_ustsg))
                }
            // start: dh
                position == 3 -> {
                    des_Location = Tools.coordinate_dh
                    des_Place = resources.getString(R.string.location_Diamond_Hill)
                    mMap.addMarker(MarkerOptions().position(Tools.coordinate_dh).title("Marker in dh"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(Tools.coordinate_dh))
                }
            // start: Choi Hung
                position == 4 -> {
                    des_Location = Tools.coordinate_ch
                    des_Place = resources.getString(R.string.location_Choi_Hung)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(Tools.coordinate_ch))
                    mMap.addMarker(MarkerOptions().position(Tools.coordinate_ch).title("Marker in Choi Hung MTR station"))
                }
            // start: Hang Hau
                position == 5 -> {
                    des_Location = Tools.coordinate_hh
                    des_Place = resources.getString(R.string.location_Hang_Hau)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(Tools.coordinate_hh))
                    mMap.addMarker(MarkerOptions().position(Tools.coordinate_hh).title("Marker in Hang Hau MTR Station"))
                }
            // start: NTK
                position == 6 -> {
                    des_Location = Tools.coordinate_ntk
                    des_Place = resources.getString(R.string.location_Ngau_Tau_Kok)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(Tools.coordinate_ntk))
                    mMap.addMarker(MarkerOptions().position(Tools.coordinate_ntk).title("Marker in NTK MTR station"))
                }
            }
            if((start_Location != default_location) && des_Location != default_location)
                writeLineInMap(start_Location,des_Location)
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


