package com.example.lab7

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.lab7.db.UserRepository
import com.example.lab7.model.User
import kotlinx.android.synthetic.main.activity_register.*
import java.lang.Thread.sleep


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register.setOnClickListener { signUp() }
        sign_in_link.setOnClickListener { finish() }
    }

    private fun signUp() {
        Log.d(getString(R.string.title_activity_register), "Sign up")

        if (!validate()) {
            return
        }

        register()
    }

    private fun validate(): Boolean {
        var valid = true
        if (name.text.isEmpty() || name.text.length < 3) {
            Log.d(getString(R.string.title_activity_register), "Name not valid!")
            name.background.setColorFilter(resources.getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP)
            valid = false
        } else if (username.text.isEmpty() || username.text.length < 4) {
            Log.d(getString(R.string.title_activity_register), "Username not valid!")
            username.background.setColorFilter(resources.getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP)
            valid = false
        } else if (password.text.isEmpty() || password.text.length < 4) {
            Log.d(getString(R.string.title_activity_register), "Password not valid!")
            password.background.setColorFilter(resources.getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP)
            valid = false
        }
        Log.d(getString(R.string.title_activity_register), valid.toString())
        return valid
    }

    private fun toggleControls() {
        name.isEnabled = !name.isEnabled
        username.isEnabled = !username.isEnabled
        password.isEnabled = !password.isEnabled
        sign_in_link.isEnabled = !sign_in_link.isEnabled
    }

    private fun register() {
        class RegisterUser : AsyncTask<Void, Void, Long>() {
            override fun onPreExecute() {
                super.onPreExecute()
                toggleControls()
                progressBar.visibility = View.VISIBLE
                register.visibility = View.INVISIBLE
            }

            override fun doInBackground(vararg params: Void?): Long {
                val userRepository = UserRepository(this@RegisterActivity.baseContext)
                val user = User()
                user.name = name.text.toString()
                user.username = username.text.toString()
                user.password = password.text.toString()
                sleep(2000)
                return userRepository.addUser(user)
            }

            override fun onPostExecute(result: Long?) {
                super.onPostExecute(result)
                toggleControls()
                progressBar.visibility = View.GONE
                register.visibility = View.VISIBLE
                if (result == null || result == -1L) {
                    return
                }

                val intentResult = Intent()
                intentResult.putExtra("userId", result)
                setResult(Activity.RESULT_OK, intentResult)
                finish()
            }
        }

        RegisterUser().execute()
    }
}
