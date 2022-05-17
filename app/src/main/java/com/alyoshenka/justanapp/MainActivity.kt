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
import android.R.attr.password
import android.content.Intent
import android.content.SharedPreferences
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bl = findViewById<Button>(R.id.button_bl)
        val br = findViewById<Button>(R.id.button_br)

        bl.setOnClickListener { startActivity(Intent(this, TrafficCameraMapActivity::class.java)) }
        br.setOnClickListener { Toast.makeText(this, resources.getString(R.string.button_br), Toast.LENGTH_SHORT).show() }

        auth = Firebase.auth
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

    fun signIn(view: View) {
        Log.d("FIREBASE", "signIn")

        // 1 - validate display name, email, and password entries
        val displayname = findViewById<TextView>(R.id.username).text.toString()
        val email = findViewById<TextView>(R.id.email).text.toString()
        val password = findViewById<TextView>(R.id.password).text.toString()
        Log.d("FIREBASE", "user: " + email)
        Log.d("FIREBASE", "password: " + password)

        // should always save username
        savePreference(DISPLAYNAME, displayname)

        // 2 - save valid entries to shared preferences


        // 3 - sign into Firebase
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this@MainActivity, OnCompleteListener<AuthResult> { task ->
                Log.d("FIREBASE", "signIn:onComplete:" + task.isSuccessful)
                if (task.isSuccessful) {
                    // should only save credentials on success
                    savePreference(PASSWORD, password)
                    savePreference(EMAIL, email)
                    // update profile. displayname is the value entered in UI
                    val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
                    val profileUpdates: UserProfileChangeRequest = UserProfileChangeRequest.Builder()
                        .setDisplayName(displayname)
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