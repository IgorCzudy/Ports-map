package com.example.portsmap.module

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MapsAdapter(val context: Context,
                  val userMaps: List<UserMap>, val onClickListener: OnClickListener)
    : RecyclerView.Adapter<MapsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }
    interface OnClickListener{
        fun OnItemClick(position: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent,
        false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userMap = userMaps[position]
        holder.itemView.setOnClickListener{
            Log.i("tag","$position")
            onClickListener.OnItemClick(position)
        }
        val textViewTitle = holder.itemView.findViewById<TextView>(android.R.id.text1)
        textViewTitle.text = userMap.title

    }

    override fun getItemCount() = userMaps.size
}