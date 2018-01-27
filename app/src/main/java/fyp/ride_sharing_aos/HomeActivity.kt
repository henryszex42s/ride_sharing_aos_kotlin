package fyp.ride_sharing_aos

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
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import fyp.ride_sharing_aos.activity.GetStartActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import android.content.DialogInterface
import android.support.design.widget.BottomNavigationView
import fyp.ride_sharing_aos.activity.AddRouteActivity
import fyp.ride_sharing_aos.activity.BaseActivity
import fyp.ride_sharing_aos.fragement.HomeFragment
import fyp.ride_sharing_aos.fragement.SettingFragment
import fyp.ride_sharing_aos.fragement.TransinfoFragment
import fyp.ride_sharing_aos.tools.FirebaseManager
import fyp.ride_sharing_aos.tools.Tools


class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener{

    var fbAuth = FirebaseAuth.getInstance()
    val homeFragment = HomeFragment()
    val transFragment = TransinfoFragment()
    val settingsFragment = SettingFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        showProgressDialog("Loading......")
        loadData()
        initView()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            closeApplication()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_setting -> {

            }
            R.id.nav_help -> {

            }
            R.id.nav_about -> {

            }
            R.id.nav_feedback -> {

            }
            R.id.nav_logout -> {
                val builder_logout = AlertDialog.Builder(this@HomeActivity)
                builder_logout.setMessage("Are You sure to logout?")
                builder_logout.setCancelable(true)
                builder_logout.setTitle("Logout")

                val logout = DialogInterface.OnClickListener { dialog, which ->
                    fbAuth.signOut()
                     val intent = Intent(this@HomeActivity, GetStartActivity::class.java)
                     startActivity(intent)
                     finish()
                }
                builder_logout.setPositiveButton(
                        "Logout",logout
                )
                builder_logout.setNegativeButton(
                        "Cancel"
                ) { dialog, id -> dialog.cancel() }

                val alert11 = builder_logout.create()
                alert11.show()

            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    private fun loadData()
    {
        FirebaseManager.setRoomListListener({dismissProgressDialog()})

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
        nav_view.setNavigationItemSelectedListener(this)
        buttom_navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        title = "Home"
        replaceFragment(homeFragment,R.id.fragment_content)
    }


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.action_home -> {
                title = "Home"
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
                title = "Transport Info"
                replaceFragment(transFragment,R.id.fragment_content)
                return@OnNavigationItemSelectedListener true
            }

            R.id.action_settings -> {
                title = "Setting"
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
}
