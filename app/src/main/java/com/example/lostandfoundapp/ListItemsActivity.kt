package com.lostandfoundapp

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListItemsActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinnerFilter: Spinner
    private lateinit var adapter: AdvertAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_items)

        dbHelper = DatabaseHelper(this)

        recyclerView = findViewById(R.id.recyclerViewItems)
        spinnerFilter = findViewById(R.id.spinnerFilter)

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter with all items initially
        adapter = AdvertAdapter(dbHelper.getAllAdverts())
        recyclerView.adapter = adapter

        setupFilterSpinner()
    }

    // Refresh the list every time we come back to this screen (e.g., after deleting an item)
    override fun onResume() {
        super.onResume()
        val currentCategory = spinnerFilter.selectedItem.toString()
        val filter = if (currentCategory == "All") null else currentCategory
        adapter.updateData(dbHelper.getAllAdverts(filter))
    }

    private fun setupFilterSpinner() {
        // "All" is added so the user can see everything before filtering
        val categories = arrayOf("All", "Electronics", "Pets", "Wallets", "Clothing", "Keys", "Other")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        spinnerFilter.adapter = spinnerAdapter

        spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position]

                // If "All" is selected, pass null to fetch everything. Otherwise, pass the category.
                val filter = if (selectedCategory == "All") null else selectedCategory

                val filteredList = dbHelper.getAllAdverts(filter)
                adapter.updateData(filteredList)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }
}