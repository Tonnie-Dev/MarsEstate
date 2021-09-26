package com.androidshowtime.marsestate.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.androidshowtime.marsestate.databinding.GridViewItemBinding
import com.androidshowtime.marsestate.network.MarsProperty


// Adapter class takes OnClickListener in the constructor

class PhotoGridAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<MarsProperty, PhotoGridAdapter.ViewHolderClass>(DiffCallBack) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderClass {
        return ViewHolderClass(GridViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {

        //use getItem() ListAdapter Method to get the item
        val marsProperty = getItem(position)

        /* set onClickListener to the item

         If RecyclerView does need to access the views stored in the
         ViewHolder, it can do so using the view holder's itemView property*/
        holder.itemView.setOnClickListener { onClickListener.onClick(marsProperty) }
        holder.bind(marsProperty)
    }


    class ViewHolderClass(val binding: GridViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(marsProperty: MarsProperty) {
            binding.marsProperty = marsProperty
            binding.executePendingBindings()
        }
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<MarsProperty>() {
        override fun areItemsTheSame(oldItem: MarsProperty, newItem: MarsProperty): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MarsProperty, newItem: MarsProperty): Boolean {
            return oldItem.id == newItem.id

        }
    }


    //inner class OnClickLister inside the adapter class taking a lambda
    class OnClickListener(val onClickListener: (MarsProperty) -> Unit) {

        //onClick() method takes entity class parameter and is set to OnClickListener
        fun onClick(marsProperty: MarsProperty) = onClickListener(marsProperty)

    }


}

