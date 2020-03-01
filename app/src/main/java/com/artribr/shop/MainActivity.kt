package com.artribr.shop

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.row_function.view.*

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName
    var isSignUp = false;
    val RC_SIGNUP = 200;
    val RC_NICKNAME = 210;

    val auth = FirebaseAuth.getInstance();

    val functions = listOf<String>(
        "Camera",
        "Invite friend",
        "Parking",
        "Download coupons",
        "News",
        "Movies",
        "Maps",
        "Bus"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
/*        if (!isSignUp) {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(intent, RC_SIGNUP)
        }*/

        auth.addAuthStateListener { auth ->
            authChanged(auth)
        }


        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        //Spinner
        val colors = arrayOf("Red", "Green", "Blue")
        //創建adapter並指定每一列的layout及給於data
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, colors)
        //修改下拉選單的樣式
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        //將adapter給spinner
        spinner.adapter = adapter
        //設定下拉選單被點中的事件
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                Log.d(TAG, "onItemSelected: ${colors[position]}")
            }


        }

        //RecyclerView
        //選擇recycler view的排版方式
        recycler.layoutManager = LinearLayoutManager(this)
        //是否為固定大小
        recycler.setHasFixedSize(true)
        //設定Adapter
        recycler.adapter = FunctionAdapter()

    }

    //必需繼承RecyclerView.Adapter，並且指定holder的型別
    inner class FunctionAdapter() : RecyclerView.Adapter<FunctionHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunctionHolder {
            //單列資料的版型
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_function, parent, false);

            return FunctionHolder(view)
        }
        override fun getItemCount(): Int {
            return functions.size

        }

        override fun onBindViewHolder(holder: FunctionHolder, position: Int) {
            holder.nameText.text = functions.get(position)
            holder.itemView.setOnClickListener { view ->
                functionClicked(holder, position)
            }
        }

    }

    private fun functionClicked(holder: MainActivity.FunctionHolder, position: Int) {
        Log.d("MainActivity", "functionClicked: $position")
        when (position) {
            1 -> startActivity(Intent(this, ContactActivity::class.java))

            2 -> startActivity(Intent(this, ParkingActivity::class.java))

            5 -> startActivity(Intent(this, MovieActivity::class.java))

            7 -> startActivity(Intent(this, BusActivity::class.java))

        }
    }

    //暫存器
    class FunctionHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nameText: TextView = view.name
    }

    private fun authChanged(auth: FirebaseAuth) {
        if (auth.currentUser == null) {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(intent, RC_SIGNUP)
        } else {
            Log.d("MainActivity", "authChanged: ${auth.currentUser?.uid}")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGNUP) {
            if (resultCode == Activity.RESULT_OK) {
                val intent = Intent(this, NicknameActivity::class.java)
                startActivityForResult(intent, RC_NICKNAME)
            }
        } else if (resultCode == RC_NICKNAME) {

        }
    }

    override fun onResume() {
        super.onResume()
        FirebaseDatabase.getInstance()
            .getReference("users")
            .child(auth.currentUser!!.uid)
            .child("nickname")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    nickname.setText(dataSnapshot.value as String)

                }

            })

    }
}
