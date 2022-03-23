package com.example.irestaurant

import java.io.Serializable

data class ItemModel(
    var name_fr: String,
    var items: ArrayList<Item>,

): Serializable
