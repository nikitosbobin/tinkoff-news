package ru.tinkoff.news.api

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.joda.time.DateTime
import java.lang.reflect.Type

class DateTimeDeserializer : JsonDeserializer<DateTime> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): DateTime {
        return DateTime(json.asJsonObject[MILLISECONDS].asLong)
    }

    companion object {
        private const val MILLISECONDS = "milliseconds"
    }
}
