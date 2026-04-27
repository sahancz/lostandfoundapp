package com.lostandfoundapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "LostAndFound.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "adverts"
        const val COLUMN_ID = "id"
        const val COLUMN_POST_TYPE = "post_type"
        const val COLUMN_NAME = "name"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_DATE = "date"
        const val COLUMN_LOCATION = "location"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_IMAGE_URI = "image_uri"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_POST_TYPE TEXT, "
                + "$COLUMN_NAME TEXT, "
                + "$COLUMN_PHONE TEXT, "
                + "$COLUMN_DESCRIPTION TEXT, "
                + "$COLUMN_DATE TEXT, "
                + "$COLUMN_LOCATION TEXT, "
                + "$COLUMN_CATEGORY TEXT, "
                + "$COLUMN_IMAGE_URI TEXT)")
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // --- CRUD OPERATIONS ---

    fun insertAdvert(advert: Advert): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_POST_TYPE, advert.postType)
            put(COLUMN_NAME, advert.name)
            put(COLUMN_PHONE, advert.phone)
            put(COLUMN_DESCRIPTION, advert.description)
            put(COLUMN_DATE, advert.date)
            put(COLUMN_LOCATION, advert.location)
            put(COLUMN_CATEGORY, advert.category)
            put(COLUMN_IMAGE_URI, advert.imageUri)
        }
        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        return id
    }


    fun getAllAdverts(categoryFilter: String? = null): List<Advert> {
        val advertList = mutableListOf<Advert>()
        val db = this.readableDatabase

        val selection = if (!categoryFilter.isNullOrEmpty() && categoryFilter != "All") "$COLUMN_CATEGORY = ?" else null
        val selectionArgs = if (!categoryFilter.isNullOrEmpty() && categoryFilter != "All") arrayOf(categoryFilter) else null


        val cursor: Cursor = db.query(
            TABLE_NAME, null, selection, selectionArgs, null, null, "$COLUMN_DATE DESC"
        )

        if (cursor.moveToFirst()) {
            do {
                val advert = Advert(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    postType = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POST_TYPE)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                    location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)),
                    category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                    imageUri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URI))
                )
                advertList.add(advert)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return advertList
    }

    fun deleteAdvert(id: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return result
    }
}