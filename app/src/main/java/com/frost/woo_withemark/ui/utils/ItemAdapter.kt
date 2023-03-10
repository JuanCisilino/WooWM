package com.frost.woo_withemark.ui.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.frost.woo_withemark.databinding.ItemBinding
import com.frost.woo_withemark.models.WooProduct

class ItemAdapter(private val postlist: List<WooProduct>, private val context: Context)
    : RecyclerView.Adapter<ItemAdapter.ViewHolder>(){

    var onProductClickCallback : ((product: WooProduct) -> Unit)? = null
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
                binding.nameTextView.text = name
                binding.priceText.text = "$ $price"
                if (images.isNotEmpty()) glideIt(images[0].src, binding.image)
                binding.cardLayout.setOnClickListener { onProductClickCallback?.invoke(this) }
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