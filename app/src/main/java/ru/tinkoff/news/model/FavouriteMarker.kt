package ru.tinkoff.news.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavouriteMarker(
    @PrimaryKey
    @ColumnInfo(name = "newsId")
    var newsId: String
) {
    constructor() : this("")
}
