package com.ng.rapetracker.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ng.rapetracker.R
import com.ng.rapetracker.databinding.ItemRapeSupportTypeRegBinding
import com.ng.rapetracker.databinding.ItemRapeTypeFormBinding
import com.ng.rapetracker.model.RapeSupportType


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

//            binding.rapeSupport.text = rapeSupportType.rapeSupportType

            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

    }

}
//CLICK LISTENER

class RapeSupportOrgClickListener(val clickListener: (RapeSupportType) -> Unit) {
    fun onClick(rapeSupportType: RapeSupportType) = clickListener(rapeSupportType)
}

