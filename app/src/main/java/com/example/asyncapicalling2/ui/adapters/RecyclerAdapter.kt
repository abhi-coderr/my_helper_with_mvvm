package com.example.asyncapicalling2.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.asyncapicalling2.R
import com.example.asyncapicalling2.databinding.RawItemOfRecyclerBinding
import com.example.asyncapicalling2.network.model.Results

class RecyclerAdapter(
    val onEvent :(MyEventSealedClass) -> Unit,

) : ListAdapter<Results ,RecyclerAdapter.RecyclerViewHolder>(DiffUtilCallbackClass()) {

    sealed class MyEventSealedClass {
        data class OnImageClick(val results : Results) : MyEventSealedClass()
        data class OnDialogShow(val results: Results) : MyEventSealedClass()
    }

    inner class RecyclerViewHolder(private val rawItemOfRecyclerBinding: RawItemOfRecyclerBinding)
        : RecyclerView.ViewHolder(rawItemOfRecyclerBinding.root){

            fun bind(list : Results){
                rawItemOfRecyclerBinding.apply {
                    Glide.with(root.context)
                        .load(list.image)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(showImgIv)

                    showTitleTv.text = list.title
                    showDescriptionTv.text = list.description

                    showImgIv.setOnClickListener {
                        onEvent(MyEventSealedClass.OnImageClick(list))
                    }

                    showImgIv.setOnLongClickListener {
                        onEvent(MyEventSealedClass.OnDialogShow(list))
                        true
                    }
                }
            }

    }

    class DiffUtilCallbackClass : DiffUtil.ItemCallback<Results>(){
        override fun areItemsTheSame(oldItem: Results, newItem: Results): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Results, newItem: Results): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: Results, newItem: Results): Any? {

            if (oldItem != newItem){
                return newItem
            }

            return super.getChangePayload(oldItem, newItem)
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(RawItemOfRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: RecyclerViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {

        if (payloads.isNullOrEmpty()){
        super.onBindViewHolder(holder, position, payloads)
        }else{
            val newItem = payloads[0] as Results
            holder.bind(newItem)
        }

    }



}