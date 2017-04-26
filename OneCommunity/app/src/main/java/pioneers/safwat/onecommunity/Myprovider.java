package pioneers.safwat.onecommunity;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.sql.SQLException;

import static pioneers.safwat.onecommunity.LocationsDB.MY_DATABASE_TABLE;

/**
 * Created by safwa on 3/5/2017.
 */

public class Myprovider extends ContentProvider {
    public static final String PROVIDER_NAME = "pioneers.safwat.onecommunity.Myprovider";

    /** A uri to do operations on locations table. A content provider is identified by its uri */
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/mlocations" );

    /** Constant to identify the requested operation */
    private static final int LOCATIONS = 1;

    private static final UriMatcher uriMatcher ;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "mlocations", LOCATIONS);
    }

    /** This content provider does the database operations by this object */
    LocationsDB mLocationsDB;

    /** A callback method which is invoked when the content provider is starting up */
    @Override
    public boolean onCreate() {
        mLocationsDB = new LocationsDB(getContext());
        return true;
    }

    /** A callback method which is invoked when insert operation is requested on this content provider */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = mLocationsDB.insert(values);
        Uri _uri=null;
        if(rowID>0){
            _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
        }else {
            try {
                throw new SQLException("Failed to insert : " + uri);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return _uri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        SQLiteDatabase db = mLocationsDB.getWritableDatabase();
     /*   String iid = uri.getPathSegments().get(1);
        selection = LocationsDB.FIELD_ROW_ID + "=" + iid
                + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");*/
     //   int count = 0;
           //   count = LocationsDB.mDB.update(LocationsDB.MY_DATABASE_TABLE, values, selection, selectionArgs);

      int  count = db.update(MY_DATABASE_TABLE, values,
             selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;

    }

    /** A callback method which is invoked when delete operation is requested on this content provider */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int cnt = 0;
        cnt = mLocationsDB.del();
        return cnt;
    }

    /** A callback method which is invoked by default content uri */
    @Override

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        if(uriMatcher.match(uri)==LOCATIONS){
            return mLocationsDB.getAllLocations();

            //  return mLocationsDB.getphonelocations(phonee);
        }
       // return mLocationsDB.getguid(selection);
        return null;

     /*   SQLiteDatabase db =  mLocationsDB.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(LocationsDB.MY_DATABASE_TABLE);
        String id = uri.getPathSegments().get(1);
        queryBuilder.appendWhere(LocationsDB.FIELD_ROW_ID + "=" + id);
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        return cursor;*/

    }


    @Override
    public String getType(Uri uri) {


        return null;
    }
}
