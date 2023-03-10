package com.frost.woo_withemark.ui.addedit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frost.woo_withemark.models.WooProduct
import com.frost.woo_withemark.repositories.WooRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import rx.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(private val wooRepository: WooRepository): ViewModel() {

    var productLiveData = MutableLiveData<WooProduct?>()
    var saveProductLiveData = MutableLiveData<WooProduct?>()

    var path : String?=null

    fun getProduct(id: Int, secretCustomer: String, secretKey: String){
        wooRepository.getProductById(id, secretCustomer, secretKey)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {productLiveData.postValue(it)},
                {productLiveData.postValue(null)}
            )
    }

    fun saveProduct(image: MultipartBody.Part, product: WooProduct, secretCustomer: String, secretKey: String){
        wooRepository.saveProduct(image, product, secretCustomer, secretKey)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {saveProductLiveData.postValue(it)},
                {saveProductLiveData.postValue(null)}
            )
    }

}