# 📍 Lost & Found Map Mobile App V2

A modern, location-aware Android application that allows users to crowdsource and track lost and found items in their community. Built as part of the SIT708 Advanced Mobile App Development unit.

## 🚀 Overview

This application upgrades a standard SQLite database ledger into a fully interactive geospatial platform. Users can log items they have lost or found and attach precise geographical coordinates using device GPS hardware or the Google Places API. These items are then dynamically plotted on a Google Map, complete with proximity filtering.

## ✨ Key Features

* **Geospatial Data Entry:**
  * **Google Places Autocomplete:** Users can search for specific global addresses which are automatically converted into exact Latitude/Longitude coordinates.
  * **GPS Integration:** A single-tap `FusedLocationProviderClient` integration locks onto the device's hardware to pull the current physical location.
* **Interactive Mapping:** * Integrates the Google Maps SDK to plot all database items globally.
  * **Visual Hierarchy:** Implements automated color-coding (Red pins for 'Lost' items, Green pins for 'Found' items).
* **Radius Proximity Filter (Subtask):** * Includes a dynamic distance-calculation engine. Users can filter the map to only show items within a specific radius of their current physical location (e.g., 2km, 5km, 10km, 50km).
* **Persistent Local Storage:** Utilizes a custom SQLite implementation upgraded to handle complex coordinate data types (`REAL`) alongside image URIs and text metadata.

## 🛠️ Technology Stack

* **Language:** Kotlin
* **UI Toolkit:** Android XML (ConstraintLayout, LinearLayout)
* **Database:** SQLite (`SQLiteOpenHelper`)
* **APIs & SDKs:** * Google Maps SDK for Android
  * Google Places API
  * Google Play Services Location (`FusedLocationProviderClient`)

## ⚙️ Setup and Installation

To clone and run this project locally, you must provide your own Google Cloud API Key.

1. Clone the repository:
   ```bash
   git clone [https://github.com/YourUsername/LostAndFoundApp.git](https://github.com/YourUsername/LostAndFoundApp.git)
