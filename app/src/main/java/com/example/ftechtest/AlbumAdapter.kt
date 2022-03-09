package com.example.ftechtest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AlbumAdapter(
    private val list: ArrayList<Album>,
    private var callBack: ImageAdapter.ICallBack?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.m000_album_act, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is Holder) {
            holder.tvTitle.text = list[position].titleAlbum
        }
    }

    override fun getItemCount() = list.size

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var tvTitle: TextView = itemView.findViewById(R.id.tv_Title_Album)
        private var layout: LinearLayout? = itemView.findViewById(R.id.layout_Album)

        init {
            layout?.setOnClickListener {
                callBack?.onClick(adapterPosition)
            }
        }
    }

    interface ICallBack {
        fun onClick(position: Int)
    }
}