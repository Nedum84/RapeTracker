package com.ng.gbv_tracker.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ng.gbv_tracker.R
import com.ng.gbv_tracker.databinding.ItemNyscAgentBinding
import com.ng.gbv_tracker.databinding.ItemRapeTypeFormBinding
import com.ng.gbv_tracker.databinding.ItemRapeVictimFormBinding
import com.ng.gbv_tracker.model.NYSCagent


class AdapterNYSCAgent(private val clickListener: NYSCAgentClickListener) : RecyclerView.Adapter<AdapterNYSCAgent.ViewHolder>() {

    var list: List<NYSCagent> = emptyList()
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
        val binding = ItemNyscAgentBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: ItemNyscAgentBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: NYSCAgentClickListener, item: NYSCagent) {


            binding.agent = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

    }

}
//CLICK LISTENER

class NYSCAgentClickListener(val clickListener: (NYSCagent) -> Unit) {
    fun onClick(item: NYSCagent) = clickListener(item)
}

