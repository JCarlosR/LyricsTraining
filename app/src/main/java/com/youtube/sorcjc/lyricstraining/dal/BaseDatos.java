package com.youtube.sorcjc.lyricstraining.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.youtube.sorcjc.lyricstraining.domain.Song;

import java.util.ArrayList;

public class BaseDatos{
	private Context contexto;
	private final  String DATABASE_NAME = "BDRegcliente.db";
	private final String DATABASE_SCRIPT0 = ""
			+ "DROP TABLE IF EXISTS GENRES;";
	private final String DATABASE_SCRIPT1 = ""
			+ "DROP TABLE IF EXISTS SONGS;";
	private final String DATABASE_SCRIPT2 = ""
			+ "DROP TABLE IF EXISTS LYRICS;";
	private final String DATABASE_SCRIPT3 = ""
			+ "CREATE TABLE IF NOT EXISTS"
			+ " GENRES("
			+ "idgenre integer,"
			+ "nombre varchar(50));";
	private final String DATABASE_SCRIPT4 = ""
			+ "CREATE TABLE IF NOT EXISTS"
			+ " SONGS("
			+ "idsong integer,"
			+ "nombre varchar(50),"
			+ "idgenre integer,"
			+ "archivo text);";
	private final String DATABASE_SCRIPT5 = ""+
			"CREATE TABLE IF NOT EXISTS"
			+ " LYRICS("
			+ "idsong integer,"
			+ "letra text,"
			+ "time1 text,"
			+ "time2 text);";

	
	public BaseDatos(Context contexto){
		this.contexto = contexto;
	}
	public void init(){
		SQLiteDatabase sqLiteBaseDatos = contexto.openOrCreateDatabase(DATABASE_NAME,Context.MODE_PRIVATE, null);
		sqLiteBaseDatos.execSQL(DATABASE_SCRIPT0);
		sqLiteBaseDatos.execSQL(DATABASE_SCRIPT1);
		sqLiteBaseDatos.execSQL(DATABASE_SCRIPT2);
		sqLiteBaseDatos.execSQL(DATABASE_SCRIPT3);
		sqLiteBaseDatos.execSQL(DATABASE_SCRIPT4);
		sqLiteBaseDatos.execSQL(DATABASE_SCRIPT5);

		sqLiteBaseDatos.close();
	}
	
	public void insertGeneros(int id, String nombre){
		SQLiteDatabase sqLiteBaseDatos = contexto.openOrCreateDatabase(DATABASE_NAME,	Context.MODE_PRIVATE, null);
		ContentValues fila = new ContentValues();
		fila.put("idgenre", id);
		fila.put("nombre", nombre);
		fila.put("canciones", nombre);
		sqLiteBaseDatos.insert("GENRES", null, fila);
		sqLiteBaseDatos.close();
	}

	public void insertCanciones(int idsong,String nombre,int idgenre,String archivo){
		SQLiteDatabase sqLiteBaseDatos = contexto.openOrCreateDatabase(DATABASE_NAME,	Context.MODE_PRIVATE, null);
		ContentValues fila = new ContentValues();
		fila.put("idsong", idsong);
		fila.put("nombre", nombre);
		fila.put("idgenre", nombre);
		fila.put("archivo", archivo);
		sqLiteBaseDatos.insert("SONGS", null, fila);
		sqLiteBaseDatos.close();
	}

	public void insertLetras(int ids,String letra,String nombre,String apellido){
		SQLiteDatabase sqLiteBaseDatos = contexto.openOrCreateDatabase(DATABASE_NAME,	Context.MODE_PRIVATE, null);
		ContentValues fila = new ContentValues();
		fila.put("idsong", ids);
		fila.put("letra", letra);
		fila.put("time1", nombre);
		fila.put("time2", apellido);
		sqLiteBaseDatos.insert("LYRICS", null, fila);
		sqLiteBaseDatos.close();
	}


	public void countAllEmpresas(){
		SQLiteDatabase sqLiteBaseDatos = contexto.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
		String [] resultados = new String[]{"idEmpresa"};
		Cursor todasFilas = sqLiteBaseDatos.query("GREMPRESA", resultados, null, null, null, null, null,null);
		Integer total = todasFilas.getCount();
		
		sqLiteBaseDatos.close();
	}
	public ArrayList<Song> listAllCanciones(){
		SQLiteDatabase sqLiteBaseDatos = contexto.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
		String [] resultados = new String[]{"idsong","nombre","archivo"};
		Cursor todasFilas = sqLiteBaseDatos.query("SONGS", resultados, null, null, null, null, null,null);

		Integer cids = todasFilas.getColumnIndex("idsong");
		Integer cnom = todasFilas.getColumnIndex("nombre");
		Integer carc = todasFilas.getColumnIndex("archivo");

		Song tempSong = new Song();
		ArrayList<Song> arrlstSong= new ArrayList<Song>();;

		if (todasFilas.moveToFirst()){
			do {
				tempSong = new Song();
					tempSong.setId(Integer.parseInt(todasFilas.getString(cids)));
					tempSong.setFileName(todasFilas.getString(carc));
					tempSong.setName(todasFilas.getString(cnom));
				arrlstSong.add(tempSong);
			}while(todasFilas.moveToNext());
		}
		sqLiteBaseDatos.close();
		return arrlstSong;
	}
	/*

	public ArrayList<FotoEmpresaEN> findEmpresas(String part){
		SQLiteDatabase sqLiteBaseDatos = contexto.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
		String [] resultados = new String[]{"idEmpresa","codigo","descripcion"};

		String where = "descripcion like ? or codigo like ?";
		String []   whereArgs   =  new String []{"%"+part+"%","%"+part+"%"} ;

		String respuesta = "";

		Cursor todasFilas = sqLiteBaseDatos.query("GREMPRESA", resultados, where, whereArgs, null, null, null,null);

		Integer cide = todasFilas.getColumnIndex("idEmpresa");
		Integer ccod = todasFilas.getColumnIndex("codigo");
		Integer cdesc = todasFilas.getColumnIndex("descripcion");

		FotoEmpresaEN temporalFotoEmpresa = new FotoEmpresaEN();
		EmpresaEN temporalEmpresa = new EmpresaEN();
		ArrayList<FotoEmpresaEN> arrlstFotoEmpresa= new ArrayList<FotoEmpresaEN>();;

		if (todasFilas.moveToFirst()){
			do {
				//respuesta +=todasFilas.getString(cindex)+",";
				temporalFotoEmpresa = new FotoEmpresaEN();
				temporalEmpresa = new EmpresaEN();

				temporalEmpresa.setIdEmpresa(Integer.parseInt(todasFilas.getString(cide)));
				temporalEmpresa.setCodigo(todasFilas.getString(ccod));
				temporalEmpresa.setDescripcion(todasFilas.getString(cdesc));
				temporalFotoEmpresa.setEmpresa(temporalEmpresa);

				arrlstFotoEmpresa.add(temporalFotoEmpresa);

			}while(todasFilas.moveToNext());
		}

		sqLiteBaseDatos.close();
		return arrlstFotoEmpresa;

	}

	public ArrayList<FotoClienteEN> listAllClientes(){
		SQLiteDatabase sqLiteBaseDatos = contexto.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
		String [] resultados = new String[]{"idEmpresa","nombre","apellido","direccion","dni","email","cargoEmpresa"};

		String respuesta = "";

		Cursor todasFilas = sqLiteBaseDatos.query("GRCLIENTE", resultados, null, null, null, null, null,null);

		Integer cide = todasFilas.getColumnIndex("idEmpresa");
		Integer cnom = todasFilas.getColumnIndex("nombre");
		Integer cape = todasFilas.getColumnIndex("apellido");
		Integer cdir = todasFilas.getColumnIndex("direccion");
		Integer cdni = todasFilas.getColumnIndex("dni");
		Integer cemail = todasFilas.getColumnIndex("email");
		Integer ccar = todasFilas.getColumnIndex("cargoEmpresa");
		FotoClienteEN temporalFotoCliente = new FotoClienteEN();
		ClienteEN temporalCliente = new ClienteEN();
		ArrayList<FotoClienteEN> arrlstFotoCliente= new ArrayList<FotoClienteEN>();;

		if (todasFilas.moveToFirst()){
			do {
				//respuesta +=todasFilas.getString(cindex)+",";
				temporalFotoCliente = new FotoClienteEN();
				temporalCliente = new ClienteEN();

				temporalCliente.setIdEmpresa(Integer.parseInt(todasFilas.getString(cide)));
				temporalCliente.setNombre(todasFilas.getString(cnom));
				temporalCliente.setApellido(todasFilas.getString(cape));
				temporalCliente.setDireccionDomicilio(todasFilas.getString(cdir));
				temporalCliente.setDni(todasFilas.getString(cdni));
				temporalCliente.setEmail(todasFilas.getString(cemail));
				temporalCliente.setCargoEmpresa(todasFilas.getString(ccar));
				temporalFotoCliente.setCliente(temporalCliente);

				arrlstFotoCliente.add(temporalFotoCliente);

			}while(todasFilas.moveToNext());
		}

		sqLiteBaseDatos.close();
		return arrlstFotoCliente;

	}

	public ArrayList<FotoClienteEN> findClientes(String part){
		SQLiteDatabase sqLiteBaseDatos = contexto.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
		String [] resultados = new String[]{"idEmpresa","nombre","apellido","direccion","dni","email","cargoEmpresa"};

		String where = "nombre like ? or dni like ? or apellido like ?";
		String []   whereArgs   =  new String []{"%"+part+"%","%"+part+"%","%"+part+"%"} ;

		String respuesta = "";

		Cursor todasFilas = sqLiteBaseDatos.query("GRCLIENTE", resultados, where, whereArgs, null, null, null,null);

		Integer cide = todasFilas.getColumnIndex("idEmpresa");
		Integer cnom = todasFilas.getColumnIndex("nombre");
		Integer cape = todasFilas.getColumnIndex("apellido");
		Integer cdir = todasFilas.getColumnIndex("direccion");
		Integer cdni = todasFilas.getColumnIndex("dni");
		Integer cemail = todasFilas.getColumnIndex("email");
		Integer ccar = todasFilas.getColumnIndex("cargoEmpresa");
		FotoClienteEN temporalFotoCliente = new FotoClienteEN();
		ClienteEN temporalCliente = new ClienteEN();
		ArrayList<FotoClienteEN> arrlstFotoCliente= new ArrayList<FotoClienteEN>();;

		if (todasFilas.moveToFirst()){
			do {
				//respuesta +=todasFilas.getString(cindex)+",";
				temporalFotoCliente = new FotoClienteEN();
				temporalCliente = new ClienteEN();

				temporalCliente.setIdEmpresa(Integer.parseInt(todasFilas.getString(cide)));
				temporalCliente.setNombre(todasFilas.getString(cnom));
				temporalCliente.setApellido(todasFilas.getString(cape));
				temporalCliente.setDireccionDomicilio(todasFilas.getString(cdir));
				temporalCliente.setDni(todasFilas.getString(cdni));
				temporalCliente.setEmail(todasFilas.getString(cemail));
				temporalCliente.setCargoEmpresa(todasFilas.getString(ccar));
				temporalFotoCliente.setCliente(temporalCliente);

				arrlstFotoCliente.add(temporalFotoCliente);

			}while(todasFilas.moveToNext());
		}

		sqLiteBaseDatos.close();
		return arrlstFotoCliente;

	}

	public ArrayList<FotoClienteEN> getClientesEmpresa(int id,String part){
		SQLiteDatabase sqLiteBaseDatos = contexto.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
		String [] resultados = new String[]{"idEmpresa","nombre","apellido","direccion","dni","email","cargoEmpresa"};
		String where = "idEmpresa = ? and (nombre like ? or apellido like ? or dni like ? )";
		String []   whereArgs   =  new String []{""+id+"","%"+part+"%","%"+part+"%","%"+part+"%"} ;

		String respuesta = "";

		Cursor todasFilas = sqLiteBaseDatos.query("GRCLIENTE", resultados, where, whereArgs, null, null, null,null);

		Integer cide = todasFilas.getColumnIndex("idEmpresa");
		Integer cnom = todasFilas.getColumnIndex("nombre");
		Integer cape = todasFilas.getColumnIndex("apellido");
		Integer cdir = todasFilas.getColumnIndex("direccion");
		Integer cdni = todasFilas.getColumnIndex("dni");
		Integer cemail = todasFilas.getColumnIndex("email");
		Integer ccar = todasFilas.getColumnIndex("cargoEmpresa");
		FotoClienteEN temporalFotoCliente = new FotoClienteEN();
		ClienteEN temporalCliente = new ClienteEN();
		ArrayList<FotoClienteEN> arrlstFotoCliente= new ArrayList<FotoClienteEN>();;

		if (todasFilas.moveToFirst()){
			do {
				//respuesta +=todasFilas.getString(cindex)+",";
				temporalFotoCliente = new FotoClienteEN();
				temporalCliente = new ClienteEN();

				temporalCliente.setIdEmpresa(Integer.parseInt(todasFilas.getString(cide)));
				temporalCliente.setNombre(todasFilas.getString(cnom));
				temporalCliente.setApellido(todasFilas.getString(cape));
				temporalCliente.setDireccionDomicilio(todasFilas.getString(cdir));
				temporalCliente.setDni(todasFilas.getString(cdni));
				temporalCliente.setEmail(todasFilas.getString(cemail));
				temporalCliente.setCargoEmpresa(todasFilas.getString(ccar));
				temporalFotoCliente.setCliente(temporalCliente);

				arrlstFotoCliente.add(temporalFotoCliente);

			}while(todasFilas.moveToNext());
		}

		sqLiteBaseDatos.close();
		return arrlstFotoCliente;

	}*/

/*
	public boolean login(String usu_usuario,String usu_clave){
		SQLiteDatabase sqLiteBaseDatos = contexto.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
		String [] resultados = new String[]{"idUsuario"};
		
		String where = "usuario=? AND clave=?";
		String []   whereArgs   =  new String []{usu_usuario,usu_clave} ;		
		
		Cursor todasFilas = sqLiteBaseDatos.query("USUARIO", resultados, where, whereArgs, null, null, null,null);
		Integer total = todasFilas.getCount();
		
		sqLiteBaseDatos.close();
		
		return total!=0;
	}*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

