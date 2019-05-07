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
    var id: String,

    @ColumnInfo(name = "text")
    @SerializedName("text")
    var title: String,

    @ColumnInfo(name = "publicationDate")
    @SerializedName("publicationDate")
    var publicationDate: DateTime
) : Serializable, ListItem {
    constructor() : this("", "", DateTime(0L))
}

