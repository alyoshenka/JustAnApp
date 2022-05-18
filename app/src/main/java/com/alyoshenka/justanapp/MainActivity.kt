package com.alyoshenka.justanapp

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
// import android.R.attr.password
import android.content.Intent
import android.content.SharedPreferences
import android.text.TextUtils
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.AuthResult
// import com.google.firebase.auth.ktx.auth
// import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    lateinit var displayname : TextView
    lateinit var email : TextView
    lateinit var password : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bl = findViewById<Button>(R.id.button_bl)
        val br = findViewById<Button>(R.id.button_br)

        displayname = findViewById<TextView>(R.id.username)
        email = findViewById<TextView>(R.id.email)
        password = findViewById<TextView>(R.id.password)
        // populate
        var pref = getPreference(DISPLAYNAME)
        if(pref != null) { displayname.text = pref }
        pref = getPreference(EMAIL)
        if(pref != null) { email.text = pref }
        pref = getPreference(PASSWORD)
        if(pref != null) { password.text = pref }

        bl.setOnClickListener { startActivity(Intent(this, TrafficCameraMapActivity::class.java)) }
        br.setOnClickListener { Toast.makeText(this, resources.getString(R.string.button_br), Toast.LENGTH_SHORT).show() }
    }

    fun viewMovies(view : View) {
        intent = Intent(this, ViewMoviesActivity::class.java)
        startActivity(intent)
    }

    fun viewCams(view : View) {
        val connection : ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activityInfo = connection.activeNetworkInfo
        if(activityInfo != null && activityInfo.isConnected) {
            intent = Intent(this, CamsActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show()
        }
    }

    private fun validateDisplayname(input : String) : Boolean {
        return input.trim().length > 0
    }

    private fun validateEmail(input : String) : Boolean {
        return !TextUtils.isEmpty(input) && android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()
    }

    private fun validatePassword(input : String) : Boolean {
        var trimmed = input.trim()
        if (trimmed.length < 6) { return false }
        if(trimmed.contains(' ')) { return false }
        return true
    }

    fun signIn(view: View) {
        // 1 - validate display name, email, and password entries
        if(!validateDisplayname(displayname.text.toString())) {
            Toast.makeText(this, "Invalid display name", Toast.LENGTH_LONG).show()
            return
        }
        if(!validateEmail(email.text.toString())) {
            Toast.makeText(this, "Invalid email", Toast.LENGTH_LONG).show()
            return
        }
        if(!validatePassword(password.text.toString())) {
            Toast.makeText(this, "Invalid password ", Toast.LENGTH_LONG).show()
            return
        }
        Log.d("FIREBASE", "signIn")


        // Log.d("FIREBASE", "user: " + email)
        // Log.d("FIREBASE", "password: " + password)


        // 2 - save valid entries to shared preferences
        savePreference(DISPLAYNAME, displayname.text.toString())
        savePreference(EMAIL, email.text.toString())
        savePreference(PASSWORD, password.text.toString())
        Log.d("PREFERENCE", "Saved display name: " + displayname.text.toString())
        Log.d("PREFERENCE", "Saved email: " + email.text.toString())
        Log.d("PREFERENCE", "Saved password: " + password.text.toString())

        // 3 - sign into Firebase
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        mAuth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this@MainActivity, OnCompleteListener<AuthResult> { task ->
                Log.d("FIREBASE", "signIn:onComplete:" + task.isSuccessful)
                if (task.isSuccessful) {
                    // update profile. displayname is the value entered in UI
                    val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
                    val profileUpdates: UserProfileChangeRequest = UserProfileChangeRequest.Builder()
                        .setDisplayName(displayname.text.toString())
                        .build()
                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener(OnCompleteListener<Void?> { task ->
                            if (task.isSuccessful) {
                                Log.d("FIREBASE", "User profile updated.")
                                // Go to FirebaseActivity
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        FirebaseActivity::class.java
                                    )
                                )
                            }
                        })
                } else {
                    Log.d("FIREBASE", "sign-in failed")
                    Toast.makeText(
                        this@MainActivity, "Sign In Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun savePreference(title : String, data : String) {
        val prefs = this@MainActivity.getSharedPreferences("com.alyoshenka.justanapp", 0)
        val editor = prefs.edit()
        editor.putString(title, data)
        editor.commit()
    }

    private fun getPreference(name : String) : String? {
        return this@MainActivity.getSharedPreferences("com.alyoshenka.justanapp", 0).getString(name, "")
    }

    companion object {
        val DISPLAYNAME = "displayname"
        val EMAIL = "email"
        val PASSWORD = "password"
    }
}