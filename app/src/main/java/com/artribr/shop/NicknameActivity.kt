package com.artribr.shop

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_nickname.*

class NicknameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nickname)

        done.setOnClickListener{
            setNickname(nickname.text.toString())

            FirebaseDatabase.getInstance()
                .getReference("users")//根節點
                .child(FirebaseAuth.getInstance().currentUser!!.uid) //users的子節點，且id不可以為null
                .child("nickname")//uid的子節點
                .setValue(nickname.text.toString())//給於nickname值

            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
