package com.frost.woo_withemark.ui.addedit

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frost.woo_withemark.R
import com.frost.woo_withemark.models.WooProduct
import com.frost.woo_withemark.repositories.AWSRepository
import com.frost.woo_withemark.repositories.WooRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import rx.schedulers.Schedulers
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(private val wooRepository: WooRepository): ViewModel() {

    var productLiveData = MutableLiveData<WooProduct?>()
    var saveProductLiveData = MutableLiveData<WooProduct?>()
    var updatedProductLiveData = MutableLiveData<Unit?>()
    var imageProductLiveData = MutableLiveData<String?>()
    lateinit var repo : AWSRepository

    var path : String?=null
    var file : File?=null

    fun getProduct(id: Int, secretCustomer: String, secretKey: String){
        wooRepository.getProductById(id, secretCustomer, secretKey)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {productLiveData.postValue(it)},
                {productLiveData.postValue(null)}
            )
    }

    fun saveProduct(product: WooProduct, secretCustomer: String, secretKey: String){
        wooRepository.saveProduct(product, secretCustomer, secretKey)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                { saveProductLiveData.postValue(it) },
                { saveProductLiveData.postValue(null) }
            )
    }

    fun saveImage(id: Int, image: MultipartBody.Part, secretCustomer: String, secretKey: String){
//        wooRepository.saveImage(id, image, secretCustomer, secretKey)
//            .subscribeOn(Schedulers.io())
//            .observeOn(Schedulers.io())
//            .subscribe(
//                { imageProductLiveData.postValue(Unit) },
//                { imageProductLiveData.postValue(null) }
//            )
    }

    fun saveImage(context: Context){
        repo = AWSRepository(context)
        repo.initUpload(
            path!!,
            context.getString(R.string.bucket))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                { imageProductLiveData.postValue(it) },
                { imageProductLiveData.postValue(null) }
            )
    }

    fun updateProduct(product: WooProduct, secretCustomer: String, secretKey: String){
        wooRepository.updateProduct(product, secretCustomer, secretKey)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                { updatedProductLiveData.postValue(Unit) },
                { updatedProductLiveData.postValue(null) }
            )
    }

}