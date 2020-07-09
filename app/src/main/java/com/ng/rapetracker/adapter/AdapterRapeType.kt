package com.ng.rapetracker.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ng.rapetracker.R
import com.ng.rapetracker.databinding.ItemRapeTypeFormBinding
import com.ng.rapetracker.model.RapeType


class AdapterRapeType(private val clickListener: RapeTypeClickListener) : RecyclerView.Adapter<AdapterRapeType.ViewHolder>() {

    var rapeTypes: List<RapeType> = emptyList()
        set(value) {
            field = value

            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = rapeTypes.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val curItem = rapeTypes[position]

        holder.bind(clickListener, curItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
//        val binding = ItemRapeTypeFormBinding.inflate(layoutInflater, parent, false)
        val binding: ItemRapeTypeFormBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_rape_detail, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: ItemRapeTypeFormBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: RapeTypeClickListener, rapeType: RapeType) {

            binding.textView.text = "Hellow World2!!! ${rapeType.id}"

            binding.rapeType = rapeType
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

    }

}
//CLICK LISTENER

class RapeTypeClickListener(val clickListener: (RapeType) -> Unit) {
    fun onClick(rapeType: RapeType) = clickListener(rapeType)
}

