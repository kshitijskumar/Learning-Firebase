package com.example.learningfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_logged_in.*

class LoggedInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged_in)

        auth= FirebaseAuth.getInstance()
        Toast.makeText(this,"Current user: ${auth.currentUser}",Toast.LENGTH_SHORT).show()

        btnSignOut.setOnClickListener {
            auth.signOut()
            finish()
        }

    }
}