package com.frost.woo_withemark.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frost.woo_withemark.models.WooProduct
import com.frost.woo_withemark.repositories.WooRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import rx.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val wooRepository: WooRepository): ViewModel(){

    var productListLiveData = MutableLiveData<List<WooProduct>?>()

    fun getProducts(secretCustomer: String, secretKey: String) {
        wooRepository.getProducts(secretCustomer, secretKey)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {productListLiveData.postValue(it)},
                {productListLiveData.postValue(null)}
            )
    }

    fun deleteProduct(id: Int, secretCustomer: String, secretKey: String){
        wooRepository.deleteProduct(id, secretCustomer, secretKey)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                { getProducts(secretCustomer, secretKey) },
                { productListLiveData.postValue(null) }
            )
    }
}