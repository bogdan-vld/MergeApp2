package com.bvladoiu.merge

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager


class MyAdapter(private var dataList: MutableList<MyData>) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    private var selectedItems = mutableListOf<Int>()

    fun toggleSelect(index: Int) {
        if (index !in selectedItems) {
            selectedItems.add(index)
        } else {
            selectedItems.remove(index)
        }
    }

    fun isSelected(index: Int): Boolean = selectedItems.contains(index)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // randomly set the layout params to take 1 or 2 columns
        val layoutParams = holder.itemView.layoutParams as FlexboxLayoutManager.LayoutParams
        if (dataList[position].size == Size.SMALL) {
            layoutParams.flexBasisPercent = 0.5f
            //layoutParams.width =widthPx/2
            // set the height based on the width using the aspect ratio
            //layoutParams.height = (holder.itemView.width * 1.5f).toInt()
        } else {
            layoutParams.flexBasisPercent = 1.0f
            // set the height based on the width using the aspect ratio
            //layoutParams.height = (holder.itemView.width * 1.5f * 0.5f).toInt()
            // set the width to half of the parent's width
            //layoutParams.width = holder.itemView.width / 2
        }
        //layoutParams.height = (layoutParams.width*1.5).toInt()
        Log.d(
            "LOGPOP", "onBindViewHolder: width:${layoutParams.width} height:${layoutParams.height}"
        )
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val title: TextView = itemView.findViewById(R.id.title)
        private val icon: ImageView = itemView.findViewById(R.id.icon)

        init {
            itemView.setOnClickListener {
                val id = dataList[adapterPosition].fileId
                // toggle the selected state of the item view
                toggleSelect(adapterPosition)
                // update the background color to show the selected state
                // notify the adapter that the item view's state has changed
                if (isSelected(adapterPosition)) {
                    icon.setColorFilter(ContextCompat.getColor(icon.context, R.color.light_blue));
                } else {
                    icon.setColorFilter(ContextCompat.getColor(icon.context, R.color.white));
                }
                notifyItemChanged(adapterPosition)
            }
        }


        fun bind(myData: MyData) {
            title.text = myData.toString()
            when (myData is MyData.File) {
                true -> icon.setImageDrawable(
                    ContextCompat.getDrawable(
                        icon.context,
                        R.drawable.draft_fill0_wght400_grad0_opsz48
                    )
                )

                else -> {
                    icon.setImageDrawable(
                        ContextCompat.getDrawable(
                            icon.context,
                            R.drawable.outline_folder_24
                        )
                    )
                }
            }

            if (isSelected(adapterPosition)) {
                icon.setColorFilter(ContextCompat.getColor(icon.context, R.color.light_blue));
            } else {
                icon.setColorFilter(ContextCompat.getColor(icon.context, R.color.white));
            }
        }

    }

    fun updateItems() {
        if (selectedItems.isEmpty()) return
        selectedItems.sort()
        val directoryIndex = selectedItems.removeAt(0)
        dataList[directoryIndex] = FileViewModel.createNextDirectory()
        notifyItemChanged(directoryIndex)
        if (selectedItems.isEmpty()) return
        var offset = 0
        val iterator = selectedItems.iterator()
        while (iterator.hasNext()) {
            val index = iterator.next()
            dataList.removeAt(index - offset)
            notifyItemRemoved(index - offset)
            ++offset
            Log.e("LOGPOP", "updateItems: removed position:${index - offset}")
        }
        selectedItems.clear()
    }
}


