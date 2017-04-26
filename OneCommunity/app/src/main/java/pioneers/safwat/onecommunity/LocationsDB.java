package pioneers.safwat.onecommunity;

/**
 * Created by Ahmad on 16/01/2017.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class LocationsDB extends SQLiteOpenHelper{

    /** Database name */
    private static final String LOCATIONS_DATABASE_NAME = "locationmarkersqlite";

    /** Version number of the database */
   public static final int LOCATION_DATABASE_VERSION = 1;

    /** Field 1 of the table locations, which is the primary key */
    public static final String FIELD_ROW_ID = "_id";

    /** Field 2 of the table locations, stores the latitude */
    public static final String FIELD_LAT = "lat";

    /** Field 3 of the table locations, stores the longitude*/
    public static final String FIELD_LNG = "lng";

    /** Field 4 of the table locations, stores the zoom level of map*/
    public static final String FIELD_NAME = "nam";
    public static final String FIELD_AGE = "age";
    public static final String FIELD_GENDRE = "gen";
    public static final String FIELD_HEFZ = "hfz";
    public static final String FIELD_STUDIES = "std";
    public static final String FIELD_VOLUNTEER = "vol";
    public static final String FIELD_SPORTS = "spt";
    public static final String FIELD_GUID = "gui";
    public static final String FIELD_NATIONALITY = "nat";
    /** A constant, stores the the table name */
    public static final String MY_DATABASE_TABLE = "mlocations";

    /** An instance variable for SQLiteDatabase */
   public SQLiteDatabase mDB;


    /** Constructor */
    public LocationsDB(Context context) {
        super(context, LOCATIONS_DATABASE_NAME, null, LOCATION_DATABASE_VERSION);
        this.mDB = getWritableDatabase();
    }


    /** This is a callback method, invoked when the method getReadableDatabase() / getWritableDatabase() is called
     * provided the database does not exists
     * */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + MY_DATABASE_TABLE + "(" +
                FIELD_ROW_ID + " integer primary key autoincrement," +
                FIELD_NAME +" text," +
                FIELD_AGE +" text," +
                FIELD_GENDRE +" text," +
                FIELD_GUID +" text," +
                FIELD_NATIONALITY +" text," +
                FIELD_STUDIES +" text," +
                FIELD_HEFZ +" text," +
                FIELD_SPORTS +" text," +
                FIELD_VOLUNTEER +" text," +
                FIELD_LNG + " double," +
                FIELD_LAT + " double" +")";
        db.execSQL(sql);
    }

    /** Inserts a new location to the table locations */
    public long insert(ContentValues contentValues){
        long rowID = mDB.insert(MY_DATABASE_TABLE, null, contentValues);
        return rowID;
    }
    public int updateuser(Userinformations informations) {
        SQLiteDatabase mdb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIELD_NAME, informations.getuserName());
        values.put(FIELD_AGE, informations.getuserage());
        values.put(FIELD_GENDRE, informations.getusergendre());
        values.put(FIELD_GUID, informations.getguid());
        values.put(FIELD_HEFZ, informations.gethefzi());
        values.put(FIELD_LAT, informations.getuserlat());
        values.put(FIELD_LNG, informations.getuserlng());
        values.put(FIELD_SPORTS, informations.getsports());
        values.put(FIELD_STUDIES, informations.getstudies());
        values.put(FIELD_VOLUNTEER, informations.getvolun());
        values.put(FIELD_NATIONALITY, informations.getnation());
        // updating row
        return mdb.update(MY_DATABASE_TABLE, values, FIELD_ROW_ID + " = ?",
                new String[] { String.valueOf(informations.getuserid()) });
    }
    /** Deletes all locations from the table */
    public int del(){
        int cnt = mDB.delete(MY_DATABASE_TABLE, null , null);
        return cnt;
    }

    /** Returns all the locations from the table */
    public Cursor getAllLocations(){
        return mDB.query(MY_DATABASE_TABLE, new String[] { FIELD_ROW_ID,FIELD_NAME,FIELD_AGE,FIELD_GENDRE,FIELD_GUID,FIELD_NATIONALITY,
                FIELD_STUDIES,FIELD_HEFZ,FIELD_SPORTS,FIELD_VOLUNTEER,FIELD_LNG,FIELD_LAT  } , null, null, null, null, null);
    }
    public Cursor getguid(String guid){
        this.mDB=getReadableDatabase();
        return mDB.query(MY_DATABASE_TABLE, new String[] { FIELD_ROW_ID,FIELD_NAME,FIELD_AGE,FIELD_GENDRE,FIELD_GUID,FIELD_NATIONALITY,
                FIELD_STUDIES,FIELD_HEFZ,FIELD_SPORTS,FIELD_VOLUNTEER,FIELD_LNG,FIELD_LAT  },FIELD_GUID+"like?",
                new String[] { guid }, null, null, null, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
