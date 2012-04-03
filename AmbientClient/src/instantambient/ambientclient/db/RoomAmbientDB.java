package instantambient.ambientclient.db;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONException;
import org.json.JSONObject;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class RoomAmbientDB {

	private  static final String AMBIENT_ID_DB = "ambientId";
	private static final String TEMPERATUR_DB = "temperature";
	private static final String LIGHT_DB = "light";
	
	private static final String LIGHT_RED_DB = "redLight";
	private static final String LIGHT_GREEN_DB = "greenLight";
	private static final String LIGHT_BLUE_DB = "blueLigth";
	
	private static final String AMBIENT_NAME = "ambientName";
	
	private String ambientId = "";
	private double temperature = 0;
	private int light = 0;
	private double redLight = 0;
	private double greenLight = 0;
	private double blueLight = 0;
	private String ambientName = "";
	private String roomType = "";
	private String refAmbiendId = "";
	
	/*
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final String CREATE_SQL = "CREATE TABLE IF NOT EXISTS RoomAmbient (ambientId TEXT PRIMARY KEY, temperature DOUBLE, light INTEGER, " +
			"redLight DOUBLE, greenLight DOUBLE, blueLight DOUBLE, ambientName TEXT, roomType TEXT, refAmbiendId TEXT,"
			+ " UNIQUE(ambientId));";
	private static final String SELECT_SQL = "SELECT ambientId,temperature,light,redLight,greenLight,blueLight,ambientName,roomType,refAmbiendId FROM RoomAmbient";
	private static final String INSERT_SQL = "INSERT INTO RoomAmbient (ambientId,temperature,light,redLight,greenLight,blueLight,ambientName,roomType,refAmbiendId) VALUES (?,?,?,?,?,?,?,?,?)";

	private static final String UPDATE_SQL = "UPDATE RoomAmbient SET temperature=?,light=?,redLight=?,greenLight=?,blueLight=?,ambientName=?,roomType=?,refAmbiendId=? WHERE ambientId=?";
	
	private static final String DELETE_SQL_BEGIN = "DELETE FROM RoomAmbient WHERE ambientId in (";
	
	
	public String getAmbientId() {
		return ambientId;
	}
	public void setAmbientId(String ambientId) {
		this.ambientId = ambientId;
	}
	public double getTemperature() {
		return temperature;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	public int getLight() {
		return light;
	}
	public void setLight(int light) {
		this.light = light;
	}
	public double getRedLight() {
		return redLight;
	}
	public void setRedLight(double redLight) {
		this.redLight = redLight;
	}
	public double getGreenLight() {
		return greenLight;
	}
	public void setGreenLight(double greenLight) {
		this.greenLight = greenLight;
	}
	public double getBlueLight() {
		return blueLight;
	}
	public void setBlueLight(double blueLight) {
		this.blueLight = blueLight;
	}
	
	public String getAmbientName() {
		return ambientName;
	}
	public void setAmbientName(String ambientName) {
		this.ambientName = ambientName;
		this.ambientId = ambientName;
	}
	public String getRoomType() {
		return roomType;
	}
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	public String getRefAmbiendId() {
		return refAmbiendId;
	}
	public void setRefAmbiendId(String refAmbiendId) {
		this.refAmbiendId = refAmbiendId;
	}
	public static void createTable(SQLiteDatabase db) {
		db.execSQL(RoomAmbientDB.CREATE_SQL);
	}

	
	private static RoomAmbientDB createRoomAmbientFromCursor(Cursor c) {
		RoomAmbientDB data = new RoomAmbientDB(); 
		data.ambientId = c.getString(0);
		data.temperature = c.getDouble(1);
		data.light = c.getInt(2);
		data.redLight = c.getDouble(3);
		data.greenLight = c.getDouble(4);
		data.blueLight = c.getDouble(5);
		data.ambientName = c.getString(6);
		data.roomType = c.getString(7);
		data.refAmbiendId = c.getString(8);
		return data;
	}

	public static ArrayList<RoomAmbientDB> readRoomAmbient(SQLiteDatabase db) {
		ArrayList<RoomAmbientDB> ambientList = new ArrayList<RoomAmbientDB>();
		Cursor c = null;
		try {
			c = db.rawQuery(RoomAmbientDB.SELECT_SQL, null);

			if (c != null) {
				c.moveToFirst();
			}
			for (int i = 0; i < c.getCount(); i++) {
				c.moveToPosition(i);
				RoomAmbientDB ambient = RoomAmbientDB.createRoomAmbientFromCursor(c);
				if(ambient != null) {
					ambientList.add(ambient);
				}
			}
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return ambientList;
	}

	public static void addRoomAmbient(SQLiteDatabase db, RoomAmbientDB ra) {
		try {
			db.beginTransaction();
			SQLiteStatement stmt = db.compileStatement(RoomAmbientDB.INSERT_SQL);
			stmt.clearBindings();
			if(ra.ambientName != null)stmt.bindString(1, ra.ambientName + ra.refAmbiendId);
			stmt.bindDouble(2, ra.temperature);
			stmt.bindLong(3, ra.light);
			stmt.bindDouble(4, ra.redLight);
			stmt.bindDouble(5, ra.greenLight);
			stmt.bindDouble(6, ra.blueLight);
			if(ra.ambientName != null)stmt.bindString(7, ra.ambientName);
			if(ra.roomType != null)stmt.bindString(8, ra.roomType);
			if(ra.refAmbiendId != null)stmt.bindString(9, ra.refAmbiendId);
			stmt.execute();
			stmt.close();
			db.setTransactionSuccessful();
		} finally {
			if (db.inTransaction()) {
				db.endTransaction();
			}
		}
	}


	/**
	 * Gibt den aktuellen Raum als JSON-Objekt zurück.
	 * 
	 * @return JSONObject oder null bei Fehlern.
	 */
	public JSONObject toJSON() {
		JSONObject roomAmbientObject = new JSONObject();
		try {
			if(roomType != null && roomType.equals(AmbientData.BEDROOM)) {
				roomAmbientObject.put(RoomAmbientDB.LIGHT_DB, light);
			}
			if(roomType != null && roomType.equals(AmbientData.LIVINGROOM)) {
				roomAmbientObject.put("red", redLight);
				roomAmbientObject.put("green", greenLight);
				roomAmbientObject.put("blue", blueLight);
			}
		} catch (JSONException e1) {
			return null;
		}
		return roomAmbientObject;
	}
	
	
	/**
	 * Gibt den aktuellen Raum als JSON-Objekt zurück.
	 * 
	 * @return JSONObject oder null bei Fehlern.
	 */
	public JSONObject toJSONAll() {
//		JSONObject roomAmbientObject = new JSONObject();
		JSONObject roomNameObjekt = new JSONObject();
		JSONObject livingRoom = new JSONObject();
		JSONObject bedroom = new JSONObject();
		
		try {
			livingRoom.put("red", redLight);
			livingRoom.put("green", greenLight);
			livingRoom.put("blue", blueLight);
			bedroom.put(RoomAmbientDB.LIGHT_DB, light);
			roomNameObjekt.put("living_room", livingRoom);
			roomNameObjekt.put("bedroom", bedroom);
			roomNameObjekt.put(RoomAmbientDB.TEMPERATUR_DB, this.temperature);
		//	roomNameObjekt.put(ambientName, roomAmbientObject);
		} catch (JSONException e1) {
			return null;
		}
		return roomNameObjekt;
	}

	public static void updateRoomAmbient(SQLiteDatabase db, ArrayList<RoomAmbientDB> raList) {
		try {
			db.beginTransaction();
			SQLiteStatement stmt = db.compileStatement(RoomAmbientDB.UPDATE_SQL);
			for (int n = 0; n < raList.size(); n++) {
				RoomAmbientDB u = raList.get(n);
				stmt.clearBindings();
				stmt.bindString(9, u.ambientId);
				stmt.bindDouble(1, u.temperature);
				stmt.bindLong(2, u.light);
				stmt.bindDouble(3, u.redLight);
				stmt.bindDouble(4, u.greenLight);
				stmt.bindDouble(5, u.blueLight);
				stmt.bindString(6, u.ambientName);
				stmt.bindString(7, u.roomType);
				if( u.refAmbiendId != null)stmt.bindString(8, u.refAmbiendId);
				
				stmt.execute();
			}
			stmt.close();
			db.setTransactionSuccessful();
		} finally {
			if (db.inTransaction()) {
				db.endTransaction();
			}
		}
	}
	
	static void deleteRoomAmbient(SQLiteDatabase db, String ambientName) {
		
		StringBuffer sb = new StringBuffer(DELETE_SQL_BEGIN);
		sb.append("'").append(ambientName).append("'");
		sb.append(')');
		db.execSQL(sb.toString());
		
	}
	
}
