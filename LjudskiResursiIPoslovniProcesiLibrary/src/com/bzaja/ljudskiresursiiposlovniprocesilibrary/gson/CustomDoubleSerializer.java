package com.bzaja.ljudskiresursiiposlovniprocesilibrary.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

public class CustomDoubleSerializer implements JsonSerializer<Double> {

    @Override
    public JsonElement serialize(Double t, Type type, JsonSerializationContext jsc) {
        return new JsonPrimitive(t.toString().replace(".", ","));
    }

}
