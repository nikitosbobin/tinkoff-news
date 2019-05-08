package ru.tinkoff.news.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CacheMarker(

    @PrimaryKey
    @ColumnInfo(name = "cacheKey")
    val cacheKey: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long
)
