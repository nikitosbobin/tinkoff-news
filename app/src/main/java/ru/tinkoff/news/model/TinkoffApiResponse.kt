package ru.tinkoff.news.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TinkoffApiResponse<T>(
    @SerializedName("payload")
    val payload: T
) : Serializable
