package com.example.learningfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth= FirebaseAuth.getInstance()

        getUserLoggedInState()
        btnSignIn.setOnClickListener {
            registerUser()
        }

        btnLogIn.setOnClickListener {
            logInExistingUser()
        }

        btnCurrentUser.setOnClickListener {
            if(auth.currentUser==null){
                Toast.makeText(this, "No user logged in",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"current user: ${auth.currentUser}",Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun getUserLoggedInState(){
        val currentUser= auth.currentUser
        if(currentUser!=null){
            val intent= Intent(this,LoggedInActivity::class.java)
            startActivity(intent)
        }else{
            Toast.makeText(this,"Login first",Toast.LENGTH_SHORT).show()
        }
    }

    private fun registerUser(){
        val userName= etEmail.text.toString()
        val password= etPassword.text.toString()

        if(userName.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Fields can't be left empty",Toast.LENGTH_SHORT).show()
        }else{
            try{
                auth.createUserWithEmailAndPassword(userName,password)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(this,"User logged in with email ${auth.currentUser?.email}",Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                        Log.d("Signin New",it.message!!)
                    }
            }catch (e: Exception){
                Toast.makeText(this, "Something went wrong in catch",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logInExistingUser(){
        val username= etEmail.text.toString()
        val password= etPassword.text.toString()
        if (username.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Fields can't be empty",Toast.LENGTH_SHORT).show()
        }else{
            try{
                auth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Logged in as ${auth.currentUser?.email}",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this, LoggedInActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                        }
                    }
            }catch (e: Exception){
                Toast.makeText(this,"Something went wrong in catch",Toast.LENGTH_SHORT).show()
            }
        }
    }


}