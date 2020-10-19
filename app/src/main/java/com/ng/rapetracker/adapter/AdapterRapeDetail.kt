package com.ng.rapetracker.adapter


import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ng.rapetracker.databinding.ItemRapeDetailBinding
import com.ng.rapetracker.model.RapeDetail
import com.ng.rapetracker.room.DatabaseRoom
import com.ng.rapetracker.utils.ClassDateAndTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AdapterRapeDetail(val app:Application, val clickListener: RapeDetailClickListener) : ListAdapter<RapeDetail,AdapterRapeDetail.ViewHolder>(RapeDetailDiffCallback()) {


    private val adapterScope = CoroutineScope(Dispatchers.Default)
    var databaseRoom = DatabaseRoom.getDatabaseInstance(app)

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

        holder.bind(clickListener, curItem,databaseRoom)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemRapeDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(clickListener: RapeDetailClickListener, rapeDetail: RapeDetail, databaseRoom:DatabaseRoom) {

            try {
                binding.rapeReporter?.text = "By: "+rapeDetail.userName.split("/")[0].trim()
                binding.rapeVictim?.text = if (rapeDetail.rapeAgainstYou)"Victim" else "witness"
                binding.rapeDate?.text = ClassDateAndTime().checkDateTimeFirst(rapeDetail.dateAdded)
                CoroutineScope(Dispatchers.Default).launch {
                    try {
                        val rapeType = databaseRoom.rapeTypeDao.getRapeTypeById(rapeDetail.typeOfRape.toLong())
                        binding.typeOfRape?.text = rapeType!!.rapeType
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                binding.rapeDetail = rapeDetail
                binding.clickListener = clickListener
                binding.executePendingBindings()
            } catch (e: Exception) {
                e.printStackTrace()
            }
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

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: RapeDetail, newItem: RapeDetail): Boolean {
        return oldItem == newItem
    }
}
