package com.example.portsmap.module

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.portsmap.R

class Register : AppCompatActivity() {

    lateinit var textUserName: EditText
    lateinit var textPassword: EditText
    lateinit var CreateAnAcoountButton: Button
    lateinit var backButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.hide()
        }

        textUserName = findViewById(R.id.UserNameText) as EditText
        textPassword = findViewById(R.id.PasswordText) as EditText
        CreateAnAcoountButton = findViewById(R.id.CreateAnAcoountButton) as Button
        backButton = findViewById(R.id.backButton) as Button

        CreateAnAcoountButton.setOnClickListener{
            if (createAnAccount()){
                Toast.makeText(this, "New user created!", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "fail to create new account", Toast.LENGTH_SHORT).show()
            }
        }

        backButton.setOnClickListener{
            val intent = Intent(this@Register, Login::class.java)
            startActivity(intent)
        }

    }

    fun createAnAccount(): Boolean {
        if (textPassword.text.trim().length ==0 || textUserName.text.trim().length ==0){
            Toast.makeText(this, "Password or User name can't be empty!", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        // Load to data base
        val password = textPassword.getText().toString().trim()
        val useName = textUserName.getText().toString().trim()

        val db = DBHelper(this, null)
        db.addName(useName,password)
        return true
    }
}