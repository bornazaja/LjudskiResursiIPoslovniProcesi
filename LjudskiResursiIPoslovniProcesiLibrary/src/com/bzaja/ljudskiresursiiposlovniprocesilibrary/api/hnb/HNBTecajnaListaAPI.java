package com.bzaja.ljudskiresursiiposlovniprocesilibrary.api.hnb;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.gson.CustomDoubleDeserializer;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.gson.CustomDoubleSerializer;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.gson.CustomLocalDateDeserializer;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.gson.CustomLocalDateSerializer;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class HNBTecajnaListaAPI {

    private static final String URL_API = "http://api.hnb.hr/tecajn/v1";

    private HNBTecajnaListaAPI() {

    }

    public static List<TecajnaListaItemDto> get() {
        try {
            URL url = new URL(URL_API);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            Type listType = new TypeToken<ArrayList<TecajnaListaItemDto>>() {
            }.getType();

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Double.class, new CustomDoubleSerializer());
            gsonBuilder.registerTypeAdapter(Double.class, new CustomDoubleDeserializer());
            gsonBuilder.registerTypeAdapter(LocalDate.class, new CustomLocalDateSerializer());
            gsonBuilder.registerTypeAdapter(LocalDate.class, new CustomLocalDateDeserializer());

            List<TecajnaListaItemDto> tecajnaListaItems = gsonBuilder.create().fromJson(reader, listType);
            return tecajnaListaItems;
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
