package edu.uw.ischool.xyou.quizdroid

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class PreferencesActivity : AppCompatActivity() {

    private val TAG = "PreferencesActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        val sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        val urlInput = findViewById<EditText>(R.id.url_input)
        val frequencyInput = findViewById<EditText>(R.id.frequency_input)

        // displays saved preferences if there is any
        urlInput.setText(sharedPref.getString("url", null))
        frequencyInput.setText(sharedPref.getInt("frequency", 0).toString())

        val saveButton = findViewById<Button>(R.id.save_preferences)

        saveButton.setOnClickListener {
            val url = urlInput.text.toString()
            val frequency = frequencyInput.text.toString().toInt()

            // check the input
            if (url == "" || frequency.toString() == "") {

                if (frequency == 0) {
                    Toast.makeText(this, "Frequency should be greater than 0", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.i(TAG, "Saving preferences")

                editor.apply {
                    putString("url", url)
                    putInt("frequency", frequency)
                    apply()
                }

                // restart the service
                var intent = Intent(this, DownloadService::class.java)
                stopService(intent)

                intent = Intent(this, DownloadService::class.java).apply {
                    putExtra("url", url)
                    putExtra("frequency", frequency)
                }
                startService(intent)
            }
        }
    }
}