package ru.vladik.dnevnikru.dnevnikapi.models.v7.deserializers

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import ru.vladik.dnevnikru.dnevnikapi.models.v7.FeedInfo
import ru.vladik.dnevnikru.dnevnikapi.models.v7.FeedInfoPrototype
import ru.vladik.dnevnikru.dnevnikapi.models.v7.FeedInfoType
import ru.vladik.dnevnikru.dnevnikapi.models.v7.FeedPost
import java.lang.reflect.Type

class FeedInfoDeserializer : JsonDeserializer<FeedInfoPrototype> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): FeedInfoPrototype {
        val jObject = json.asJsonObject

        try {

            val type = when (context.deserialize<FeedInfoType>(
                jObject.get("type"),
                object : TypeToken<FeedInfoType>() {}.type
            )) {

                FeedInfoType.Post -> {
                    object : TypeToken<FeedInfo<FeedPost>>() {}.type
                }

                //TODO: add more types

                else -> {
                    object : TypeToken<FeedInfo<Any>>() {}.type
                }
            }
            return context.deserialize(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            return context.deserialize(json, object : TypeToken<FeedInfo<Any>>() {}.type)
        }
    }
}