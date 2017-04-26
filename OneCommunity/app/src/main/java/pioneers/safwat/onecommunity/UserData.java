package pioneers.safwat.onecommunity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by safwa on 3/2/2017.
 */

public class UserData extends Activity implements AdapterView.OnItemSelectedListener, LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;
    LocationsDB mlocations;
  //  String androidId = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
    private String androidId;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private EditText username;
    private EditText age;
    private Spinner gender;
    private Spinner hefz;
    private Spinner nationality;
    private Spinner sports;
    private Spinner volunt;
    private Spinner study;
    String guid;
  //  SharedPreferences userprefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       androidId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d("Android","Android ID : "+androidId);
        Log.d(TAG, "onCreate ...............................");
      //  userprefs= PreferenceManager.getDefaultSharedPreferences(this);

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
        setContentView(R.layout.user_data);
        username = (EditText) findViewById(R.id.usernamee);
        age = (EditText) findViewById(R.id.agee);
        gender = (Spinner) findViewById(R.id.Gendre);
        hefz = (Spinner) findViewById(R.id.Quranhefz);
        nationality = (Spinner) findViewById(R.id.Nationality);
        sports = (Spinner) findViewById(R.id.Sports);
        volunt = (Spinner) findViewById(R.id.Volunteer);
        study = (Spinner) findViewById(R.id.Studies);
        Button adddata = (Button) findViewById(R.id.add_user);
Button showmap=(Button)findViewById(R.id.map_user);

        gender.setOnItemSelectedListener(this);
        hefz.setOnItemSelectedListener(this);
        nationality.setOnItemSelectedListener(this);
        sports.setOnItemSelectedListener(this);
        volunt.setOnItemSelectedListener(this);
        study.setOnItemSelectedListener(this);

        List<String> gendrelist = new ArrayList<String>();
        gendrelist.add("MALE");
        gendrelist.add("FEMALE");
        ArrayAdapter<String> gendredata = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, gendrelist);
        gendredata.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        gender.setAdapter(gendredata);
        gender.setOnItemSelectedListener(this);
        List<String> hefzlist = new ArrayList<String>();
        hefzlist.add("MORE THAN 10JUZ ");
        hefzlist.add("MORE THAN 20JUZ ");
        hefzlist.add("ALL MEMORIZED ");
        ArrayAdapter<String> hefzdata = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, hefzlist);
        hefzdata.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        hefz.setAdapter(hefzdata);
        hefz.setOnItemSelectedListener(this);
        List<String> sportslist = new ArrayList<String>();
        sportslist.add("FOOTBALL");
        sportslist.add("BASKET BALL");
        sportslist.add("SWIMMING");
        sportslist.add("MARSHAL ARTS");
        sportslist.add("OTHERS");
        ArrayAdapter<String> sportsdata = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, sportslist);
        sportsdata.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sports.setAdapter(sportsdata);
        sports.setOnItemSelectedListener(this);
        List<String> voluntlist = new ArrayList<String>();
        voluntlist.add("YES");
        voluntlist.add("NO");
        ArrayAdapter<String> voluntdata = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, voluntlist);
        voluntdata.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        volunt.setAdapter(voluntdata);
        volunt.setOnItemSelectedListener(this);
        List<String> studylist = new ArrayList<String>();
        studylist.add("ISLAMIC STUDIES");
        studylist.add("SCIENCE STUDIES");
        studylist.add("LANGUAGES STUDIES");
        studylist.add("ENGINEERING STUDIES");
        studylist.add("IT STUDIES");
        studylist.add("ACCOUNTS STUDIES");
        ArrayAdapter<String> studydata = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, studylist);
        studydata.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        study.setAdapter(studydata);
        study.setOnItemSelectedListener(this);

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
        nationality.setAdapter(nationdata);

        adddata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lat = String.valueOf(mCurrentLocation.getLatitude());
                String lng = String.valueOf(mCurrentLocation.getLongitude());
// Creating an instance of ContentValues
                ContentValues contentValues = new ContentValues();

                // Setting latitude in ContentValues
                contentValues.put(LocationsDB.FIELD_LAT, lat);

                // Setting longitude in ContentValues
                contentValues.put(LocationsDB.FIELD_LNG, lng);

                // Setting zoom in ContentValues
                contentValues.put(LocationsDB.FIELD_AGE,age.getText().toString());
                contentValues.put(LocationsDB.FIELD_NAME,username.getText().toString());
                contentValues.put(LocationsDB.FIELD_GENDRE,gender.getSelectedItem().toString());
                contentValues.put(LocationsDB.FIELD_SPORTS,sports.getSelectedItem().toString());
                contentValues.put(LocationsDB.FIELD_GUID,androidId.toString());
                contentValues.put(LocationsDB.FIELD_STUDIES,study.getSelectedItem().toString());
                contentValues.put(LocationsDB.FIELD_NATIONALITY, nationality.getSelectedItem().toString());
                contentValues.put(LocationsDB.FIELD_HEFZ,hefz.getSelectedItem().toString());
                contentValues.put(LocationsDB.FIELD_VOLUNTEER,volunt.getSelectedItem().toString());
                // Creating an instance of LocationInsertTask
                LocationInsertTask insertTask = new LocationInsertTask();
                // Storing the latitude, longitude and zoom level to SQLite database
              /*  if (uniqueID==guid)
                {   Toast.makeText(getBaseContext(),"GUID: " + uniqueID + "\n" +

                            "same user can't add another location " , Toast.LENGTH_SHORT).show();}
                else*/

                insertTask.execute(contentValues);
              //  long rowID = mlocations.insert(contentValues);
                Toast.makeText(getBaseContext(),"GUID: " + androidId + "\n" +
                        "Latitude: " + lat + "\n" +
                        "Longitude: " + lng + "\n" +
                        "Accuracy: " + mCurrentLocation.getAccuracy() + "\n" +
                        "Provider: " + mCurrentLocation.getProvider(), Toast.LENGTH_SHORT).show();
            }
        });
        showmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserData.this,pioneers.safwat.onecommunity.MainActivity.class));

            }
        });


    }


    private class LocationInsertTask extends AsyncTask<ContentValues, Void, Void> {
        @Override
        protected Void doInBackground(ContentValues... contentValues) {

            /** Setting up values to insert the clicked location into SQLite database */
            getContentResolver().insert(Myprovider.CONTENT_URI, contentValues[0]);
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

}