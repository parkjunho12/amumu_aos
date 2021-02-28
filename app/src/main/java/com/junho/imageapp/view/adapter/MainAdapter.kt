package com.junho.imageapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.junho.imageapp.R
import com.junho.imageapp.database.format.ImageData


class MainAdapter(private val context: Context, private val itemList: ArrayList<ImageData>):
        RecyclerView.Adapter<MainAdapter.Holder>(){

    private var itemClick: ItemClick? = null
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
        holder.bind(itemList[position])
        if (itemClick != null){
        holder.itemView.setOnClickListener { v ->
            itemClick?.onClick(v, position)

        }}

    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        private val img = itemView?.findViewById<ImageView>(R.id.img)
        private val title = itemView?.findViewById<TextView>(R.id.title)
        private val cost = itemView?.findViewById<TextView>(R.id.cost)
        fun bind (image: ImageData) {


        }
    }

}



