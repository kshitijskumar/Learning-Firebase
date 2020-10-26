package com.example.learningfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth= FirebaseAuth.getInstance()

        btnSignIn.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser(){
        val userName= etEmail.text.toString()
        val password= etPassword.text.toString()

        if(userName.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Fields can't be left empty",Toast.LENGTH_SHORT).show()
        }else{
            Log.d("Main Activity","In coroutine scope ${Thread.currentThread().name}")
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    Log.d("Main Activity","In  try block coroutine scope ${Thread.currentThread().name}")
                    auth.createUserWithEmailAndPassword(userName,password)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity,"User registered",Toast.LENGTH_SHORT).show()
                        currentUserStatus()
                    }
                }catch (e: Exception){
                    Log.d("Main Activity",e.message!!)
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@MainActivity,"Something went wrong. Try again",
                        Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }

    private fun currentUserStatus(){
        if(auth.currentUser==null){
            Toast.makeText(this,"Not logged in",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"User logged in with ${auth.currentUser}",Toast.LENGTH_SHORT).show()
        }
    }
}