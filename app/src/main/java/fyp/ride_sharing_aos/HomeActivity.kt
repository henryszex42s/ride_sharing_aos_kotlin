package fyp.ride_sharing_aos

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import fyp.ride_sharing_aos.activity.GetStartActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import android.graphics.Color
import android.support.design.widget.BottomNavigationView
import fyp.ride_sharing_aos.activity.AddRouteActivity
import fyp.ride_sharing_aos.fragement.*
import fyp.ride_sharing_aos.tools.FirebaseManager
import android.support.v7.widget.CardView
import android.view.View
import android.widget.Toast
import fyp.ride_sharing_aos.activity.ChatroomActivity
import fyp.ride_sharing_aos.tools.FirebaseManager.destinationFilterValue
import fyp.ride_sharing_aos.tools.FirebaseManager.genderFilterValue
import fyp.ride_sharing_aos.tools.FirebaseManager.minPassengersFilterValue
import fyp.ride_sharing_aos.tools.FirebaseManager.startingFilterValue
import fyp.ride_sharing_aos.tools.Tools
import kotlinx.android.synthetic.main.nav_filter.*


class HomeActivity : BaseActivity(){

    private val TAG = "HomeActivity"
    var fbAuth = FirebaseAuth.getInstance()

    val homeFragment = HomeFragment()
    val transFragment = TransinfoFragment()
    val settingsFragment = SettingFragment()

    val langFragment = LanguageFragment()
    val notificationFragment = NotificationFragment()
    val edit_profileFragment = EditProfileFragment()

    val startingLocationView = ArrayList<CardView>()
    val destinationView = ArrayList<CardView>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initView()
        loadData()
        selectDrawerItem()
        showLoginPage()
        setDefaultValue()
//        http://blog.csdn.net/mobilexu/article/details/41147417
//        supportActionBar?.setDisplayShowTitleEnabled(false)
//        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()

        }
    }

    override fun onStart() {
        super.onStart()
        if (!checkPermissions()) {
            requestPermissions()
        } else {
            getLastLocation()
        }
    }


    fun setDefaultValue()
    {
        if(Tools.nearestLocation != "")
        {
            when(Tools.nearestLocation)
            {
                getString(R.string.location_HKUST_North) -> startingFilterValue = locationClickBehavior(card_s_ustng,getString(R.string.location_HKUST_North),true)
                getString(R.string.location_HKUST_South) -> startingFilterValue = locationClickBehavior(card_s_ustsg,getString(R.string.location_HKUST_South),true)
                getString(R.string.location_Diamond_Hill) -> startingFilterValue = locationClickBehavior(card_s_dh,getString(R.string.location_Diamond_Hill),true)
                getString(R.string.location_Choi_Hung) -> startingFilterValue = locationClickBehavior(card_s_ch,getString(R.string.location_Choi_Hung),true)
                getString(R.string.location_Hang_Hau) -> startingFilterValue = locationClickBehavior(card_s_hh,getString(R.string.location_Hang_Hau),true)
                getString(R.string.location_Ngau_Tau_Kok) -> startingFilterValue = locationClickBehavior(card_s_ntk,getString(R.string.location_Ngau_Tau_Kok),true)
            }
        }
    }
     fun loadData()
    {
        showProgressDialog(getString(R.string.progress_loading))
        FirebaseManager.updateRoomList({roomListChange()})
    }


     fun roomListChange()
    {
        dismissProgressDialog()
        if(supportFragmentManager.findFragmentById(R.id.fragment_content) == homeFragment)
            homeFragment.DataChange()
    }

    private fun showLoginPage()
    {
        if(FirebaseManager.isLogin() )
        {
                //User is logged in
                FirebaseManager.updateUser()
                        if(!FirebaseManager.isEmailVerified())
                        {
                            val intent = Intent(this@HomeActivity, GetStartActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_up)

                        }
        }
        else
        {
            //User is not logged in
            val intent = Intent(this@HomeActivity, GetStartActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_up)
        }
    }





    private fun initView()
    {
        //Toolbar
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
//        nav_view.setNavigationItemSelectedListener(this)
        buttom_navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_content, homeFragment).commit()

        button_chat.setOnClickListener({
            callChatRoom()
        })
    }


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.action_home -> {
                replaceFragment(homeFragment,R.id.fragment_content)
                loadData()
                return@OnNavigationItemSelectedListener true
            }

            R.id.action_addroute -> {
                val intent = Intent(this@HomeActivity, AddRouteActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_up)
//                return@OnNavigationItemSelectedListener true
            }

            R.id.action_transinfo -> {
                replaceFragment(transFragment,R.id.fragment_content)
                return@OnNavigationItemSelectedListener true
            }

            R.id.action_settings -> {
                replaceFragment(settingsFragment,R.id.fragment_content)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun closeApplication()
    {
        val builder = AlertDialog.Builder(this)
        builder.setIcon(R.drawable.app_icon)
        builder.setMessage(R.string.closeApp)
        builder.setCancelable(false)
        builder.setTitle("")

        builder.setPositiveButton("OK")
        {
            dialog, id -> dialog.cancel()
            finish()
        }
        val alert = builder.create()
        alert.show()
    }

    fun callFragment(fragment: Int)
    {
        when (fragment) {

            1 ->
            {
                replaceFragment(edit_profileFragment,R.id.fragment_content)
            }

            2 ->
            {
                replaceFragment(notificationFragment,R.id.fragment_content)
            }

            3 ->
            {
                replaceFragment(langFragment,R.id.fragment_content)
            }
        }
    }



    fun selectDrawerItem()
    {
        Tools.findTheNearestLocation(this)
        startingLocationView.add(card_s_ustng)
        startingLocationView.add(card_s_ustsg)
        startingLocationView.add(card_s_dh)
        startingLocationView.add(card_s_ch)
        startingLocationView.add(card_s_hh)
        startingLocationView.add(card_s_ntk)

        card_s_ustng.setOnClickListener { nav_starting_filterOnClickListener(card_s_ustng) }
        card_s_ustsg.setOnClickListener { nav_starting_filterOnClickListener(card_s_ustsg) }
        card_s_dh.setOnClickListener { nav_starting_filterOnClickListener(card_s_dh) }
        card_s_ch.setOnClickListener { nav_starting_filterOnClickListener(card_s_ch) }
        card_s_hh.setOnClickListener { nav_starting_filterOnClickListener(card_s_hh) }
        card_s_ntk.setOnClickListener { nav_starting_filterOnClickListener(card_s_ntk) }

        destinationView.add(card_d_ustng)
        destinationView.add(card_d_ustsg)
        destinationView.add(card_d_dh)
        destinationView.add(card_d_ch)
        destinationView.add(card_d_ntk)
        destinationView.add(card_d_hh)

        card_d_ustng.setOnClickListener{ nav_destination_filterOnClickListener(card_d_ustng) }
        card_d_ustsg.setOnClickListener{ nav_destination_filterOnClickListener(card_d_ustsg) }
        card_d_dh.setOnClickListener{ nav_destination_filterOnClickListener(card_d_dh) }
        card_d_ch.setOnClickListener{ nav_destination_filterOnClickListener(card_d_ch) }
        card_d_ntk.setOnClickListener{ nav_destination_filterOnClickListener(card_d_ntk) }
        card_d_hh.setOnClickListener{ nav_destination_filterOnClickListener(card_d_hh) }


        //Set the Default Value as the NearestLocation

        filter_reset.setOnClickListener(View.OnClickListener {
            resetFilterValue()
        })

        filter_submit.setOnClickListener(View.OnClickListener {
            getFilterValue()
            loadData()
        })



        //Please Sync With RoomListAdapter -> ItemOnClickListener
        card_automatch.setOnClickListener(View.OnClickListener {
            if (startingFilterValue != "None" &&  destinationFilterValue != "None")
            {

                showProgressDialog(getString(R.string.progress_matching))
                FirebaseManager.autoMatching({
                    dismissProgressDialog()
                    if(FirebaseManager.AutoMatchRoomIDTemp != null && FirebaseManager.AutoMatchRoomIDTemp != "")
                    {
                        Tools.printLog(TAG,FirebaseManager.AutoMatchRoomIDTemp!!)
                        joinRoom(FirebaseManager.AutoMatchRoomIDTemp!!)
                    }
                    else
                    {
                        val error_msg: ArrayList<String> = ArrayList()
                        error_msg.add(this.getString(R.string.automatch__no_Room))
                        Tools.showDialog(this,getString(R.string.automatch__error),error_msg)

                    }
                })


            }
            else
            {
                val error_msg: ArrayList<String> = ArrayList()
                error_msg.add(this.getString(R.string.automatch_select_error))
                Tools.showDialog(this,getString(R.string.automatch__error),error_msg)
            }

        })

    }


    fun nav_starting_filterOnClickListener(v: CardView)
    {
        for (a in startingLocationView)
        {
            a.setCardBackgroundColor(Color.parseColor("#FF424242"))
        }
        when (v.id) {
            R.id.card_s_ustng -> {
                startingFilterValue = locationClickBehavior(v,getString(R.string.location_HKUST_North),true)
            }

            R.id.card_s_ustsg -> {
                startingFilterValue = locationClickBehavior(v,getString(R.string.location_HKUST_South),true)
            }

            R.id.card_s_dh -> {
                startingFilterValue = locationClickBehavior(v,getString(R.string.location_Diamond_Hill),true)
            }
            R.id.card_s_ch -> {
                startingFilterValue = locationClickBehavior(v,getString(R.string.location_Choi_Hung),true)
            }
            R.id.card_s_hh -> {
                startingFilterValue = locationClickBehavior(v,getString(R.string.location_Hang_Hau),true)
            }
            R.id.card_s_ntk -> {
                startingFilterValue = locationClickBehavior(v,getString(R.string.location_Ngau_Tau_Kok),true)
            }
        }
    }

    fun nav_destination_filterOnClickListener(v: CardView)
    {

        for (a in destinationView)
        {
            a.setCardBackgroundColor(Color.parseColor("#FF424242"))
        }

        when (v.id) {
            R.id.card_d_ustng -> {
                destinationFilterValue = locationClickBehavior(v,getString(R.string.location_HKUST_North),false)

            }

            R.id.card_d_ustsg -> {
                destinationFilterValue = locationClickBehavior(v,getString(R.string.location_HKUST_South),false)

            }

            R.id.card_d_dh -> {
                destinationFilterValue = locationClickBehavior(v,getString(R.string.location_Diamond_Hill),false)

            }
            R.id.card_d_ch -> {
                destinationFilterValue = locationClickBehavior(v,getString(R.string.location_Choi_Hung),false)

            }
            R.id.card_d_hh -> {
                destinationFilterValue = locationClickBehavior(v,getString(R.string.location_Hang_Hau),false)

            }
            R.id.card_d_ntk -> {
                destinationFilterValue = locationClickBehavior(v,getString(R.string.location_Ngau_Tau_Kok),false)

            }
        }
    }

    fun locationClickBehavior(v: CardView, currentValue: String, direction: Boolean) : String
    {
        lateinit var result : String
        if(direction)
        {
            result = startingFilterValue
        }
        else
        {
            result = destinationFilterValue
        }

        if(currentValue == result)
        {
            result = "None"
        }
        else
        {
            result = currentValue
            v.setCardBackgroundColor(Color.parseColor("#e91e63"))
        }
        return result
    }

    fun getFilterValue()
    {
        genderfilter.setOnClickedButtonPosition()
        {
            position ->
            when(position)
            {
                0 -> genderFilterValue = "None"
                1 -> genderFilterValue = "Male"
                2 -> genderFilterValue = "Female"
            }
        }


        passengerfilter.setOnClickedButtonPosition()
        {
            position ->
            when(position)
            {
                0 -> minPassengersFilterValue = "None"
                1 -> minPassengersFilterValue = "1"
                2 -> minPassengersFilterValue = "2"
                3 -> minPassengersFilterValue = "3"
                4 -> minPassengersFilterValue = "4"
            }
        }

    }


    fun resetFilterValue()
    {
        for (a in startingLocationView)
        {
            a.setCardBackgroundColor(Color.parseColor("#FF424242"))
        }
        for (a in destinationView)
        {
            a.setCardBackgroundColor(Color.parseColor("#FF424242"))
        }
        genderfilter.setPosition(0,true)
        passengerfilter.setPosition(0,true)

        startingFilterValue = "None"
        destinationFilterValue = "None"
        genderFilterValue = "None"
        minPassengersFilterValue = "None"
    }




    fun joinRoom(RoomID : String)
    {
        if(FirebaseManager.isRoomIDValid())
        {
            val error_msg: ArrayList<String> = ArrayList()
            error_msg.add(this.getString(R.string.room_join_error_msg))
            Tools.showDialog(this,getString(R.string.room_join_error_title),error_msg)
        }
        else
        {
            showProgressDialog(this.getString(R.string.progress_join))
            FirebaseManager.joinRoom(RoomID,{

                dismissProgressDialog()
                FirebaseManager.detachUserListener()
                callChatRoom()

            })
        }
    }

}


