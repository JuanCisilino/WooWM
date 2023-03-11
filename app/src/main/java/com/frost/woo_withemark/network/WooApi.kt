package com.frost.woo_withemark.network

import com.frost.woo_withemark.models.Images
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

    @POST("products")
    fun saveProduct(@Body product: WooProduct,
                    @Query("consumer_key") consumerKey: String,
                    @Query("consumer_secret") consumerSecret: String): Observable<WooProduct>

    @Multipart
    @POST("products/{id}/images")
    fun uploadImage(
        @Path("id") productId: Int,
        @Part image: MultipartBody.Part?,
        @Query("consumer_key") consumerKey: String,
        @Query("consumer_secret") consumerSecret: String
    ): Observable<Images>

    @DELETE("products/{id}")
    fun deleteProduct(@Path("id") id: Int,
                      @Query("consumer_key") consumerKey: String,
                      @Query("consumer_secret") consumerSecret: String): Completable
}