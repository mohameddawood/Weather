package com.app.robusta.weather.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.robusta.weather.databinding.ItemHistoryBinding
import com.app.robusta.weather.db.entities.HistoryWeatherItem


class HistoryAdapter(var items: ArrayList<HistoryWeatherItem> = arrayListOf()) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {


    class HistoryViewHolder(val bind: ItemHistoryBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, i: Int) {
        holder.bind.tvTime.text = items[i].time
        holder.bind.tvLocation.text = items[i].location
        holder.bind.tvDegree.text = "${items[i].status}\n${items[i].temperature}"
        holder.bind.ivPicItem.setImageBitmap(items[i].photo)
    }

    fun addItem(item:HistoryWeatherItem){
        val size = items.size
        items.add(0,item)
        notifyItemInserted(0)
    }

    override fun getItemCount() = items.size
}