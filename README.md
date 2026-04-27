# Lost & Found App (SIT708 - Task 7.1)

An Android mobile application designed to connect lost items with their owners. Users can create detailed adverts for items they have lost or found, browse a community feed of items, and remove postings once an item has been successfully returned. 

This project was built to demonstrate proficiency in Android UI development, Intents, and local persistent data storage using SQLite.

## 🚀 Core Features & Subtasks

* **Create & Manage Adverts:** Users can post items as either "Lost" or "Found" with detailed descriptions, contact information, and locations.
* **Persistent SQLite Database:** All items are stored locally on the device using a custom `DatabaseHelper` class for full CRUD (Create, Read, Delete) functionality.
* **Category Filtering (Subtask):** The main feed includes a dynamic dropdown filter, allowing users to instantly sort the database by categories like Electronics, Pets, Wallets, etc.
* **Image Uploads (Subtask):** Integrated with the Android `ActivityResultContracts.OpenDocument` API, allowing users to securely select and permanently link high-resolution images from their device gallery to their postings.
* **Automated Timestamps (Subtask):** The app automatically generates and formats a date/time stamp at the exact moment a user opens the creation form, ensuring accurate tracking of how recent a listing is.
* **Smart UI/UX:** Features color-coded list items (Red for Lost, Green for Found) and safe image-rendering logic to prevent crashes on revoked URI permissions.

## 🛠️ Tech Stack

* **Language:** Kotlin
* **UI Layouts:** XML (LinearLayout, ConstraintLayout, CardView, RecyclerView)
* **Database:** SQLite (`SQLiteOpenHelper`)
* **Architecture:** Standard Android MVC / Activity-based

## 📱 Project Structure

* `MainActivity.kt`: The primary navigation hub.
* `CreateAdvertActivity.kt`: Handles user input, image selection intents, and timestamp generation.
* `ListItemsActivity.kt`: Manages the RecyclerView and the category filtering logic.
* `ItemDetailActivity.kt`: Displays full item details, renders the persistent image URI, and handles the database deletion logic.
* `DatabaseHelper.kt`: The SQLite implementation managing the `adverts` table.

## ⚙️ How to Run

1. Clone this repository to your local machine.
2. Open the project in **Android Studio**.
3. Allow Gradle to sync the required dependencies (RecyclerView, CardView, Material Components).
4. Build and run the project on an Android Emulator (API 31+ recommended) or a physical Android device.
