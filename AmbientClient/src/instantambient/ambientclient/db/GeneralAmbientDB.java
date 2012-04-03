package instantambient.ambientclient.db;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class GeneralAmbientDB {
	
	private static final String AMBIENT_ID_DB = "ambientId";
	private static final String TEMPERATUR_DB = "temperature";
	private static final String LIGHT_DB = "light";
	private static final String FIRST_START_DB = "firstStart";
	final static String KEY = "generalA";
	
	private String ambientId = KEY;
	private double temperature = 0;
	private int light = -1;
	private boolean firstStart = true;
	
	
	/*
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final String CREATE_SQL = "CREATE TABLE IF NOT EXISTS GeneralAmbient (ambientId TEXT PRIMARY KEY, temperature DOUBLE, light INTEGER, firstStart Integer,"
			+ " UNIQUE(ambientId));";
	private static final String SELECT_SQL = "SELECT ambientId,temperature,light,firstStart FROM GeneralAmbient";
	private static final String INSERT_SQL = "INSERT INTO GeneralAmbient (ambientId,temperature,light,firstStart) VALUES (?,?,?,?)";

	private static final String UPDATE_SQL = "UPDATE GeneralAmbient SET temperature=?,light=?,firstStart=? WHERE ambientId=?";
	private static final String DELETE_SQL = "DELETE FROM GeneralAmbient";

	public static void createTable(SQLiteDatabase db) {
		db.execSQL(GeneralAmbientDB.CREATE_SQL);
		addGeneralAmbient(db);
	}

	private static GeneralAmbientDB createGeneralAmbientFromCursor(final Cursor c) {
		GeneralAmbientDB data = new GeneralAmbientDB(); 
		data.temperature = c.getDouble(1);
		data.light = c.getInt(2);
		return data;
	}

	public static void deleteUser(SQLiteDatabase db) {
		db.execSQL(GeneralAmbientDB.DELETE_SQL);
	}

	public static GeneralAmbientDB readGeneralAmbient(final SQLiteDatabase db) {
		GeneralAmbientDB ambient = null;
		Cursor c = null;
		try {
			c = db.rawQuery(GeneralAmbientDB.SELECT_SQL, null);

			if (c != null) {
				c.moveToFirst();
			}
			for (int i = 0; i < c.getCount(); i++) {
				c.moveToPosition(i);
				ambient = GeneralAmbientDB.createGeneralAmbientFromCursor(c);
			}
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return ambient;
	}


	private static void addGeneralAmbient(final SQLiteDatabase db) {
		try {
			db.beginTransaction();
			final SQLiteStatement stmt = db.compileStatement(GeneralAmbientDB.INSERT_SQL);
			stmt.clearBindings();
			stmt.bindString(1, GeneralAmbientDB.KEY);
			stmt.bindDouble(2, 22);
			stmt.bindLong(3, 80);
			stmt.bindLong(4, 1);
			stmt.execute();
			stmt.close();
			db.setTransactionSuccessful();
		} finally {
			if (db.inTransaction()) {
				db.endTransaction();
			}
		}
	}

	
	public JSONObject toJSON() {
		JSONObject generelAmbientObject = new JSONObject();
		JSONObject light = new JSONObject();
		try {
			light.put("brightnes", this.light);
			generelAmbientObject.put(GeneralAmbientDB.TEMPERATUR_DB, this.temperature);
			generelAmbientObject.put(GeneralAmbientDB.LIGHT_DB, light);
		} catch (JSONException e1) {
			return null;
		}
		return generelAmbientObject;
	}

	
	public JSONObject getPraesentationExample() {
		JSONObject livingRoom = new JSONObject();
		JSONObject bedroom = new JSONObject();
		JSONObject generalJson = new JSONObject();
		try{
			livingRoom.put("red", 0.8);
			livingRoom.put("green", 0.2);
			livingRoom.put("blue", 0.1);
			bedroom.put("light", 1);
			generalJson.put("living_room", livingRoom);
			generalJson.put("bedroom", bedroom);
		} catch (JSONException e) {
			return null;
		}
		return generalJson;
	}
	
	public static void updateGeneralAmbient(final SQLiteDatabase db, final GeneralAmbientDB u) {
		try {
			db.beginTransaction();
			final SQLiteStatement stmt = db.compileStatement(GeneralAmbientDB.UPDATE_SQL);
			stmt.clearBindings();
			stmt.bindString(4, u.ambientId);
			stmt.bindDouble(1, u.temperature);
			stmt.bindLong(2, u.light);
			if(u.firstStart) {
				stmt.bindLong(3, 1);
			} else {
				stmt.bindLong(3, 0);
			}
			stmt.execute();
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

	public boolean isFirstStart() {
		return firstStart;
	}

	public void setFirstStart(boolean firstStart) {
		this.firstStart = firstStart;
	}
}
