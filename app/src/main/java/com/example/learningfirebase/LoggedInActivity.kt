package com.example.learningfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_logged_in.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoggedInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged_in)

        auth= FirebaseAuth.getInstance()
        firestore= FirebaseFirestore.getInstance()
        Toast.makeText(this,"Current user: ${auth.currentUser}",Toast.LENGTH_SHORT).show()

        btnSignOut.setOnClickListener {
            auth.signOut()
            finish()
        }

        btnAdd.setOnClickListener {
            addUserData()
        }
    }

    private fun addUserData(){
        val name= etName.text.toString()
        val surname= etSurname.text.toString()
        val person= Person(name,surname)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                firestore.collection("person").document(auth.currentUser?.uid!!)
                    .set(person)
                withContext(Dispatchers.Main){
                    Toast.makeText(this@LoggedInActivity,"Data added",Toast.LENGTH_SHORT).show()
                }

            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    Log.d("Catch BLock",e.message!!)
                    Toast.makeText(this@LoggedInActivity,e.message,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}