package com.example.learningfirebase

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_logged_in.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoggedInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: StorageReference
    private var uri: Uri?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged_in)

        auth= FirebaseAuth.getInstance()
        firestore= FirebaseFirestore.getInstance()
        storage= FirebaseStorage.getInstance().reference

        Log.d("Thread Info",Thread.currentThread().name)
        Toast.makeText(this,"Current user: ${auth.currentUser}",Toast.LENGTH_SHORT).show()

        btnSignOut.setOnClickListener {
            auth.signOut()
            finish()
        }

        btnAdd.setOnClickListener {
            val name= etName.text.toString()
            val surname= etSurname.text.toString()
            val map= HashMap<String, String?>()
            if(name.isNotEmpty()){
                map["name"]= name
            }
            if(surname.isNotEmpty()){
                map["surname"]= surname
            }
            addUserData(map)
        }

        btnAddImage.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).apply {
                this.type= "image/*"
                startActivityForResult(this, 0)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==0 && resultCode== RESULT_OK){
            uri= data?.data
            ivImage.setImageURI(uri)
        }
    }

    private fun addUserData(map: Map<String, String?>)=CoroutineScope(Dispatchers.IO).launch {

            try {
                firestore.collection("person").document(auth.currentUser?.uid!!)
                    .set(map, SetOptions.merge()).await()
                uploadImageAndGetUrl()
                
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    Log.d("Catch BLock",e.message!!)
                    Toast.makeText(this@LoggedInActivity,e.message,Toast.LENGTH_SHORT).show()
                }
            }
    }
//    Java way

//    private fun uploadImageAndGetUrl()= CoroutineScope(Dispatchers.IO).launch {
//        try {
//            storage.child("image.jpeg").putFile(uri!!)
//                    .continueWithTask {
//                        storage.child("image.jpeg").downloadUrl
//                    }
//                    .addOnSuccessListener {
//                        val map= HashMap<String, String?>()
//                        map["imageUrl"]= it.toString()
//                        CoroutineScope(Dispatchers.IO).launch {
//                            try {
//                                firestore.collection("person").document(auth.currentUser?.uid!!)
//                                        .set(map, SetOptions.merge())
//                            }catch (e: Exception){
//                                Log.d("InCatchUpdatingUser", e.message.toString())
//                            }
//                        }
//                    }
//
//
//        }catch (e: Exception){
//            withContext(Dispatchers.Main){
//                Toast.makeText(this@LoggedInActivity, "Something went wrong in catch", Toast.LENGTH_SHORT).show()
//                Log.d("InCatch",e.message.toString())
//            }
//        }
//    }

    // Kotlin way

    private fun uploadImageAndGetUrl()= CoroutineScope(Dispatchers.IO).launch {
        try {
            storage.child("image_ktx.jpeg").putFile(uri!!).await()
            val url= storage.child("image_ktx.jpeg").downloadUrl.await()
            val map= HashMap<String, String?>()
            map["imageUrl"]= url.toString()
            firestore.collection("person").document(auth.currentUser?.uid!!)
                    .set(map, SetOptions.merge()).await()
            withContext(Dispatchers.Main){
                Toast.makeText(this@LoggedInActivity, "Url saved as $url", Toast.LENGTH_SHORT).show()
            }
        }catch (e: Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(this@LoggedInActivity, "Something went wrong in catch", Toast.LENGTH_SHORT).show()
                Log.d("UploadImage", e.message.toString())
            }
        }
    }

}