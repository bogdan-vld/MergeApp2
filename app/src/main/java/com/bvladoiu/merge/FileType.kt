package com.bvladoiu.merge

import kotlin.random.Random

sealed class FileType(val fileId: Int) {
    var size: Size = Size.SMALL

    init {
        if (Random.nextInt(3)>1) {
            size = Size.BIG
        }
    }

    data class File(val id: Int) : FileType(id){
        override fun toString(): String = "File $id"
    }
    data class Directory(val id: Int) : FileType(id){
        override fun toString(): String = "Directory $id"
    }
}


enum class Size {
    BIG,
    SMALL
}