package com.example.habittracker

import com.google.gson.*
import java.lang.reflect.Type


class HabitJsonSerializer : JsonSerializer<Habit> {
    override fun serialize(
        src: Habit, typeOfSrc: Type, context: JsonSerializationContext
    ): JsonElement = JsonObject().apply {
        addProperty("uid", src.id)
        addProperty("date", src.date)
        addProperty("count", src.count)
        addProperty("description", src.description)
        addProperty("frequency", src.periodicity)
        addProperty("priority", src.priority.ordinal)
        addProperty("title", src.title)
        addProperty("type", src.type.ordinal)
    }
}

class HabitJsonDeserializer : JsonDeserializer<Habit> {
    override fun deserialize(
        json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?
    ): Habit = Habit(
        json.asJsonObject.get("uid").asString,
        json.asJsonObject.get("title").asString,
        json.asJsonObject.get("description").asString,
        Priority.values()[json.asJsonObject.get("priority").asInt],
        HabitType.values()[json.asJsonObject.get("type").asInt],
        json.asJsonObject.get("count").asInt,
        json.asJsonObject.get("frequency").asInt,
        json.asJsonObject.get("date").asInt
    )
}

class HabitHabitWithoutIdJsonSerializer : JsonSerializer<HabitWithoutId> {
    override fun serialize(
        src: HabitWithoutId, typeOfSrc: Type, context: JsonSerializationContext
    ): JsonElement = JsonObject().apply {
        addProperty("date", src.date)
        addProperty("count", src.count)
        addProperty("description", src.description)
        addProperty("frequency", src.periodicity)
        addProperty("priority", src.priority.ordinal)
        addProperty("title", src.title)
        addProperty("type", src.type.ordinal)
    }
}
