package com.isen.irestaurant.objects

import com.isen.irestaurant.objects.Item
import java.io.Serializable

data class ItemModel(
    var name_fr: String,
    var items: ArrayList<Item>,

    ): Serializable
