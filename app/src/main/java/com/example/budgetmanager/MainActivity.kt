package com.example.budgetmanager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity() {
    private lateinit var emailAddress: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button
    private lateinit var createAcc: TextView
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth
        initializeViews()
        createAcc.setOnClickListener {
            val intent = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
        loginButton.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        if (emailAddress.text.toString().isEmpty() || password.text.toString().isEmpty()) {
            Toast.makeText(this, "Credentials cannot be empty!", Toast.LENGTH_SHORT).show()
        } else {
            var email: String = emailAddress.text.toString().replace(" ","")
            var password: String = password.text.toString()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        if (auth.currentUser!!.isEmailVerified) {
                            val sharedPref =
                                getSharedPreferences("loginStatus", Context.MODE_PRIVATE)
                            val editor = sharedPref.edit()
                            editor.apply {
                                putBoolean("status_login", true) //user is logged in.
                                apply()
                            }
//                            val Name:String = currentUsersName()
//                            homeIntent.putExtra("name",Name.toString())
                            var homeIntent = Intent(this@MainActivity, homeScreen::class.java)
                            startActivity(homeIntent)
                        } else {
                            Toast.makeText(this, "Your email is not verified!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(this, "Invalid credentials! Try again.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                .addOnFailureListener() {
                    Toast.makeText(
                        this,
                        "Error occurred: ${it.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }


    private fun initializeViews(){
        emailAddress = findViewById(R.id.emailAdd)
        password = findViewById(R.id.password)
        loginButton = findViewById(R.id.button)
        createAcc = findViewById(R.id.textView2)
    }
}