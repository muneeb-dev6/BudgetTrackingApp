package com.example.budgetmanager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var userModel:userModelForFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var pswd1: EditText
    private lateinit var pswd2: EditText
    private lateinit var createUser:Button
    private lateinit var backtoLogin:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
        initializeViews()
        createUser.findViewById<Button>(R.id.getReg).setOnClickListener {
            filterDataAndProceed()
        }
        backtoLogin.setOnClickListener{
            var intent = Intent(this@RegisterActivity,MainActivity::class.java)
            startActivity(intent)
        }
    }
    private fun filterDataAndProceed(){
        val nameReg = """^[A-Za-z\s]+$""".toRegex()
        val passwordReg = """^(?!.*\s).{8,}$""".toRegex()
        val emailReg = """^[A-Za-z\d._%+-]+@[A-Za-z\d.-]+\.[A-Za-z]{2,}$""".toRegex()
        if(name.text.toString().matches(nameReg)){
                if(email.text.toString().matches(emailReg)){
                    if(pswd1.text.toString().matches(passwordReg) && pswd1.text.toString().equals(pswd2.text.toString())){
                            auth.createUserWithEmailAndPassword(email.text.toString(),pswd1.text.toString())
                                .addOnCompleteListener(this) {task ->
                                    if (task.isSuccessful) {
                                        FirebaseAuth.getInstance().currentUser!!.sendEmailVerification().addOnCompleteListener(this){ task ->
                                                if(task.isSuccessful){
                                                    if(auth.currentUser!!.isEmailVerified){
                                                        sendUserData()
                                                        Toast.makeText(this,"Account created successfully.",Toast.LENGTH_SHORT).show()
                                                    }
                                                    else{
                                                        Toast.makeText(this,"Email address needs to be verified.",Toast.LENGTH_SHORT).show()
                                                    }
                                                } else{
                                                    Toast.makeText(this,"Email address not verified.",Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                            .addOnFailureListener(){
                                                Toast.makeText(this,"Error occurred: ${it.localizedMessage}",Toast.LENGTH_SHORT).show()
                                            }
                                    } else {
                                        Toast.makeText(this,"Authentication failed!",Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .addOnFailureListener(){
                                    Toast.makeText(this,"Error occurred: ${it.localizedMessage}",Toast.LENGTH_SHORT).show()
                                    Log.d("Error:",it.localizedMessage)
                                }
                    }
                    else{
                        Toast.makeText(this,"Passwords should match and be at least 8 characters long without whitespaces!",Toast.LENGTH_SHORT).show()
                        return
                    }
                }
                else{
                    Toast.makeText(this,"Email address is not valid.",Toast.LENGTH_SHORT).show()
                    return
                }
        }
        else{
            Toast.makeText(this,"Names can only have alphabets!",Toast.LENGTH_SHORT).show()
            return
        }

    }
    private fun sendUserData(){
        userModel = userModelForFirestore(email.text.toString(), pswd1.text.toString(),name.text.toString())
        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()
        val userDocRef = db.collection("users").document(user!!.uid)
        userDocRef.set(userModel)
    }
    private fun initializeViews(){
        name = findViewById(R.id.enterName)
        email = findViewById(R.id.editTextTextEmailAddress)
        pswd1 = findViewById(R.id.editTextTextPassword3)
        pswd2 = findViewById(R.id.editTextTextPassword4)
        createUser = findViewById(R.id.getReg)
        backtoLogin = findViewById(R.id.accExists)
    }
}