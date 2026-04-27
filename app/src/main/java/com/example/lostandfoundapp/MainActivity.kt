package com.lostandfoundapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCreateAdvert = findViewById<Button>(R.id.btnCreateAdvert)
        val btnShowItems = findViewById<Button>(R.id.btnShowItems)


        btnCreateAdvert.setOnClickListener {
            val intent = Intent(this, CreateAdvertActivity::class.java)
            startActivity(intent)
        }



        btnShowItems.setOnClickListener {
            val intent = Intent(this, ListItemsActivity::class.java)
            startActivity(intent)
        }
    }
}