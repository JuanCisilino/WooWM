package com.frost.woo_withemark.repositories

import android.content.Context
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.frost.woo_withemark.R
import com.frost.woo_withemark.amazon.AmazonUtil
import com.frost.woo_withemark.amazon.S3Utils
import dagger.hilt.android.qualifiers.ApplicationContext
import rx.Observable
import java.io.File


class AWSRepository(val context: Context) {


    private val transferUtility: TransferUtility? = AmazonUtil.getTransferUtility(context)
    private var s3UploadInterface: S3UploadInterface? = null

    fun initUpload(filePath: String, bucketName: String): Observable<String> {
        val file = File(filePath)
        val myObjectMetadata = ObjectMetadata()
        myObjectMetadata.contentType = "image/png"
        val mediaUrl = file.name
        val observer = transferUtility!!.upload(bucketName, mediaUrl, file)
        observer.setTransferListener(UploadListener())
        return Observable.just(S3Utils.generates3ShareUrl(context, filePath, bucketName))
    }

    private inner class UploadListener : TransferListener {
        // Simply updates the UI list when notified.
        override fun onError(id: Int, e: Exception) {
            s3UploadInterface!!.onUploadError(e.toString())
            s3UploadInterface!!.onUploadError("Error")
        }

        override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
        }

        override fun onStateChanged(id: Int, newState: TransferState) {
            if (newState == TransferState.COMPLETED) {
                s3UploadInterface!!.onUploadSuccess("Success")
            }
        }
    }

    fun setOns3UploadDone(s3UploadInterface: S3UploadInterface?) {
        this.s3UploadInterface = s3UploadInterface
    }

    interface S3UploadInterface {
        fun onUploadSuccess(response: String?)
        fun onUploadError(response: String?)
    }

    companion object {
        private const val TAG = "S3Uploader"
    }

}