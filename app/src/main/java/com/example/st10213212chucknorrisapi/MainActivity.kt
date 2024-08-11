package com.example.st10213212chucknorrisapi

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val URL = "https://api.chucknorris.io/jokes/random"
    private lateinit var progressBar: ProgressBar
    private lateinit var jokeTextView: TextView
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the UI elements
        progressBar = findViewById(R.id.progressBar)
        jokeTextView = findViewById(R.id.ChuckTxt)

        // Set up edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Load the first joke on startup
        loadRandomFact()
    }

    // Button click function to load a new random fact
    fun nextBtn(view: View) {
        loadRandomFact()
    }

    private fun loadRandomFact() {
        // Show the progress bar while loading
        progressBar.visibility = View.VISIBLE

        // Create a request to the API
        val request = Request.Builder().url(URL).build()

        // Asynchronous network request
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    jokeTextView.text = "Failed to load joke"
                }
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                response.body?.let { responseBody ->
                    val json = responseBody.string()
                    val jsonObject = JSONObject(json)
                    val joke = jsonObject.getString("value")

                    // Update the UI with the joke
                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        jokeTextView.text = joke
                    }
                }
            }
        })
    }
}
