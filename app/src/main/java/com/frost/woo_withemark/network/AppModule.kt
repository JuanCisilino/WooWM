package com.frost.woo_withemark.network

import com.frost.woo_withemark.repositories.FirebaseRepository
import com.frost.woo_withemark.repositories.WooRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUserDatabase() = Firebase.firestore.collection("users")

    @Provides
    @Singleton
    fun provideWooInstance() = Retrofit.Builder()
            .baseUrl("https://whitemark.ml/woorender/wp-json/wc/v3/")
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()
            .create(WooApi::class.java)

    @Provides
    @Singleton
    fun provideWooRepository(api: WooApi) = WooRepository(api)

    @Provides
    @Singleton
    fun provideDatabaseRepository(db: CollectionReference) = FirebaseRepository(db)

}