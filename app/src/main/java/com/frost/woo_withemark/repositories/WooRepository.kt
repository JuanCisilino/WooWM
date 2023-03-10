package com.frost.woo_withemark.repositories

import com.frost.woo_withemark.models.WooProduct
import com.frost.woo_withemark.network.WooApi
import okhttp3.MultipartBody

class WooRepository(private val api: WooApi) {

    fun getProducts(customerKey: String, secretKey: String) = api.getProducts(customerKey, secretKey)

    fun getProductById(id: Int, secretCustomer: String, secretKey: String) =
        api.getProductsByID(id, secretCustomer, secretKey)

    fun saveProduct(image: MultipartBody.Part, product: WooProduct, secretCustomer: String, secretKey: String) =
        api.saveProduct(image, product, secretCustomer, secretKey)
}