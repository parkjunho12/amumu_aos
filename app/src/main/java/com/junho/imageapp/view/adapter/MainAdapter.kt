package com.junho.imageapp.view.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.request.LoadRequest
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.junho.imageapp.R
import com.junho.imageapp.database.format.ImageData
import com.squareup.picasso.Picasso

class MainAdapter(private val context: Context, private val itemList: ArrayList<ImageData>):
        RecyclerView.Adapter<MainAdapter.Holder>(){

    var itemClick: ItemClick? = null
    interface ItemClick
    {
        fun onClick(
            view: View,
            position: Int
        )

        fun onLongClick(
            view: View,
            position: Int
        ) : Boolean

        fun deleteItem(
           image: ImageData
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
        holder.bind(itemList[position], position)
        if (itemClick != null){
            holder.itemView.setOnLongClickListener { v -> itemClick?.onLongClick(v, position)!! }
            holder.itemView.setOnClickListener { v -> itemClick?.onClick(v, position)!! }
        }
    }

    fun toggleDeleteButton() {

    }


    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        private val img = itemView?.findViewById<ImageView>(R.id.img)
        private val index = itemView?.findViewById<TextView>(R.id.index_row)
        private val deleteBtn = itemView?.findViewById<ImageButton>(R.id.btn_delete)
        fun bind (image: ImageData, position: Int) {

            val imageLoader = Coil.imageLoader(itemView.context)
            val request = LoadRequest.Companion.Builder(itemView.context)
                .data(Uri.parse(image.imageUri))
                .target(img!!)
                .scale(Scale.FIT)
                .transformations(RoundedCornersTransformation(25f))
                .build()
            imageLoader.execute(request)

            index?.text = (position+1).toString()
            deleteBtn?.setOnClickListener {
                itemList.remove(image)
                itemClick?.deleteItem(image)
                notifyDataSetChanged()
            }
        }

    }

}



