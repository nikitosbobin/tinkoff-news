package ru.tinkoff.news.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class NewsItemDetails(
    @PrimaryKey
    @Embedded
    @SerializedName("title")
    val title: NewsItemTitle,

    @ColumnInfo(name = "content")
    @SerializedName("content")
    val content: String
) : Serializable
