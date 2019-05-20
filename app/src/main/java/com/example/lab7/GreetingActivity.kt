package com.example.lab7

import android.os.Bundle
import android.support.v7.app.AppCompatActivity;

import android.content.Intent
import java.lang.Thread.sleep


class GreetingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greeting)

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
}
