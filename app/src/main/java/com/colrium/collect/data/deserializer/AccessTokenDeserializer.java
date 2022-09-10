package com.colrium.collect.data.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;

import com.colrium.collect.data.local.model.AccessToken;

public class AccessTokenDeserializer implements JsonDeserializer<AccessToken> {
    @Override
    public AccessToken deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        final JsonObject jsonObject = json.getAsJsonObject();

        final JsonElement jsonKey = jsonObject.get("key");
        final String key = jsonKey.getAsString();

        final JsonElement jsonValue = jsonObject.get("value");
        Object value = null;
        if (jsonValue.isJsonPrimitive()) {
            JsonPrimitive jsonPrimitive = jsonValue.getAsJsonPrimitive();
            if (jsonPrimitive.isBoolean())
                value = jsonValue.getAsBoolean();
            else if (jsonPrimitive.isString())
                value = jsonValue.getAsString();
            else if (jsonPrimitive.isNumber()){
                value = jsonValue.getAsNumber();
            }
        }
        AccessToken typeValue = new AccessToken();
        return typeValue;

    }
}
