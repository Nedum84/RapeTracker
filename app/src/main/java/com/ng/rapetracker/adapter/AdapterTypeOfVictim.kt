package com.ng.rapetracker.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ng.rapetracker.databinding.ItemRapeVictimFormBinding
import com.ng.rapetracker.model.RapeTypeOfVictim


class AdapterTypeOfVictim(private val clickListener: RapeTypeOfVictimClickListener) : RecyclerView.Adapter<AdapterTypeOfVictim.ViewHolder>() {

    var rapeTypeOfVictim: List<RapeTypeOfVictim> = emptyList()
        set(value) {
            field = value

            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = rapeTypeOfVictim.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val curItem = rapeTypeOfVictim[position]

        holder.bind(clickListener, curItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRapeVictimFormBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: ItemRapeVictimFormBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: RapeTypeOfVictimClickListener, rapeTypeOfVictim: RapeTypeOfVictim) {


            binding.rapeVictim = rapeTypeOfVictim
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

    }

}
//CLICK LISTENER

class RapeTypeOfVictimClickListener(val clickListener: (RapeTypeOfVictim) -> Unit) {
    fun onClick(rapeTypeOfVictim: RapeTypeOfVictim) = clickListener(rapeTypeOfVictim)
}

