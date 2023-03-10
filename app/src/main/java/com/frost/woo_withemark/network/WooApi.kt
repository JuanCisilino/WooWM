package com.frost.woo_withemark.network

import com.frost.woo_withemark.models.WooProduct
import okhttp3.MultipartBody
import retrofit2.http.*
import rx.Completable
import rx.Observable

interface WooApi {

    @GET("products")
    fun getProducts(
        @Query("consumer_key") consumerKey: String,
        @Query("consumer_secret") consumerSecret: String): Observable<List<WooProduct>>

    @GET("products/{id}")
    fun getProductsByID(
        @Path("id") id : Int,
        @Query("consumer_key") consumerKey: String,
        @Query("consumer_secret") consumerSecret: String): Observable<WooProduct>

    @PUT("products/{id}")
    fun updateProduct(@Path("id") id: Int,
                      @Body product: WooProduct,
                      @Query("consumer_key") consumerKey: String,
                      @Query("consumer_secret") consumerSecret: String): Observable<WooProduct>

    @Multipart
    @POST("products")
    fun saveProduct(@Part imagen: MultipartBody.Part,
                    @Part product: WooProduct,
                    @Query("consumer_key") consumerKey: String,
                    @Query("consumer_secret") consumerSecret: String): Observable<WooProduct>

    @DELETE("products/{id}")
    fun deleteProduct(@Path("id") id: Int,
                      @Query("consumer_key") consumerKey: String,
                      @Query("consumer_secret") consumerSecret: String): Completable
}