package instantambient.ambientclient.db;

import instantambient.ambientclient.App;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AmbientData extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "ambient.db";
	private static final int DATABASE_VERSION = 2;
	private static SQLiteDatabase db = null;
	private static AmbientData instance;
	private GeneralAmbientDB generealAmbient;
	private ArrayList<RoomAmbientDB> roomAmbients;
	private static HashMap<String, RoomAmbientDB> roomNames;
	private ArrayList<AmbientDB> ambients;
	private static HashMap<String, AmbientDB> ambientNames;
	
	// Art der Umgebungen
	public static final String BEDROOM = "bedroom";
	public static final String LIVINGROOM = "living_room";
	
	public static final String BEDROOM_SPINNER = "Schlafzimmer";
	public static final String LIVINGROOM_SPINNER = "Wohnzimmer";
	
	public static String curentId = null; 
	
	/**
	 * 
	 * Singleton Instanz.
	 * 
	 * @return AmbientData
	 */
	public static AmbientData getInstance() {
		if (instance == null) {
			instance = new AmbientData(App.getInstance());
			db = instance.getWritableDatabase();
			instance.generealAmbient = GeneralAmbientDB.readGeneralAmbient(db);
		}
		return instance;
	}
	
	public void init(){
		if(generealAmbient == null || roomNames == null || roomAmbients == null || ambientNames == null || ambients == null) {
			generealAmbient = GeneralAmbientDB.readGeneralAmbient(db);
			roomAmbients = RoomAmbientDB.readRoomAmbient(db);
			roomNames = new HashMap<String, RoomAmbientDB>();
			if(roomAmbients != null) {
				for (RoomAmbientDB ra : roomAmbients) {
					if(ra != null) {
						roomNames.put(ra.getAmbientName(), ra);
					}
				}
			}
			ambients = AmbientDB.readAmbient(db);
			ambientNames = new HashMap<String, AmbientDB>();
			if(ambients != null) {
				for (AmbientDB ra : ambients) {
					if(ra != null) {
						ambientNames.put(ra.getAmbientId(), ra);
					}
				}
			}
		}
	}
	
	public AmbientData(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		GeneralAmbientDB.createTable(db);
		RoomAmbientDB.createTable(db);
		AmbientDB.createTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public void updateGeneralAmbient() {
		GeneralAmbientDB.updateGeneralAmbient(db, this.generealAmbient);
	}
	
	public void updateRoomAmbient(RoomAmbientDB ra) {
		RoomAmbientDB.updateRoomAmbient(db, this.roomAmbients);
	}
	
	public void updateAmbient(AmbientDB ra) {
		AmbientDB.updateAmbient(db, this.ambients);
	}
	
	public void addAmbient(AmbientDB ra) {
		AmbientDB.addAmbient(db, ra);
		ambients.add(ra);
		ambientNames.put(ra.getAmbientId(), ra);
	}
	
	public void addRoomAmbient(RoomAmbientDB ra) {
		RoomAmbientDB.addRoomAmbient(db, ra);
		roomAmbients.add(ra);
		roomNames.put(ra.getAmbientName(), ra);
	}
	
	public GeneralAmbientDB getGenerealAmbient() {
		init();
		return generealAmbient;
	}

	public RoomAmbientDB getRoomAmbient(String id) {
		init();
		RoomAmbientDB temp = null;
		if(roomNames != null) {
			temp = roomNames.get(id); 
		}
		return temp;
	}
	
	public AmbientDB getAmbient(String id) {
		init();
		AmbientDB temp = null;
		if(ambientNames != null) {
			temp = ambientNames.get(id); 
		}
		return temp;
	}

	public ArrayList<RoomAmbientDB> getRoomAmbients() {
		init();
		return roomAmbients;
	}

	public ArrayList<AmbientDB> getAmbients() {
		init();
		return ambients;
	}
	
	public void setGenerealAmbient(GeneralAmbientDB generealAmbient) {
		this.generealAmbient = generealAmbient;
	}
	
	public JSONObject getGeneralAmbientJson(){
		init();
		//return this.generealAmbient.toJSON();
		return this.generealAmbient.getPraesentationExample();
	}
	
	
	public JSONObject getRoomAmbientJson(){
		init();
		//return this.generealAmbient.toJSON();
		JSONObject tempJson = new JSONObject();
		if(this.ambients.size() > 0) {
			if(curentId != null) {
				tempJson = this.ambientNames.get(curentId).toJSON();
			} else {
				tempJson = this.ambients.get(0).toJSON();
			}
			 
		}
		return tempJson;
	}

	public void deleteRoomAmbient(String ambientName) {
		AmbientDB.deleteAmbient(db, ambientName);
		ambientNames.remove(ambientName);
		ambients.remove(getAmbient(ambientName));
	}
}
