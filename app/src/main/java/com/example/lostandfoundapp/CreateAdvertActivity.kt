package com.lostandfoundapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity // Added missing import
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.text.SimpleDateFormat
import java.util.*

class CreateAdvertActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var selectedImageUri: Uri? = null

    // Variables to hold the math coordinates for Google Maps
    private var selectedLatitude: Double? = null
    private var selectedLongitude: Double? = null

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
    private lateinit var btnGetCurrentLocation: Button

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let {
            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            selectedImageUri = it
            ivPreview.setImageURI(it)
        }
    }

    // Google Places auto complete
    private val autocompleteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val place = Autocomplete.getPlaceFromIntent(result.data!!)
            etLocation.setText(place.name)

            selectedLatitude = place.latLng?.latitude
            selectedLongitude = place.latLng?.longitude
        } else if (result.resultCode == AutocompleteActivity.RESULT_ERROR) {
            Toast.makeText(this, "Error fetching place", Toast.LENGTH_SHORT).show()
        }
    }


    private val locationPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            fetchCurrentLocation()
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_advert)

        dbHelper = DatabaseHelper(this)

        // Initialize Google Places using my API key
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)
        }

        //  GPS Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

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
        btnGetCurrentLocation = findViewById(R.id.btnGetCurrentLocation)

        setupCategorySpinner()
        autoFillDate()

        btnAddImage.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        // Google search
        etLocation.isFocusable = false
        etLocation.isClickable = true
        etLocation.setOnClickListener {
            val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(this)
            autocompleteLauncher.launch(intent)
        }

        // Handle GPS Button click
        btnGetCurrentLocation.setOnClickListener {
            checkLocationPermissions()
        }

        btnSave.setOnClickListener {
            saveAdvert()
        }
    }

    private fun checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fetchCurrentLocation()
        } else {
            locationPermissionRequest.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                selectedLatitude = location.latitude
                selectedLongitude = location.longitude
                etLocation.setText("Current GPS Location")
                Toast.makeText(this, "Location acquired!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Could not fetch GPS. Ensure location is turned on.", Toast.LENGTH_SHORT).show()
            }
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


        if (selectedLatitude == null || selectedLongitude == null) {
            Toast.makeText(this, "Please select a valid location from the dropdown or use GPS", Toast.LENGTH_SHORT).show()
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
            imageUri = selectedImageUri.toString(),
            latitude = selectedLatitude,
            longitude = selectedLongitude
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