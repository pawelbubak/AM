package com.example.lab7

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;

import android.content.Intent
import java.lang.Thread.sleep


class GreetingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greeting)

        if (!checkUserIsLoggedIn()) {
            displayLoginActivity()
        } else {
            startApp()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (!checkUserIsLoggedIn()) {
                displayLoginActivity()
            } else {
                startApp()
            }
        }
    }

    private fun displayLoginActivity() {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivityForResult(intent, 1)
    }

    private fun startApp(){
        val thread = Thread {
            run {
                sleep(2000)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                this.finish()
            }
        }

        thread.start()
    }

    private fun checkUserIsLoggedIn(): Boolean {
        val loginShared = this.getSharedPreferences("com.example.lab7.prefs", Context.MODE_PRIVATE)
        val userId = loginShared.getInt("userId", -1)
        if (userId < 0) {
            return false
        }
        return true
    }
}
