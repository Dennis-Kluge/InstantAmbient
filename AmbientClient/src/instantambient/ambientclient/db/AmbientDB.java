package instantambient.ambientclient.db;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class AmbientDB {

	private static final String AMBIENT_ID_DB = "ambientId";
	private static final String TEMPERATUR_DB = "temperature";
	private static final String LIGHT_DB = "light";
	final static String KEY = "generalA";
	
	private String ambientId = KEY;
	private double temperature = 0;
	private int light = -1;
	
	
	/*
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final String CREATE_SQL = "CREATE TABLE IF NOT EXISTS Ambient (ambientId TEXT PRIMARY KEY, temperature DOUBLE, light INTEGER,"
			+ " UNIQUE(ambientId));";
	private static final String SELECT_SQL = "SELECT ambientId,temperature,light FROM Ambient";
	private static final String INSERT_SQL = "INSERT INTO Ambient (ambientId,temperature,light) VALUES (?,?,?)";

	private static final String UPDATE_SQL = "UPDATE Ambient SET temperature=?,light=? WHERE ambientId=?";
	private static final String DELETE_SQL_BEGIN = "DELETE FROM Ambient WHERE ambientId in (";

	
	public static void createTable(SQLiteDatabase db) {
		db.execSQL(AmbientDB.CREATE_SQL);
	}


	static void deleteAmbient(SQLiteDatabase db, String ambientName) {
		
		StringBuffer sb = new StringBuffer(DELETE_SQL_BEGIN);
		sb.append("'").append(ambientName).append("'");
		sb.append(')');
		db.execSQL(sb.toString());
		
	}

	private static AmbientDB createAmbientFromCursor(final Cursor c) {
		AmbientDB data = new AmbientDB(); 
		data.ambientId = c.getString(0);
		data.temperature = c.getDouble(1);
		data.light = c.getInt(2);
		return data;
	}

	public static ArrayList<AmbientDB> readAmbient(final SQLiteDatabase db) {
		ArrayList<AmbientDB> ambientList = new ArrayList<AmbientDB>();
		Cursor c = null;
		try {
			c = db.rawQuery(AmbientDB.SELECT_SQL, null);

			if (c != null) {
				c.moveToFirst();
			}
			for (int i = 0; i < c.getCount(); i++) {
				c.moveToPosition(i);
				AmbientDB ambient = AmbientDB.createAmbientFromCursor(c);
				if(ambient != null){
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


	public static void addAmbient(SQLiteDatabase db, AmbientDB a) {
		try {
			db.beginTransaction();
			SQLiteStatement stmt = db.compileStatement(AmbientDB.INSERT_SQL);
			stmt.clearBindings();
			stmt.bindString(1, a.ambientId);
			stmt.bindDouble(2, a.temperature);
			stmt.bindLong(3, a.light);
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
	 * Gibt den aktuellen User als JSON-Objekt zurück.
	 * 
	 * @return JSONObject oder null bei Fehlern.
	 */
	public JSONObject toJSON() {
		ArrayList<RoomAmbientDB> ra = AmbientData.getInstance().getRoomAmbients();
		JSONObject generelAmbientObject = new JSONObject();
		
		try {
			for(RoomAmbientDB room : ra){
				if(room != null && room.getRefAmbiendId() != null && room.getRefAmbiendId().equals(ambientId)) {
					generelAmbientObject.put(room.getRoomType(), room.toJSON());
				}
			}
			
		} catch (JSONException e1) {
			return null;
		}
		return generelAmbientObject;
	}

	
	public static void updateAmbient(SQLiteDatabase db, ArrayList<AmbientDB> aList) {
		try {
			db.beginTransaction();
			SQLiteStatement stmt = db.compileStatement(AmbientDB.UPDATE_SQL);
			for (int n = 0; n < aList.size(); n++) {
				AmbientDB u = aList.get(n);
				stmt.clearBindings();
				stmt.bindString(3, u.ambientId);
				stmt.bindDouble(1, u.temperature);
				stmt.bindLong(2, u.light);
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


}
