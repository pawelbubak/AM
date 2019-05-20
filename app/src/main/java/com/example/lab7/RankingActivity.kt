package com.example.lab7

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.lab7.db.RecordRepository
import com.example.lab7.model.Record

import kotlinx.android.synthetic.main.activity_ranking.*
import org.json.JSONArray
import java.net.URL

class RankingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        exitButton.setOnClickListener {
            val thread = Thread {
                run {
                    Thread.sleep(2000)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    this.finish()
                }
            }

            thread.start()
        }
        getRanking()
    }

    private fun getRanking() {
        class GetRecordsAsync : AsyncTask<Void, Void, List<Record>>() {
            override fun doInBackground(vararg params: Void?): List<Record> {
                var ranking = ArrayList<Record>()
                val dbHelper = RecordRepository(this@RankingActivity.applicationContext)
                try {
                    val link = "http://hufiecgniezno.pl/br/record.php?f=get"
                    val json = URL(link).readText()
                    val tab = JSONArray(json)

                    for (i in 0 until tab.length()) {
                        val item = tab.getJSONArray(i)
                        val record =
                            Record(item[0]?.toString()?.toInt(), item[1]?.toString(), item[2]?.toString()?.toInt())
                        ranking.add(record)
                    }
                } catch (e: Exception) {
                    ranking = ArrayList(dbHelper.allRecord)
                }
                if (ranking.isEmpty()) {
                    ranking = ArrayList(dbHelper.allRecord)
                }
                dbHelper.addRecordList(ranking)
                return ranking
            }

            override fun onPostExecute(result: List<Record>?) {
                super.onPostExecute(result)
                val sb = StringBuilder("")
                for (i in 0 until result!!.size) {
                    val item = result[i]
                    sb.append(i + 1).append(". ").append(item.username).append(" ").append(item.value).append("\n")
                }
                rankView.text = sb.toString()
            }
        }
        GetRecordsAsync().execute()
    }
}
