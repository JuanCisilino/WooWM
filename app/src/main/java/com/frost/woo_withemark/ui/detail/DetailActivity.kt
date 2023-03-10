package com.frost.woo_withemark.ui.detail

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.frost.woo_withemark.R
import com.frost.woo_withemark.databinding.ActivityDetailBinding
import com.frost.woo_withemark.extensions.showToast
import com.frost.woo_withemark.models.WooProduct
import com.frost.woo_withemark.ui.home.HomeActivity
import com.frost.woo_withemark.ui.utils.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel>()
    private var loadingDialog = LoadingDialog()

    companion object {
        fun start(activity: Activity, id: Int){
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra("id", id)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingDialog.show(supportFragmentManager)
        val id = intent.getIntExtra("id",0)
        viewModel.getProduct(id, getString(R.string.key), getString(R.string.customer))
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.productLiveData.observe(this) { handleProduct(it) }
    }

    private fun handleProduct(wooProduct: WooProduct?) {
        loadingDialog.dismiss()
        wooProduct
            ?.let { setProduct(it) }
            ?:run { showToast(this, getString(R.string.error_prod))}
    }

    private fun setProduct(wooProduct: WooProduct) {
        with(binding){
            binding.nameTextView.text = wooProduct.name
            binding.priceText.text = "$ ${wooProduct.price}"
            binding.idText.text = "ID: ${wooProduct.id.toString()}"
            if (wooProduct.images.isNotEmpty()) glideIt(wooProduct.images[0].src)
        }
    }

    private fun glideIt(url: String) {
        Glide.with(this)
            .load(url)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.image)
    }

}