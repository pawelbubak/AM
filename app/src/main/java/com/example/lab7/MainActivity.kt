package com.example.lab7

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.net.URL
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    var record = 0
    private var counter = 0
    private var randomValue = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        randValue()
        showWelcome()
        getRecord()
        showRecordMessage()

        newGame.setOnClickListener {
            resetGame()
        }

        shoot.setOnClickListener {
            if (input.text.toString().isNotEmpty()) {
                val shootValue = input.text.toString().toInt()
                counter++
                if (counter > 10)
                    resetGame()
                when {
                    shootValue > randomValue -> showPrompt("Mniej", shootValue)
                    shootValue < randomValue -> showPrompt("Więcej", shootValue)
                    else -> {
                        saveRecord()
                        dialogShow()
                    }
                }
                input.setText("")
            }
        }

        rankButton.setOnClickListener {
            val thread = Thread {
                run {
                    Thread.sleep(2000)
                    val intent = Intent(this, RankingActivity::class.java)
                    startActivity(intent)
                    this.finish()
                }
            }

            thread.start()
        }
    }

    private fun resetGame() {
        counter = 0
        randValue()
        showWelcome()
        showRecordMessage()
    }

    private fun dialogShow() {
        val builder = AlertDialog.Builder(this@MainActivity)

        builder.setTitle("Wygrałeś")
        builder.setMessage("Udało Ci się za $counter podejściem! Udało Ci się zdobyć ${calculatePoints()} punktów!")

        builder.setPositiveButton("Super") { _, _ ->
            Toast.makeText(this@MainActivity, "Wylosowano nową liczbę", Toast.LENGTH_SHORT).show()
            resetGame()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun randValue() {
        randomValue = Random.nextInt(0, 1)
    }

    private fun getRecord() {
        val loginShared = this.getSharedPreferences("com.example.lab7.prefs", Context.MODE_PRIVATE)
        record = loginShared.getInt("recordValue", -1)
    }

    private fun saveRecord() {
        record += calculatePoints()
        saveRanking()
        val loginShared = this.getSharedPreferences("com.example.lab7.prefs", Context.MODE_PRIVATE)
        val editor = loginShared!!.edit()
        editor.putInt("recordValue", record)
        editor.apply()
    }

    private fun calculatePoints(): Int {
        return when {
            counter == 1 -> 5
            counter <= 4 -> 3
            counter <= 6 -> 2
            else -> 1
        }
    }

    private fun showRecordMessage() {
        if (record < 0) {
            lastResult.text = "Brak rekordu!"
        } else {
            lastResult.text = "Twój record wynosi: $record"
        }
    }

    private fun showPrompt(info: String, shootValue: Int) {
        prompt.text = "$info! Strzeliłeś $shootValue. Ilość prób: $counter"
    }

    private fun showWelcome() {
        prompt.text = getString(R.string.start_game_welcome)
    }

    private fun saveRanking() {
        class SaveRecordAsync : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg params: Void): String {
                val index = 132197
                val link = "http://hufiecgniezno.pl/br/record.php?f=add&id=$index&r=$record"
                val url = URL(link)
                if (url.readText() != "OK")
                    throw IllegalStateException("Nie udało się zapisać rekordu na serwerze")
                return url.readText()
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                prompt.text = result
            }
        }
        SaveRecordAsync().execute()
    }
}
