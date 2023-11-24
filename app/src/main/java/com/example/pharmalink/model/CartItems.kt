package com.example.pharmalink.model

data class CartItems(
    var drugName: String? = null,
    var drugPrice: String ?= null,
    var drugDescription: String ?= null,
    var drugImage: String ?= null,
    var drugQuantity: Int ?= null
)
