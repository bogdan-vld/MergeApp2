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


class FileAdapter(private var dataList: MutableList<FileType>) :
    RecyclerView.Adapter<FileAdapter.ViewHolder>() {

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

        val layoutParams = holder.itemView.layoutParams as FlexboxLayoutManager.LayoutParams
        if (dataList[position].size == Size.SMALL) {
            layoutParams.flexBasisPercent = 0.5f
        } else {
            layoutParams.flexBasisPercent = 1.0f
        }
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


        fun bind(fileType: FileType) {
            title.text = fileType.toString()
            when (fileType is FileType.File) {
                true -> icon.setImageDrawable(
                    ContextCompat.getDrawable(
                        icon.context,
                        R.drawable.file
                    )
                )

                else -> {
                    icon.setImageDrawable(
                        ContextCompat.getDrawable(
                            icon.context,
                            R.drawable.directory
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


