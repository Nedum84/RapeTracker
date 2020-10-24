package com.ng.gbv_tracker.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ng.gbv_tracker.R
import com.ng.gbv_tracker.databinding.ItemRapeSupportTypeFormBinding
import com.ng.gbv_tracker.databinding.ItemRapeSupportTypeRegBinding
import com.ng.gbv_tracker.databinding.ItemRapeTypeFormBinding
import com.ng.gbv_tracker.model.RapeSupportType


class AdapterRapeSupportOrgType(private val clickListener: RapeSupportOrgClickListener) : RecyclerView.Adapter<AdapterRapeSupportOrgType.ViewHolder>() {

    var rapeSupportTypes: List<RapeSupportType> = emptyList()
        set(value) {
            field = value

            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = rapeSupportTypes.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val curItem = rapeSupportTypes[position]

        holder.bind(clickListener, curItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRapeSupportTypeRegBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: ItemRapeSupportTypeRegBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: RapeSupportOrgClickListener, rapeSupportType: RapeSupportType) {

            binding.rapeSupport = rapeSupportType

            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

    }

}
class AdapterRapeSupportOrgType_LogForm(private val clickListener: RapeSupportOrgClickListener) : RecyclerView.Adapter<AdapterRapeSupportOrgType_LogForm.ViewHolder>() {

    var rapeSupportTypes: List<RapeSupportType> = emptyList()
        set(value) {
            field = value

            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = rapeSupportTypes.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val curItem = rapeSupportTypes[position]

        holder.bind(clickListener, curItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRapeSupportTypeFormBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: ItemRapeSupportTypeFormBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: RapeSupportOrgClickListener, rapeSupportType: RapeSupportType) {

            binding.rapeSupport = rapeSupportType

            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

    }

}


//CLICK LISTENER
class RapeSupportOrgClickListener(val clickListener: (RapeSupportType) -> Unit) {
    fun onClick(rapeSupportType: RapeSupportType) = clickListener(rapeSupportType)
}

