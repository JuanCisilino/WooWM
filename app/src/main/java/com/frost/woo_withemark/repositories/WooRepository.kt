package com.frost.woo_withemark.repositories

import com.frost.woo_withemark.network.WooApi

class WooRepository(private val api: WooApi) {

    fun getProducts(customerKey: String, secretKey: String) = api.getProducts(customerKey, secretKey)

    fun getProductById(id: Int, secretCustomer: String, secretKey: String) =
        api.getProductsByID(id, secretCustomer, secretKey)

}