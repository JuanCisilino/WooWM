package com.frost.woo_withemark.network

import rx.Observable
import java.io.File
import java.util.jar.Attributes.Name

interface AWSApi {

    fun uploadImage(bucketName: String, fileName: Name, file: File): Observable<String>{

        return Observable.just("t")
    }

}