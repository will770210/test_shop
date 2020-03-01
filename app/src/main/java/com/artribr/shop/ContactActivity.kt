package com.artribr.shop

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ContactActivity : AppCompatActivity() {
    private val TAG = ContactActivity::class.java.simpleName
    private val RC_CONTACTS = 110
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        //取得是否有讀聯絡人資訊的權限
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)

        //確認是否已授權
        if(permission != PackageManager.PERMISSION_GRANTED) {
            //跳出詢問授權視窗
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), RC_CONTACTS)
        }else{
            readContacts()
        }

    }

    //詢問授權後，不管是否有授權，都會進入此function
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if(requestCode == RC_CONTACTS){
            //grantResults就依詢問權限順序排列
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                readContacts()
            }
        }
    }

    private fun readContacts(){
        //如同資料庫查詢語法，cursor用來存放查詢結果的資料
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )
        //透過loop，將逐筆資料抓出
        while(cursor.moveToNext()){
            //往該筆資料，的display name欄位取得值
            val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            Log.d(TAG,  name)
        }
    }
}
