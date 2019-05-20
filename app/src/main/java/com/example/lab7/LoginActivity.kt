package com.example.lab7

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import android.content.Intent
import android.graphics.PorterDuff
import android.os.AsyncTask
import android.util.Log
import android.view.View
import com.example.lab7.db.RecordRepository
import com.example.lab7.db.UserRepository
import com.example.lab7.model.User


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login.setOnClickListener { signIn() }

        sign_up_link.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivityForResult(intent, 0)
        }
    }

    private fun signIn() {
        Log.d(getString(R.string.title_activity_login), "Sign in")

        if (!validate()) {
            return
        }

        val user = validateUser()
        user?.let { login(it) }
    }

    private fun validate(): Boolean {
        var valid = true
        if (username.text.isEmpty() || username.text.length < 4) {
            Log.d(getString(R.string.title_activity_login), "Username not valid!")
            username.background.setColorFilter(resources.getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP)
            valid = false
        } else if (password.text.isEmpty() || password.text.length < 4) {
            Log.d(getString(R.string.title_activity_login), "Password not valid!")
            password.background.setColorFilter(resources.getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP)
            valid = false
        }
        Log.d(getString(R.string.title_activity_login), valid.toString())
        return valid
    }

    private fun validateUser(): User? {
        Log.d(getString(R.string.title_activity_login), "User authentication")
        val user = UserRepository(this@LoginActivity.baseContext).getUser(username.text.toString())
        Log.d(getString(R.string.title_activity_login), user.toString())
        if (user?.password.equals(password.text.toString())){
            Log.d(getString(R.string.title_activity_login), "The user was found")
            return user
        }
        return null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                val userId = data?.extras?.getLong("userId")
                val user = userId?.let { UserRepository(this@LoginActivity.baseContext).getUser(it) }
                user?.let { login(it) }
                this.finish()
            }
        }
    }

    private fun toggleControls() {
        username.isEnabled = !username.isEnabled
        password.isEnabled = !password.isEnabled
        sign_up_link.isEnabled = !sign_up_link.isEnabled
    }

    private fun login(user: User) {
        class LoginUser : AsyncTask<Void, Void, Void>() {
            override fun onPreExecute() {
                super.onPreExecute()
                toggleControls()
                progressBar.visibility = View.VISIBLE
                login.visibility = View.INVISIBLE
            }

            override fun doInBackground(vararg params: Void?): Void? {
                val recordRepository = RecordRepository(this@LoginActivity.baseContext)
                val record = recordRepository.getRecord(user.username!!)
                var points = 0
                if (record != null)
                    points = record.value!!

                Thread.sleep(2000)

                val loginShared = getSharedPreferences("com.example.lab7.prefs", Context.MODE_PRIVATE)
                val editor = loginShared!!.edit()
                editor.putInt("userId", user.id)
                editor.putString("displayName", user.name)
                editor.putInt("points", points)
                editor.apply()
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                toggleControls()
                progressBar.visibility = View.GONE
                login.visibility = View.VISIBLE
                finish()
            }
        }

        LoginUser().execute()
    }
}
