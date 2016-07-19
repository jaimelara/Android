package com.example.jameslara.login2;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by James Lara on 13/07/2016.
 */
public class JSONUsuarioParser {

    public Usuario readJsonStream(InputStream in) throws IOException{
        JsonReader reader = new JsonReader(
                new InputStreamReader(in, "UTF-8")
        );
        reader.setLenient(true);
        try
        {
            return leerUsuario(reader);
        }finally {
            reader.close();
        }
    }

    public Usuario leerUsuario(JsonReader reader) throws IOException{
        int id=0;
        String nombre = "-";
        reader.beginArray();
        if (reader.hasNext()) {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("id")) {
                    id = reader.nextInt();
                } else if (name.equals("nombre")) {
                    nombre = reader.nextString();
                } else
                    reader.skipValue();
            }
            reader.endObject();
        }
        reader.endArray();
        return new Usuario(nombre,id);
    }
}
