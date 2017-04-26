package pioneers.safwat.onecommunity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by safwa on 3/2/2017.
 */

public class UpdateData extends FragmentActivity implements AdapterView.OnItemSelectedListener, LocationListener,
        GoogleApiClient.ConnectionCallbacks,android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;
    CursorLoader cursorLoader;
    TextView guidview=null;
   // private String[] names;
   List<String> names=new ArrayList<String>();
    private String androidId ;
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private EditText usernameupd;
    private EditText ageupd;
    private Spinner genderupd;
    private Spinner hefzupd;
    private Spinner nationalityupd;
    private Spinner sportsupd;
    private Spinner voluntupd;
    private Spinner studyupd;
    private String mode , id;
    LocationsDB mLocationsDB;

    private String results;
    String SELECT_EMPLOYEE_WITH_EMPLOYER;
    String[]selectionArgs;
   private String myguid;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        androidId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d(TAG, "onCreate ...............................");
        getSupportLoaderManager().initLoader(0, null, this);
        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        setContentView(R.layout.update_location);
        usernameupd = (EditText) findViewById(R.id.usernameeupd);

        ageupd = (EditText) findViewById(R.id.ageeupd);
        genderupd = (Spinner) findViewById(R.id.Gendreupd);
        hefzupd = (Spinner) findViewById(R.id.Quranhefzupd);
        nationalityupd = (Spinner) findViewById(R.id.Nationalityupd);
        sportsupd = (Spinner) findViewById(R.id.Sportsupd);
        voluntupd = (Spinner) findViewById(R.id.Volunteerupd);
        studyupd = (Spinner) findViewById(R.id.Studiesupd);
        Button updatelocation = (Button) findViewById(R.id.updatelocation);
        Button launchmap=(Button)findViewById(R.id.launch_map_upd);
        guidview= (TextView) findViewById(R.id.guidview);
        genderupd.setOnItemSelectedListener(this);
        hefzupd.setOnItemSelectedListener(this);
        nationalityupd.setOnItemSelectedListener(this);
        sportsupd.setOnItemSelectedListener(this);
        voluntupd.setOnItemSelectedListener(this);
        studyupd.setOnItemSelectedListener(this);

        List<String> gendrelist = new ArrayList<String>();
        gendrelist.add("MALE");
        gendrelist.add("FEMALE");
        ArrayAdapter<String> gendredata = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, gendrelist);
        gendredata.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        genderupd.setAdapter(gendredata);
        genderupd.setOnItemSelectedListener(this);
        List<String> hefzlist = new ArrayList<String>();
        hefzlist.add("MORE THAN 10JUZ ");
        hefzlist.add("MORE THAN 20JUZ ");
        hefzlist.add("ALL MEMORIZED ");
        ArrayAdapter<String> hefzdata = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, hefzlist);
        hefzdata.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        hefzupd.setAdapter(hefzdata);
        hefzupd.setOnItemSelectedListener(this);
        List<String> sportslist = new ArrayList<String>();
        sportslist.add("FOOTBALL");
        sportslist.add("BASKET BALL");
        sportslist.add("SWIMMING");
        sportslist.add("MARSHAL ARTS");
        sportslist.add("OTHERS");
        ArrayAdapter<String> sportsdata = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, sportslist);
        sportsdata.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sportsupd.setAdapter(sportsdata);
        sportsupd.setOnItemSelectedListener(this);
        List<String> voluntlist = new ArrayList<String>();
        voluntlist.add("YES");
        voluntlist.add("NO");
        ArrayAdapter<String> voluntdata = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, voluntlist);
        voluntdata.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        voluntupd.setAdapter(voluntdata);
        voluntupd.setOnItemSelectedListener(this);
        List<String> studylist = new ArrayList<String>();
        studylist.add("ISLAMIC STUDIES");
        studylist.add("SCIENCE STUDIES");
        studylist.add("LANGUAGES STUDIES");
        studylist.add("ENGINEERING STUDIES");
        studylist.add("IT STUDIES");
        studylist.add("ACCOUNTS STUDIES");
        ArrayAdapter<String> studydata = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, studylist);
        studydata.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        studyupd.setAdapter(studydata);
        studyupd.setOnItemSelectedListener(this);

        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String country;
        for (Locale loc : locale) {
            country = loc.getDisplayCountry();
            if (country.length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter<String> nationdata = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, countries);
        nationdata.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        nationalityupd.setAdapter(nationdata);

        launchmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(UpdateData.this,MainActivity.class));
               /* if (myguid.equalsIgnoreCase(androidId))
                    Toast.makeText(getBaseContext(),"GUID not allowed: " + guidview.getText().toString(), Toast.LENGTH_SHORT).show();
                else
                {
                    String lat = String.valueOf(mCurrentLocation.getLatitude());
                    String lng = String.valueOf(mCurrentLocation.getLongitude());
// Creating an instance of ContentValues
                    ContentValues contentValues = new ContentValues();

                    // Setting latitude in ContentValues
                    contentValues.put(LocationsDB.FIELD_LAT, lat);

                    // Setting longitude in ContentValues
                    contentValues.put(LocationsDB.FIELD_LNG, lng);
                    Uri uri = Uri.parse(Myprovider.CONTENT_URI + "/" + LocationsDB.FIELD_ROW_ID);
                    getContentResolver().update(uri,contentValues,null,null);
                }*/
            }
        });
    }
    public void startUpdate(View view) {
        String lat = String.valueOf(mCurrentLocation.getLatitude());
        String lng = String.valueOf(mCurrentLocation.getLongitude());
// Creating an instance of ContentValues
        ContentValues contentValues = new ContentValues();

        // Setting latitude in ContentValues
        contentValues.put(LocationsDB.FIELD_LAT, lat);

        // Setting longitude in ContentValues
        contentValues.put(LocationsDB.FIELD_LNG, lng);

             /*  contentValues.put(LocationsDB.FIELD_AGE,ageupd.getText().toString());
                contentValues.put(LocationsDB.FIELD_NAME,usernameupd.getText().toString());
                contentValues.put(LocationsDB.FIELD_GENDRE,genderupd.getSelectedItem().toString());
                contentValues.put(LocationsDB.FIELD_SPORTS,sportsupd.getSelectedItem().toString());
                contentValues.put(LocationsDB.FIELD_GUID,uniqueID.toString());
                contentValues.put(LocationsDB.FIELD_STUDIES,studyupd.getSelectedItem().toString());
                contentValues.put(LocationsDB.FIELD_NATIONALITY, nationalityupd.getSelectedItem().toString());
                contentValues.put(LocationsDB.FIELD_HEFZ,hefzupd.getSelectedItem().toString());
                contentValues.put(LocationsDB.FIELD_VOLUNTEER,voluntupd.getSelectedItem().toString());*/
        // long rowID = mLocationsDB.insert(contentValues);
        // Uri uri = Myprovider.CONTENT_URI;
        Uri uri = Uri.parse(Myprovider.CONTENT_URI + "/" +Integer.parseInt( myguid));
        getContentResolver().update(uri,contentValues,null,null);
        Toast.makeText(getBaseContext(),
                "Provider: " + myguid, Toast.LENGTH_SHORT).show();
        //getSupportLoaderManager().initLoader(0, null, this);

    }
    public void startUpdatedata(View view) {
        String lat = String.valueOf(mCurrentLocation.getLatitude());
        String lng = String.valueOf(mCurrentLocation.getLongitude());
// Creating an instance of ContentValues
        ContentValues contentValues = new ContentValues();

        // Setting latitude in ContentValues
        //   contentValues.put(LocationsDB.FIELD_LAT, lat);

        // Setting longitude in ContentValues
        // contentValues.put(LocationsDB.FIELD_LNG, lng);

        contentValues.put(LocationsDB.FIELD_AGE,ageupd.getText().toString());
        contentValues.put(LocationsDB.FIELD_NAME,usernameupd.getText().toString());
        contentValues.put(LocationsDB.FIELD_GENDRE,genderupd.getSelectedItem().toString());
        contentValues.put(LocationsDB.FIELD_SPORTS,sportsupd.getSelectedItem().toString());
        contentValues.put(LocationsDB.FIELD_GUID,androidId.toString());
        contentValues.put(LocationsDB.FIELD_STUDIES,studyupd.getSelectedItem().toString());
        contentValues.put(LocationsDB.FIELD_NATIONALITY, nationalityupd.getSelectedItem().toString());
        contentValues.put(LocationsDB.FIELD_HEFZ,hefzupd.getSelectedItem().toString());
        contentValues.put(LocationsDB.FIELD_VOLUNTEER,voluntupd.getSelectedItem().toString());
        // long rowID = mLocationsDB.insert(contentValues);
        // Uri uri = Myprovider.CONTENT_URI;
        Uri uri = Uri.parse(Myprovider.CONTENT_URI + "/" +LocationsDB.FIELD_ROW_ID);
        getContentResolver().update(uri,contentValues,null,null);
        Toast.makeText(getBaseContext(),
                "Provider: " + myguid, Toast.LENGTH_SHORT).show();
        //getSupportLoaderManager().initLoader(0, null, this);

    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri uri = Myprovider.CONTENT_URI;
        cursorLoader= new CursorLoader(this, uri, null, null, null, null);
        return cursorLoader;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
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
            LatLng location = new LatLng(lat, lng);
            // Drawing the marker in the Google Maps
            //  drawMarker(location);
            // Traverse the pointer to the next row
         //  if(guid==uniqueID)
            if(guid.equalsIgnoreCase(androidId))
              //  if(distance(lat, lng, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()) < 2.0)
             //   names.add(name);
            myguid=arg1.getString(arg1.getColumnIndex(LocationsDB.FIELD_ROW_ID));
            arg1.moveToNext();
        }
      //  guidview.setText(names.size());
       // guidview.getText(names);
       /* for(int i=0; i < names.size(); i++){

            guidview.setText(guidview.getText() + names.get(i) + " , ");
        }*/
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

    private class LocationupdateTask extends AsyncTask<ContentValues, Void, Void> {
        @Override
        protected Void doInBackground(ContentValues... contentValues) {

            /** Setting up values to insert the clicked location into SQLite database */
            Uri uri = Myprovider.CONTENT_URI;
            getContentResolver().update(uri,contentValues[0],null,null);
            return null;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.Gendre:
                //        String genderitem=parent.getItemAtPosition(position).toString();
                break;
            case R.id.agee:
                //        String ageeitem=parent.getItemAtPosition(position).toString();
                break;
            case R.id.Quranhefz:
                //         String hefzitem=parent.getItemAtPosition(position).toString();
                break;
            case R.id.Nationality:
                //         String nationitem=parent.getItemAtPosition(position).toString();
                break;
            case R.id.Sports:
                //        String sportitem=parent.getItemAtPosition(position).toString();
                break;
            case R.id.Volunteer:
                //          String voluntitem=parent.getItemAtPosition(position).toString();
                break;
            case R.id.Studies:
                //        String studitem=parent.getItemAtPosition(position).toString();
                break;
            default:
                break;
        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

 public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.MAPS_RECEIVE)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.MAPS_RECEIVE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.MAPS_RECEIVE},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.MAPS_RECEIVE},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    private void updateUI() {
        Log.d(TAG, "UI update initiated .............");
        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());
           /* tvLocation.setText("At Time: " + mLastUpdateTime + "\n" +
                    "Latitude: " + lat + "\n" +
                    "Longitude: " + lng + "\n" +
                    "Accuracy: " + mCurrentLocation.getAccuracy() + "\n" +
                    "Provider: " + mCurrentLocation.getProvider());*/
        } else {
            Log.d(TAG, "location is null ...............");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
    }
    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * c;

        return dist; // output distance, in MILES
    }
}