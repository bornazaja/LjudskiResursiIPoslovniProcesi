package com.bzaja.ljudskiresursiiposlovniprocesilibrary.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class CustomDoubleDeserializer implements JsonDeserializer<Double> {

    @Override
    public Double deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        return Double.parseDouble(je.getAsString().replace(",", "."));
    }

}
