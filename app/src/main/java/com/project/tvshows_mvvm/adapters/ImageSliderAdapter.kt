package com.project.tvshows_mvvm.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.tvshows_mvvm.databinding.ItemContainerSliderImageBinding

class ImageSliderAdapter(private val sliderImages: Array<String>) :
    RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>() {

    private var layoutInflater: LayoutInflater? = null

    inner class ImageSliderViewHolder(private val itemContainerSliderImageBinding: ItemContainerSliderImageBinding) :
        RecyclerView.ViewHolder(itemContainerSliderImageBinding.root) {

        fun bindSliderImage(imageURL: String) {
            itemContainerSliderImageBinding.imageURL = imageURL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val sliderImageBinding = ItemContainerSliderImageBinding.inflate(
            LayoutInflater.from(layoutInflater!!.context),
            parent,
            false
        )
        return ImageSliderViewHolder(sliderImageBinding)
    }

    override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
        holder.bindSliderImage(sliderImages[position])
    }

    override fun getItemCount(): Int {
        return sliderImages.size
    }
}