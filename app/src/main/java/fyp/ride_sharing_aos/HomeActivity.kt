package fyp.ride_sharing_aos

import android.app.Notification
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import fyp.ride_sharing_aos.activity.GetStartActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import android.content.DialogInterface
import android.graphics.Color
import android.support.design.widget.BottomNavigationView
import fyp.ride_sharing_aos.activity.AddRouteActivity
import fyp.ride_sharing_aos.activity.BaseActivity
import fyp.ride_sharing_aos.fragement.*
import fyp.ride_sharing_aos.tools.FirebaseManager
import android.support.v7.widget.CardView
import android.widget.Button
import android.graphics.Color.parseColor
import android.view.View
import android.widget.Toast
import fyp.ride_sharing_aos.R.id.card_view
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.nav_filter.*


class HomeActivity : BaseActivity(){

    var fbAuth = FirebaseAuth.getInstance()

    val homeFragment = HomeFragment()
    val transFragment = TransinfoFragment()
    val settingsFragment = SettingFragment()

    val langFragment = LanguageFragment()
    val notificationFragment = NotificationFragment()
    val edit_profileFragment = EditProfileFragment()

    // slide menu array
    val s_clicked_array: BooleanArray = booleanArrayOf(false,false,false,false,false,false)
    val d_clicked_array: BooleanArray = booleanArrayOf(false,false,false,false,false,false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        showProgressDialog("Loading......")
        loadData()
        initView()
        slideFilter()
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

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.home, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        when (item.itemId) {
//            R.id.action_settings -> return true
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        // Handle navigation view item clicks here.
//        when (item.itemId) {
//            R.id.nav_setting -> {
//
//            }
//            R.id.nav_help -> {
//
//            }
//            R.id.nav_about -> {
//
//            }
//            R.id.nav_feedback -> {
//
//            }
//            R.id.nav_logout -> {
//                val builder_logout = AlertDialog.Builder(this@HomeActivity)
//                builder_logout.setMessage("Are You sure to logout?")
//                builder_logout.setCancelable(true)
//                builder_logout.setTitle("Logout")
//
//                val logout = DialogInterface.OnClickListener { dialog, which ->
//                    fbAuth.signOut()
//                     val intent = Intent(this@HomeActivity, GetStartActivity::class.java)
//                     startActivity(intent)
//                     finish()
//                }
//                builder_logout.setPositiveButton(
//                        "Logout",logout
//                )
//                builder_logout.setNegativeButton(
//                        "Cancel"
//                ) { dialog, id -> dialog.cancel() }
//
//                val alert11 = builder_logout.create()
//                alert11.show()
//
//            }
//        }
//        drawer_layout.closeDrawer(GravityCompat.START)
//        return true
//    }


    private fun loadData()
    {
        FirebaseManager.updateRoomListListener({dismissProgressDialog()})

        if(fbAuth.currentUser != null)
        {
            //User is logged in
            FirebaseManager.updateUser()
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
    }


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.action_home -> {
                replaceFragment(homeFragment,R.id.fragment_content)
                return@OnNavigationItemSelectedListener true
            }

            R.id.action_addroute -> {
                val intent = Intent(this@HomeActivity, AddRouteActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_up)
                return@OnNavigationItemSelectedListener true
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


    fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int){
        supportFragmentManager.inTransaction { add(frameId, fragment) }
    }


    fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
        supportFragmentManager.inTransaction{replace(frameId, fragment)}
    }


    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun slideFilter()
    {


        var s_cardview = findViewById<CardView>(R.id.card_s_ustng)
        var s_cardview2 = findViewById<CardView>(R.id.card_s_ustsg)
        var s_cardview3 = findViewById<CardView>(R.id.card_s_dh)
        var s_cardview4 = findViewById<CardView>(R.id.card_s_ch)
        var s_cardview5 = findViewById<CardView>(R.id.card_s_ntk)
        var s_cardview6 = findViewById<CardView>(R.id.card_s_hh)

        var d_cardview = findViewById<CardView>(R.id.card_d_ustng)
        var d_cardview2 = findViewById<CardView>(R.id.card_d_ustsg)
        var d_cardview3 = findViewById<CardView>(R.id.card_d_dh)
        var d_cardview4 = findViewById<CardView>(R.id.card_d_ch)
        var d_cardview5 = findViewById<CardView>(R.id.card_d_ntk)
        var d_cardview6 = findViewById<CardView>(R.id.card_d_hh)





        s_cardview.setOnClickListener(View.OnClickListener {
            if(s_clicked_array[0] == false) {
                s_cardview.setCardBackgroundColor(Color.parseColor("#e91e63"))
                s_clicked_array[0] = true
            }
            else {
                s_cardview.setCardBackgroundColor(Color.parseColor("#FF424242"))
                s_clicked_array[0] = false
            }
        })
        s_cardview2.setOnClickListener(View.OnClickListener {

            if(s_clicked_array[1] == false) {
                s_cardview2.setCardBackgroundColor(Color.parseColor("#e91e63"))
                s_clicked_array[1] = true
            }
            else {
                s_cardview2.setCardBackgroundColor(Color.parseColor("#FF424242"))
                s_clicked_array[1] = false
            }
        })
        s_cardview3.setOnClickListener(View.OnClickListener {

            if(s_clicked_array[2] == false) {
                s_cardview3.setCardBackgroundColor(Color.parseColor("#e91e63"))
                s_clicked_array[2] = true
            }
            else {
                s_cardview3.setCardBackgroundColor(Color.parseColor("#FF424242"))
                s_clicked_array[2] = false
            }
        })
        s_cardview4.setOnClickListener(View.OnClickListener {

            if(s_clicked_array[3] == false) {
                s_cardview4.setCardBackgroundColor(Color.parseColor("#e91e63"))
                s_clicked_array[3] = true
            }
            else {
                s_cardview4.setCardBackgroundColor(Color.parseColor("#FF424242"))
                s_clicked_array[3] = false
            }
        })
        s_cardview5.setOnClickListener(View.OnClickListener {

            if(s_clicked_array[4] == false) {
                s_cardview5.setCardBackgroundColor(Color.parseColor("#e91e63"))
                s_clicked_array[4] = true
            }
            else {
                s_cardview5.setCardBackgroundColor(Color.parseColor("#FF424242"))
                s_clicked_array[4] = false
            }
        })
        s_cardview6.setOnClickListener(View.OnClickListener {

            if(s_clicked_array[5] == false) {
                s_cardview6.setCardBackgroundColor(Color.parseColor("#e91e63"))
                s_clicked_array[5] = true
            }
            else {
                s_cardview6.setCardBackgroundColor(Color.parseColor("#FF424242"))
                s_clicked_array[5] = false
            }
        })


        d_cardview.setOnClickListener(View.OnClickListener {
            if(d_clicked_array[0] == false) {
                d_cardview.setCardBackgroundColor(Color.parseColor("#e91e63"))
                d_clicked_array[0] = true
            }
            else {
                d_cardview.setCardBackgroundColor(Color.parseColor("#FF424242"))
                d_clicked_array[0] = false
            }
        })
        d_cardview2.setOnClickListener(View.OnClickListener {

            if(d_clicked_array[1] == false) {
                d_cardview2.setCardBackgroundColor(Color.parseColor("#e91e63"))
                d_clicked_array[1] = true
            }
            else {
                d_cardview2.setCardBackgroundColor(Color.parseColor("#FF424242"))
                d_clicked_array[1] = false
            }
        })
        d_cardview3.setOnClickListener(View.OnClickListener {

            if(d_clicked_array[2] == false) {
                d_cardview3.setCardBackgroundColor(Color.parseColor("#e91e63"))
                d_clicked_array[2] = true
            }
            else {
                d_cardview3.setCardBackgroundColor(Color.parseColor("#FF424242"))
                d_clicked_array[2] = false
            }
        })
        d_cardview4.setOnClickListener(View.OnClickListener {

            if(d_clicked_array[3] == false) {
                d_cardview4.setCardBackgroundColor(Color.parseColor("#e91e63"))
                d_clicked_array[3] = true
            }
            else {
                d_cardview4.setCardBackgroundColor(Color.parseColor("#FF424242"))
                d_clicked_array[3] = false
            }
        })
        d_cardview5.setOnClickListener(View.OnClickListener {

            if(d_clicked_array[4] == false) {
                d_cardview5.setCardBackgroundColor(Color.parseColor("#e91e63"))
                d_clicked_array[4] = true
            }
            else {
                d_cardview5.setCardBackgroundColor(Color.parseColor("#FF424242"))
                d_clicked_array[4] = false
            }
        })
        d_cardview6.setOnClickListener(View.OnClickListener {

            if(d_clicked_array[5] == false) {
                d_cardview6.setCardBackgroundColor(Color.parseColor("#e91e63"))
                d_clicked_array[5] = true
            }
            else {
                d_cardview6.setCardBackgroundColor(Color.parseColor("#FF424242"))
                d_clicked_array[5] = false
            }
        })


        filter_reset.setOnClickListener(View.OnClickListener {
            Toast.makeText(this,
                    "reset Test" ,
                    Toast.LENGTH_SHORT).show();

        })
        filter_submit.setOnClickListener(View.OnClickListener {
            Toast.makeText(this,
                    "submit Test" ,
                    Toast.LENGTH_SHORT).show();
        })

    }

}


