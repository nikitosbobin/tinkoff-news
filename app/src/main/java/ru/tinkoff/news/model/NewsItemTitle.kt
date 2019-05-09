package ru.tinkoff.news.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime
import ru.tinkoff.news.list.ListItem
import java.io.Serializable

@Entity
data class NewsItemTitle(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    val id: String,

    @ColumnInfo(name = "text")
    @SerializedName("text")
    val title: String,

    @ColumnInfo(name = "publicationDate")
    @SerializedName("publicationDate")
    val publicationDate: DateTime
) : Serializable, ListItem
