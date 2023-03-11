package com.frost.woo_withemark.ui.addedit

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.frost.woo_withemark.R
import com.frost.woo_withemark.databinding.ActivityAddEditBinding
import com.frost.woo_withemark.extensions.createImageFile
import com.frost.woo_withemark.extensions.showToast
import com.frost.woo_withemark.models.Images
import com.frost.woo_withemark.models.WooProduct
import com.frost.woo_withemark.ui.utils.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException

@AndroidEntryPoint
class AddEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditBinding
    lateinit var photoURI: Uri

    private val viewModel by viewModels<AddEditViewModel>()
    private var loadingDialog = LoadingDialog()
    private val name = System.currentTimeMillis().toString()

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { imageUri ->
            glideUri(imageUri)
            viewModel.path = imageUri.path }
    }


    companion object {
        const val CAMARA_REQUEST = 100

        fun start(activity: Activity, id: Int){
            val intent = Intent(activity, AddEditActivity::class.java)
            intent.putExtra("id", id)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkIntents()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.productLiveData.observe(this) { handleProduct(it) }
        viewModel.saveProductLiveData.observe(this) { handleSavedProduct(it) }
        viewModel.imageProductLiveData.observe(this) { handleImageProduct(it) }
    }

    private fun handleImageProduct(image: Unit?) {
        image
            ?.let { finish() }
            ?:run {
                loadingDialog.dismiss()
                showToast(this, getString(R.string.error_image)) }
    }

    private fun handleSavedProduct(product: WooProduct?) {
        product
            ?.let { finish() }
            ?:run {
                loadingDialog.dismiss()
                showToast(this, getString(R.string.error_saved)) }
    }

    private fun saveImage(product: WooProduct) {
        val file = viewModel.path?.let { File(it) }
        val imageRequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val part = MultipartBody.Part.createFormData("image", file!!.name , imageRequestBody)
        viewModel.saveImage(product.id!!, part, getString(R.string.key), getString(R.string.customer))
    }

    private fun handleProduct(wooProduct: WooProduct?) {
        loadingDialog.dismiss()
        wooProduct
            ?.let { setProduct(it) }
            ?:run { showToast(this, getString(R.string.error_prod)) }
    }

    private fun setProduct(wooProduct: WooProduct) {
        with(binding){
            nameTextView.hint = wooProduct.name
            descriptionTextView.hint = wooProduct.description
            priceText.hint = "$ ${wooProduct.price}"
            if (wooProduct.images.isNotEmpty()) glideUrl(wooProduct.images[0].src)
        }
    }

    private fun glideUrl(url: String) {
        Glide.with(this)
            .load(url)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.image)
    }

    private fun glideUri(uri: Uri) {
        binding.sendButton.visibility = View.VISIBLE
        Glide.with(this)
            .load(uri)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.image)
    }

    private fun checkIntents() {
        val id = intent.getIntExtra("id", 0)
        if (id == 0) showAddLayout() else showEditLayout(id)
        setButtons()
    }

    private fun setButtons() {
        with(binding){
            camaraImage.setOnClickListener { dispatchTakePictureIntent() }
            galleryImage.setOnClickListener { getContent.launch("image/*") }
            sendButton.setOnClickListener { validateAndSave() }
        }
    }

    private fun validateAndSave() {
        viewModel.productLiveData.value
            ?.let { checkEditProduct(it) }
            ?:run { checkNewProduct() }
    }

    private fun checkNewProduct() {
        when {
            binding.nameTextView.text.isNullOrBlank() -> binding.nameTextView.hint = getString(R.string.insert_text)
            binding.descriptionTextView.text.isNullOrBlank() -> binding.descriptionTextView.hint = getString(R.string.insert_text)
            binding.priceText.text.isNullOrBlank() -> binding.priceText.hint = getString(R.string.insert_precio)
            else -> createAndSave()
        }
    }

    private fun createAndSave() {
        loadingDialog.show(supportFragmentManager)
        val newProduct = WooProduct(
            name = binding.nameTextView.text.toString(),
            description = binding.descriptionTextView.text.toString(),
            regular_price = binding.priceText.text.toString(),
            images = listOf(Images("https://www.digitalsport.com.ar/files/products/624b1bc16431e-565339-500x500.jpg"))
        )
        viewModel.saveProduct(newProduct, getString(R.string.key), getString(R.string.customer))
    }

    private fun checkEditProduct(product: WooProduct) {

    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile(name)
                } catch (ex: IOException) {
                    null
                }
                viewModel.path = photoFile?.absolutePath
                photoFile?.also {
                    photoURI = FileProvider.getUriForFile(
                        this,
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CAMARA_REQUEST)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_CANCELED) return
        when(requestCode){
            CAMARA_REQUEST -> glideUri(photoURI)
        }
    }

    private fun showEditLayout(id: Int) {
        loadingDialog.show(supportFragmentManager)
        viewModel.getProduct(id, getString(R.string.key), getString(R.string.customer))
    }

    private fun showAddLayout() {
        binding.sendButton.visibility = View.GONE
    }

}