package com.ng.rapetracker.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ng.rapetracker.databinding.ItemRapeDetailBinding
import com.ng.rapetracker.model.RapeDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AdapterRapeDetail(val clickListener: RapeDetailClickListener) : ListAdapter<RapeDetail,AdapterRapeDetail.ViewHolder>(RapeDetailDiffCallback()) {


    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addNewItems(list: List<RapeDetail>?) {
        adapterScope.launch {
//            val items = list?.map {it}
            withContext(Dispatchers.Main) {
                submitList(list)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val curItem = getItem(position)

        holder.bind(clickListener, curItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemRapeDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListener: RapeDetailClickListener, rapeDetail: RapeDetail) {

            binding.rapeVictim.text = if (rapeDetail.rapeAgainstYou)"Rape Victim" else "Rape witness"
            binding.rapeDate.text = ""

            binding.rapeDetail = rapeDetail
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemRapeDetailBinding.inflate(layoutInflater, parent, false)
//                val binding2: ItemRapeDetailBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_rape_type_form, parent, false)
                return ViewHolder(binding)
            }
        }
    }


}
//CLICK LISTNER

class RapeDetailClickListener(val clickListener: (RapeDetail) -> Unit) {
    fun onClick(rapeDetail: RapeDetail) = clickListener(rapeDetail)
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minumum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class RapeDetailDiffCallback : DiffUtil.ItemCallback<RapeDetail>() {
    override fun areItemsTheSame(oldItem: RapeDetail, newItem: RapeDetail): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RapeDetail, newItem: RapeDetail): Boolean {
        return oldItem == newItem
    }
}
