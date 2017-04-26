package pioneers.safwat.onecommunity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by safwa on 4/14/2017.
 */

public class Start extends FragmentActivity implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    private String myguid=null;
    private int rowId;
    double lat=0;
    double lng=0;
    String name;
    String age;
    String gendre;
    String guid;
    String nation;
    String study;
    String hefz;
    String sports;
    String volunteer;
    CursorLoader cursorLoader;
    private String androidId ;
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        androidId = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        Button newuser;
        Button updatelocation;
        Button launchmap;
        Button chatmessage;
      //  ArrayList<String> points; //added
      //  String  uniqueID = UUID.randomUUID().toString();
        setContentView(R.layout.start);
        newuser = (Button) findViewById(R.id.newuser);
       updatelocation = (Button) findViewById(R.id.updloc);
       launchmap = (Button) findViewById(R.id.launch);
        chatmessage = (Button) findViewById(R.id.upduserdata);
        getSupportLoaderManager().initLoader(0, null, this);
        newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(Start.this, pioneers.safwat.onecommunity.Authentic.class));
               // startActivity(new Intent(Start.this, pioneers.safwat.onecommunity.UserData.class));
                if (androidId.equalsIgnoreCase(myguid))
                {Toast.makeText(getBaseContext(),"YOU ARE EXISTING USER " , Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Start.this, pioneers.safwat.onecommunity.MainActivity.class));}
                else
                startActivity(new Intent(Start.this, pioneers.safwat.onecommunity.UserData.class));
            }

        });
       launchmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Start.this, pioneers.safwat.onecommunity.MainActivity.class));

            }
        });
        updatelocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (androidId.equalsIgnoreCase(myguid))
                    startActivity(new Intent(Start.this, pioneers.safwat.onecommunity.UpdateData.class));
                else
                {Toast.makeText(getBaseContext(),"Fill Your Profile First " , Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Start.this, pioneers.safwat.onecommunity.UpdateData.class));}

            }
        });
        chatmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Start.this, pioneers.safwat.onecommunity.Authentic.class));
            }
        });
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Myprovider.CONTENT_URI;
        cursorLoader= new CursorLoader(this, uri, null, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor arg1) {
        int locationCount = 0;

        // Number of locations available in the SQLite database table
        locationCount = arg1.getCount();
        // Move the current record pointer to the first row of the table
        arg1.moveToFirst();
        for(int i=0;i<locationCount;i++){
            name=arg1.getString(arg1.getColumnIndex(LocationsDB.FIELD_NAME));
            age=arg1.getString(arg1.getColumnIndex(LocationsDB.FIELD_AGE));
            gendre=arg1.getString(arg1.getColumnIndex(LocationsDB.FIELD_GENDRE));
            guid=arg1.getString(arg1.getColumnIndex(LocationsDB.FIELD_GUID));
            nation=arg1.getString(arg1.getColumnIndex(LocationsDB.FIELD_NATIONALITY));
            study=arg1.getString(arg1.getColumnIndex(LocationsDB.FIELD_STUDIES));
            hefz=arg1.getString(arg1.getColumnIndex(LocationsDB.FIELD_HEFZ));
            sports=arg1.getString(arg1.getColumnIndex(LocationsDB.FIELD_SPORTS));
            volunteer=arg1.getString(arg1.getColumnIndex(LocationsDB.FIELD_VOLUNTEER));
            lng = arg1.getDouble(arg1.getColumnIndex(LocationsDB.FIELD_LNG));
            lat = arg1.getDouble(arg1.getColumnIndex(LocationsDB.FIELD_LAT));
            if(guid.equalsIgnoreCase(androidId))
                //  if(distance(lat, lng, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()) < 2.0)
            myguid=guid;
            arg1.moveToNext();
        }
        if(locationCount>0){
            // Moving CameraPosition to last clicked position
            // mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,lng)));

            // Setting the zoom level in the map on last position  is clicked
            //     mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
