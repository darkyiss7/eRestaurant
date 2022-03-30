package com.isen.irestaurant.objects

import java.io.Serializable

data class CartItem (
    val plat : Item,
    var quantité : Int
        ): Serializable
