package com.frost.woo_withemark.models

data class WooProduct(
    var id: Int?=null,
    val name: String,
    val description: String,
    val price: String?=null,
    val regular_price: String,
    var images: List<Images> = listOf(),
    val stock_quantity: Int?=0
){}

data class Images(
    val src: String
)
