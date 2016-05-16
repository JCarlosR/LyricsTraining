package com.youtube.sorcjc.lyricstraining.dal;

import android.content.Context;

public class GlobalDatos {
	public static BaseDatos baseDatos = null;
	public static boolean estadoImagenDefault = true;
	public static int distancia = 100;

	public static void insertarBaseCanciones(Context contexto){
		baseDatos = new BaseDatos(contexto);
		baseDatos.init();
		baseDatos.insertGeneros(1, "rock");
		baseDatos.insertCanciones(1,"Turn down for what",1,"ubicacion/turendown.mp3");
		baseDatos.insertCanciones(1,"Turn down for what",1,"ubicacion/turendown.mp3");
		baseDatos.insertCanciones(1,"Turn down for what",1,"ubicacion/turendown.mp3");

	}
/*
	public static boolean verificar(String usu, String pass){
		return baseDatos.login(usu,pass);
	}
*/

	
}
