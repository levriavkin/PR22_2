package com.example.myapplication

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var monthEditText: EditText
    private lateinit var dayEditText: EditText
    private lateinit var factTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        monthEditText = findViewById(R.id.monthEditText)
        dayEditText = findViewById(R.id.dayEditText)
        factTextView = findViewById(R.id.factTextView)
    }

    fun fetchFactOnClick(view: View) {
        val month = monthEditText.text.toString()
        val day = dayEditText.text.toString()

        if (month.isNotEmpty() && day.isNotEmpty()) {
            val date = "$month/$day"
            FetchFactTask().execute(date)
        }
    }

    private inner class FetchFactTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String): String {
            val date = params[0]
            val apiUrl = "http://numbersapi.com/$date/date"

            val url = URL(apiUrl)
            val connection = url.openConnection() as HttpURLConnection

            try {
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val stringBuilder = StringBuilder()
                var line: String?

                while (reader.readLine().also { line = it } != null) {
                    stringBuilder.append(line)
                }

                return stringBuilder.toString()
            } finally {
                connection.disconnect()
            }
        }

        override fun onPostExecute(result: String) {
            factTextView.text = result
        }
    }
}