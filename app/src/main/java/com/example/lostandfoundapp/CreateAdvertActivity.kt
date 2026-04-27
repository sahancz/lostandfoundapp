package com.lostandfoundapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class CreateAdvertActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var selectedImageUri: Uri? = null


    private lateinit var radioGroupPostType: RadioGroup
    private lateinit var radioLost: RadioButton
    private lateinit var spinnerCategory: Spinner
    private lateinit var etName: EditText
    private lateinit var etPhone: EditText
    private lateinit var etDescription: EditText
    private lateinit var etDate: EditText
    private lateinit var etLocation: EditText
    private lateinit var btnAddImage: Button
    private lateinit var ivPreview: ImageView
    private lateinit var btnSave: Button


    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let {

            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)

            selectedImageUri = it
            ivPreview.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_advert)

        dbHelper = DatabaseHelper(this)


        radioGroupPostType = findViewById(R.id.radioGroupPostType)
        radioLost = findViewById(R.id.radioLost)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        etName = findViewById(R.id.etName)
        etPhone = findViewById(R.id.etPhone)
        etDescription = findViewById(R.id.etDescription)
        etDate = findViewById(R.id.etDate)
        etLocation = findViewById(R.id.etLocation)
        btnAddImage = findViewById(R.id.btnAddImage)
        ivPreview = findViewById(R.id.ivPreview)
        btnSave = findViewById(R.id.btnSave)

        setupCategorySpinner()
        autoFillDate()


        btnAddImage.setOnClickListener {

            pickImageLauncher.launch(arrayOf("image/*"))
        }


        btnSave.setOnClickListener {
            saveAdvert()
        }
    }

    private fun setupCategorySpinner() {

        val categories = arrayOf("Electronics", "Pets", "Wallets", "Clothing", "Keys", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        spinnerCategory.adapter = adapter
    }

    private fun autoFillDate() {

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val currentDateAndTime = sdf.format(Date())
        etDate.setText(currentDateAndTime)
    }

    private fun saveAdvert() {
        val name = etName.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val description = etDescription.text.toString().trim()
        val date = etDate.text.toString().trim()
        val location = etLocation.text.toString().trim()

        if (name.isEmpty() || phone.isEmpty() || description.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill in all text fields", Toast.LENGTH_SHORT).show()
            return
        }


        if (selectedImageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }


        val postType = if (radioLost.isChecked) "Lost" else "Found"
        val category = spinnerCategory.selectedItem.toString()

        val advert = Advert(
            postType = postType,
            name = name,
            phone = phone,
            description = description,
            date = date,
            location = location,
            category = category,
            imageUri = selectedImageUri.toString()
        )

        val id = dbHelper.insertAdvert(advert)

        if (id > -1) {
            Toast.makeText(this, "Advert Saved Successfully!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error saving advert", Toast.LENGTH_SHORT).show()
        }
    }
}