package com.ng.gbv_tracker.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ng.gbv_tracker.R
import com.ng.gbv_tracker.databinding.ItemNyscAgentBinding
import com.ng.gbv_tracker.databinding.ItemRapeTypeFormBinding
import com.ng.gbv_tracker.databinding.ItemRapeVictimFormBinding
import com.ng.gbv_tracker.databinding.ItemSarcsSupportBinding
import com.ng.gbv_tracker.model.NYSCagent
import com.ng.gbv_tracker.model.SARCsSupport
import com.ng.gbv_tracker.viewmodel.SarcSupports


class AdapterSARCsSupport(private val clickListener: SARCsSupportClickListener) : RecyclerView.Adapter<AdapterSARCsSupport.ViewHolder>() {

    var list: List<SARCsSupport> = emptyList()
        set(value) {
            field = value

            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = list.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val curItem = list[position]

        holder.bind(clickListener, curItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSarcsSupportBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: ItemSarcsSupportBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: SARCsSupportClickListener, item: SARCsSupport) {


            binding.sarcsSupport = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

    }

}
//CLICK LISTENER

interface SARCsSupportClickListener {
    fun onClick(item: SARCsSupport)
    fun onMoreClick(item: SARCsSupport)
}

