package com.lostandfoundapp

data class Advert(
    var id: Int = 0,
    var postType: String,
    var name: String,
    var phone: String,
    var description: String,
    var date: String,
    var location: String,
    var category: String,
    var imageUri: String?,
    var latitude: Double? = null,
    var longitude: Double? = null
)