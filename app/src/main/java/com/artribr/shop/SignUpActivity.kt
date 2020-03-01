package com.artribr.shop

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signup.setOnClickListener{
            val sEmail: String = email.text.toString()
            val sPassword: String = password.text.toString()

            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        AlertDialog.Builder(this)
                            .setTitle("Sign Up")
                            .setMessage("Account created")
                            .setPositiveButton("OK"){dialog, which ->
                                setResult(Activity.RESULT_OK)
                                finish();
                            }.show()
                    }else{


                        AlertDialog.Builder(this)
                            .setTitle("Sign Up")
                            .setMessage(it.exception?.message)
                            .setPositiveButton("OK"){dialog, which ->
                                setResult(Activity.RESULT_OK)
                            }.show()
                    }
                }
        }
    }
}
