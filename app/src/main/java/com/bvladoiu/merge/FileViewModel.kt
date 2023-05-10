package com.bvladoiu.merge

import androidx.lifecycle.ViewModel

object FileViewModel : ViewModel() {
    private val data = ArrayList<MyData>()
    private var maxFileId = 0
    private var maxDirectoryId = 0
    private val MAX_FILE_COUNT = 30


    fun getFiles(): MutableList<MyData> {
        ensureData()
        return data
    }

    /*fun merge(selectedItemIds: MutableList<MyData>) {
        if (selectedItemIds.isEmpty()) return
        var minIndex = Int.MAX_VALUE
        for (selected in selectedItemIds) {
            data.find { it -> it.fileId == selected.fileId }.let {
                minIndex = min(data.indexOf(it), minIndex)
                data.remove(it)
            }
        }
        data.add(minIndex, MyData.Directory(getNextDirectoryId()))
    }*/

    private fun getNextDirectoryId(): Int {
        return ++maxDirectoryId
    }

    fun ensureData(): Int {
        var result = 0
        if (data.size < MAX_FILE_COUNT) {
            result = MAX_FILE_COUNT - data.size
            for (i in data.size..MAX_FILE_COUNT) data.add(MyData.File(maxFileId + i))
        }
        maxFileId = MAX_FILE_COUNT
        return result
    }

    fun createNextDirectory(): MyData {
        val result = MyData.Directory(getNextDirectoryId())
        return result
    }
}
