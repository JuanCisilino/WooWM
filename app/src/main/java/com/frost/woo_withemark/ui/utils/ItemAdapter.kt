package com.frost.woo_withemark.ui.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.frost.woo_withemark.databinding.ItemBinding
import com.frost.woo_withemark.models.WooProduct

class ItemAdapter(
    private val postlist: List<WooProduct>,
    private val context: Context,
    private val isAdmin:Boolean) : RecyclerView.Adapter<ItemAdapter.ViewHolder>(){

    var onProductClickCallback : ((product: WooProduct) -> Unit)? = null
    var onProductEditClickCallback : ((product: WooProduct) -> Unit)? = null
    var onProductDeleteClickCallback : ((product: WooProduct) -> Unit)? = null

    inner class ViewHolder(val binding: ItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ViewHolder {
        val binding = ItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemAdapter.ViewHolder, position: Int) {
        with(holder){
            with(postlist[position]) {
                val product = this
                if (isAdmin) {
                    binding.floatingEdit.visibility = View.VISIBLE
                    binding.floatingDelete.visibility = View.VISIBLE
                }
                if (images.isNotEmpty()) glideIt(images[0].src, binding.image)
                with(binding){
                    nameTextView.text = name
                    priceText.text = "$ $price"
                    cardLayout.setOnClickListener { onProductClickCallback?.invoke(product) }
                    floatingEdit.setOnClickListener { onProductEditClickCallback?.invoke(product) }
                    floatingDelete.setOnClickListener { onProductDeleteClickCallback?.invoke(product) }
                }
            }
        }
    }

    private fun glideIt(url: String, image: ImageView) {
        Glide.with(context)
            .load(url)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(image)
    }

    override fun getItemCount() = postlist.size
}