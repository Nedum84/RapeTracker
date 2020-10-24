package com.ng.gbv_tracker.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ng.gbv_tracker.R
import com.ng.gbv_tracker.databinding.ItemRapeTypeFormBinding
import com.ng.gbv_tracker.model.RapeType
import com.ng.gbv_tracker.utils.ClassAlertDialog


class AdapterRapeType(private val clickListener: RapeTypeClickListener) : RecyclerView.Adapter<AdapterRapeType.ViewHolder>() {

    var mCtx:Context? = null
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
        val binding: ItemRapeTypeFormBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_rape_type_form, parent, false)
        mCtx = parent.context
        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: ItemRapeTypeFormBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: RapeTypeClickListener, rapeType: RapeType) {

            binding.rapeTypeInfo.setOnClickListener {
                mCtx?.let {
                    ClassAlertDialog(it).alertMessage(rapeType.rapeDescription)
                }
            }

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

