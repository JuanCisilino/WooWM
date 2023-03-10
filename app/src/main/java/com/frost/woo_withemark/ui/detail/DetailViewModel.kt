package com.frost.woo_withemark.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frost.woo_withemark.models.WooProduct
import com.frost.woo_withemark.repositories.WooRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import rx.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val wooRepository: WooRepository) :ViewModel() {

    var productLiveData = MutableLiveData<WooProduct?>()

    fun getProduct(id: Int, secretCustomer: String, secretKey: String){
        if (id != 0){
            wooRepository.getProductById(id, secretCustomer, secretKey)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                    {productLiveData.postValue(it)},
                    {productLiveData.postValue(null)}
                )
        } else {
            productLiveData.postValue(null)
        }

    }

}