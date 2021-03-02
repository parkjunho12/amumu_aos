package com.junho.imageapp.view.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.junho.imageapp.R
import com.junho.imageapp.database.format.ImageData

@GlideModule
class MainAdapter(private val context: Context, private val itemList: ArrayList<ImageData>):
        RecyclerView.Adapter<MainAdapter.Holder>(){

    var itemClick: ItemClick? = null
    interface ItemClick
    {
        fun onClick(
            view: View,
            position: Int
        )
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.cardview_item_gallary, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if (position == 0) {
            holder.firstImage(itemList[position])
        } else {
            holder.bind(itemList[position], position)
        }
        if (itemClick != null){
            holder.itemView.setOnClickListener { v -> itemClick?.onClick(v, position) }}
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        private val img = itemView?.findViewById<ImageView>(R.id.img)
        private val index = itemView?.findViewById<TextView>(R.id.index_row)
        fun bind (image: ImageData, position: Int) {
            index?.text = position.toString()
            Glide.with(itemView)
                .load(Uri.parse(image.imageUri))
                .override(127,127)
                .into(img!!)
        }

        fun firstImage(image: ImageData) {
            Glide.with(itemView)
                .load(Uri.parse(image.imageUri))
                .override(127,127)
                .into(img!!)
        }
    }

}



