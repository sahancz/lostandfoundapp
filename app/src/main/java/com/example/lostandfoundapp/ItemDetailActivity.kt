package com.lostandfoundapp

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ItemDetailActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var advertId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        dbHelper = DatabaseHelper(this)

        advertId = intent.getIntExtra("ADVERT_ID", -1)

        if (advertId != -1) {
            loadAdvertDetails()
        } else {
            Toast.makeText(this, "Error loading item", Toast.LENGTH_SHORT).show()
            finish()
        }

        val btnRemove = findViewById<Button>(R.id.btnRemove)
        btnRemove.setOnClickListener {
            val result = dbHelper.deleteAdvert(advertId)
            if (result > 0) {
                Toast.makeText(this, "Item Removed", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error removing item", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadAdvertDetails() {

        val advert = dbHelper.getAllAdverts().find { it.id == advertId }

        advert?.let {
            val tvDetailType = findViewById<TextView>(R.id.tvDetailType)
            val ivDetailImage = findViewById<ImageView>(R.id.ivDetailImage)
            val tvDetailName = findViewById<TextView>(R.id.tvDetailName)
            val tvDetailDate = findViewById<TextView>(R.id.tvDetailDate)
            val tvDetailLocation = findViewById<TextView>(R.id.tvDetailLocation)
            val tvDetailPhone = findViewById<TextView>(R.id.tvDetailPhone)
            val tvDetailCategory = findViewById<TextView>(R.id.tvDetailCategory)
            val tvDetailDescription = findViewById<TextView>(R.id.tvDetailDescription)

            tvDetailType.text = it.postType
            if (it.postType == "Lost") {
                tvDetailType.setTextColor(Color.parseColor("#D32F2F"))
            } else {
                tvDetailType.setTextColor(Color.parseColor("#388E3C"))
            }

            tvDetailName.text = it.name
            tvDetailDate.text = "Date: ${it.date}"
            tvDetailLocation.text = "Location: ${it.location}"
            tvDetailPhone.text = "Contact: ${it.phone}"
            tvDetailCategory.text = "Category: ${it.category}"
            tvDetailDescription.text = it.description



            if (!it.imageUri.isNullOrEmpty() && it.imageUri != "null") {
                try {
                    val uri = Uri.parse(it.imageUri)

                    val inputStream = contentResolver.openInputStream(uri)
                    val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
                    ivDetailImage.setImageBitmap(bitmap)
                    inputStream?.close()
                } catch (e: SecurityException) {

                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}