package com.frost.woo_withemark.repositories

import com.frost.woo_withemark.models.WooProduct
import com.frost.woo_withemark.network.WooApi
import okhttp3.MultipartBody

class WooRepository(private val api: WooApi) {

    fun getProducts(customerKey: String, secretKey: String) = api.getProducts(customerKey, secretKey)

    fun getProductById(id: Int, secretCustomer: String, secretKey: String) =
        api.getProductsByID(id, secretCustomer, secretKey)

    fun saveProduct(product: WooProduct, secretCustomer: String, secretKey: String) =
        api.saveProduct(product, secretCustomer, secretKey)

    fun saveImage(id: Int, image: MultipartBody.Part, secretCustomer: String, secretKey: String) =
        api.uploadImage(id, image, secretCustomer, secretKey)

    fun deleteProduct(id: Int, secretCustomer: String, secretKey: String) =
        api.deleteProduct(id, secretCustomer, secretKey)

    fun updateProduct(product: WooProduct, secretCustomer: String, secretKey: String) =
        api.updateProduct(product.id!!, product, secretCustomer, secretKey)
}