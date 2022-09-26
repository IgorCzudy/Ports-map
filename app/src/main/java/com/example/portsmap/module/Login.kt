package com.example.portsmap.module

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.portsmap.R

class Login : AppCompatActivity() {

    lateinit var textUserName: EditText
    lateinit var textPassword: EditText
    lateinit var LoginButton: Button
    lateinit var NewAccountButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.hide()
        }


        textUserName = findViewById(R.id.textUserName) as EditText
        textPassword = findViewById(R.id.textPassword) as EditText
        LoginButton = findViewById(R.id.LoginButton) as Button
        NewAccountButton = findViewById(R.id.NewAccountButton) as Button

        LoginButton.setOnClickListener{
            if (checkData()){
                val intent = Intent(this@Login, MainActivity::class.java)
                startActivity(intent)
            }
        }

        NewAccountButton.setOnClickListener{
            val intent = Intent(this@Login, Register::class.java)
            startActivity(intent)

        }
    }

    fun checkData(): Boolean {

        Log.d("LOG",textPassword.text.toString())

        val password = textPassword.getText().toString().trim()
        val useName = textUserName.getText().toString().trim()

        if (password.length ==0 || useName.length ==0){
            Toast.makeText(this, "Password or User name can't be empty!", Toast.LENGTH_SHORT)
                .show()
            return false
        }

        // check if that user name is in database and check if it's have this passwors
        val db = DBHelper(this, null)

        if (db.hasUserNameCorrectPassword(useName, password)){
            return true
        }
        Toast.makeText(this, "Uncorrect data ${password}, ${useName} ", Toast.LENGTH_SHORT)
            .show()

        return false
    }
}