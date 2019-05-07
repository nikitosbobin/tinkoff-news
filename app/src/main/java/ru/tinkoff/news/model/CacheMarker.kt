package ru.tinkoff.news.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CacheMarker(

    @PrimaryKey
    @ColumnInfo(name = "cacheKey")
    var cacheKey: String,

    @ColumnInfo(name = "timestamp")
    var timestamp: Long
) {
    constructor() : this("", 0L)
}
