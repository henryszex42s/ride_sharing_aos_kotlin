package fyp.ride_sharing_aos.activity

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import fyp.ride_sharing_aos.R
import kotlinx.android.synthetic.main.activity_signup.*
import fyp.ride_sharing_aos.HomeActivity
import fyp.ride_sharing_aos.model.User
import fyp.ride_sharing_aos.tools.Tools
import com.google.firebase.firestore.FirebaseFirestore





class SignupActivity : BaseActivity() {

    private val TAG = "SignupActivity"

    private lateinit var mAuth : FirebaseAuth
    private lateinit var db : FirebaseFirestore

//    private lateinit var database: FirebaseDatabase
//    private lateinit var dbRef: DatabaseReference


    private var email : String? = null
    private var password : String? = null
    private var gender : String? = null
    private var identity : String? = null
    private var username: String? = null





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        database = FirebaseDatabase.getInstance()
//        dbRef = database.reference
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


        InitView()

    }

    override fun onResume() {
        super.onResume()
    }

    fun InitView()
    {
        setContentView(R.layout.activity_signup)
        supportActionBar?.hide()

        signup_button.setOnClickListener({
            getDataFromView()

            if (inputValidation())
            {
                mAuth.createUserWithEmailAndPassword(email.toString(), password.toString())
                        .addOnCompleteListener { task: Task<AuthResult> ->
                            if (task.isSuccessful)
                            {
                                val userId = mAuth.currentUser!!.uid
                                val user = User(username, userId, email, identity,gender)

                                db.collection("user")
                                        .document(userId)
                                        .set(user)
                                        .addOnSuccessListener({
                                            Toast.makeText(this, "Your account has been created successfully.", Toast.LENGTH_SHORT).show()
                                            val intent = Intent(this@SignupActivity, HomeActivity::class.java)
                                            startActivity(intent)
                                            this.finish()
                                        })

                                        .addOnFailureListener({
                                                    Toast.makeText(this, "Your account has not been created, please contact us for help.", Toast.LENGTH_SHORT).show()
                                        })

//                                Realtime Database Code
//                                registerRef.setValue(user).addOnSuccessListener()
//                                {
//                                    Toast.makeText(this, "Authentication Success.", Toast.LENGTH_SHORT).show()
//                                    val intent = Intent(this@SignupActivity, HomeActivity::class.java)
//                                    startActivity(intent)
//                                    this.finish()
//                                }
                            }
                            else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                            }
                        }
            }
        })


        type_segmentedButtonGroup.setOnClickedButtonPosition()
        { position ->
            when(position)
            {
                0 -> identity = "Student"
                1 -> identity = "Staff"
            }
        }
        gender_segmentedButtonGroup.setOnClickedButtonPosition()
        { position ->
            when(position)
            {
                0 -> gender = "Male"
                1 -> gender = "Female"
            }
        }


        //Setup the Default Value
        type_segmentedButtonGroup.setPosition(0, 0)
        identity = "Student"

        gender_segmentedButtonGroup.setPosition(0, 0)
        gender = "Male"

    }






    fun getDataFromView()
    {
        password = signup_password.text.toString()
        username = signup_username.text.toString()
        email = signup_itsc.text.toString()

        if(identity.equals("Student"))
        {
            email = email+"@connect.ust.hk"
        }
        else if(identity.equals("Staff"))
        {
            email = email+"@connect.ust.hk"
        }

    }


    //https://kotlinlang.org/docs/reference/functions.html
    fun inputValidation() : Boolean
    {
        val uname  = signup_username.text.toString()
        val password = signup_password.text.toString()
        val error_msg: ArrayList<String> = ArrayList()

        //Reset The ErrorView
        password_require.setTextColor(Color.BLACK)
        username_require.setTextColor(Color.BLACK)


        //Username Validation
        if (  (uname.length < 6 || uname.length > 8) || uname.isEmpty() ) {
            error_msg.add(getString(R.string.signup_username_error))
            username_require.setTextColor(Color.RED)
        }

        //Password Validation
        if (password.length < 6 || password.isEmpty()) {
            error_msg.add(getString(R.string.signup_password_error))
            password_require.setTextColor(Color.RED)
        }

        if ( !password.matches("[0-9]+".toRegex()) && (password.matches("[a-z]+".toRegex()) || password.matches("[A-Z]+".toRegex()))) {
            error_msg.add(getString(R.string.signup_password_error2))
            password_require.setTextColor(Color.RED)
        }

        if(error_msg.isEmpty())
        {
            return true
        }
        else
        {
            Tools.showDialog(this@SignupActivity, "Alert", error_msg)
            return false
        }
    }



}
