package com.example.ftechtest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImageAdapter(
    private val list: ArrayList<Image>,
    private val context: Context,
    private var callBack: ICallBack?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (MainActivity.checkedLayoutManager == 1) {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.m000_image_linear_act, parent, false)
            LinearHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.m000_image_grind_act, parent, false)
            GrindHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LinearHolder) {
            Glide.with(context).load(list[position].path).centerInside().into(holder.ivImage)

            holder.tvSize.text = "Size: ${list[position].size.toDouble() / 1000} KB"
            val title = list[position].path
            val index = title.lastIndexOf('/')
            holder.tvTitle.text = "Full Title: ${title.substring(index + 1)}"
        }
        if (holder is GrindHolder) {
            Glide.with(context).load(list[position].path).centerCrop().into(holder.ivGrindImage)
        }
    }

    override fun getItemCount() = list.size

    inner class LinearHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var ivImage: ImageView = itemView.findViewById(R.id.iv_Image)
        internal var tvTitle: TextView = itemView.findViewById(R.id.tv_Title)
        internal var tvSize: TextView = itemView.findViewById(R.id.tv_Size)
        private var linear: LinearLayout = itemView.findViewById(R.id.layout_Linear)

        init {
            linear.setOnClickListener {
                callBack?.onClick(adapterPosition)
            }
        }
    }

    inner class GrindHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var ivGrindImage: ImageView = itemView.findViewById(R.id.iv_Grind_Image)
        private var grind: LinearLayout = itemView.findViewById(R.id.layout_Grind)

        init {
            grind.setOnClickListener {
                callBack?.onClick(adapterPosition)
            }
        }
    }

    interface ICallBack {
        fun onClick(position: Int)
    }
}