package com.youtube.sorcjc.lyricstraining.global;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.InputStream;
import java.util.Properties;

public class Utilitario {
    public static Properties readProperties(Context ctx, String arc) {
        Properties prop = null;
        try {
            AssetManager am = ctx.getAssets();
            InputStream is = am.open(arc);
            prop = new Properties();
            prop.load(is);
        } catch (Exception ex) {
            Log.e("readProperties","Error",ex);
        }
        return prop;
    }

    /**
     * Carga el archivo "Assets\params.properties" por defecto.
     * @param ctx Context
     * @since 1.6.4
     * @return Archivo Properties
     */
    public static Properties readProperties(Context ctx) {
        return readProperties(ctx, ConfiguracionApp.CONSPROPERTYFILE);
    }
}