package pioneers.safwat.onecommunity;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>,GoogleMap.OnInfoWindowClickListener,GoogleMap.OnMarkerClickListener, OnMapReadyCallback, com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

   private GoogleMap mMap;
    TextView tvLocInfo;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    private ArrayList<LatLng> points; //added
    private Polyline line; //added
    private static final String TAG = "LocationActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        points = new ArrayList<LatLng>();
        mapFragment.getMapAsync(this);

    }
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
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

    private void drawMarker(LatLng point) {
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
        markerOptions.position(point);


        // Adding marker on the Google Map
        mMap.addMarker(markerOptions);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        //or myMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setTrafficEnabled(true);

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
        googleMap.setMyLocationEnabled(true);
        getSupportLoaderManager().initLoader(0, null, this);
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {
                // Removing all markers from the Google Map
                googleMap.clear();
                // Creating an instance of LocationDeleteTask
                LocationDeleteTask deleteTask = new LocationDeleteTask();
                // Deleting all the rows from SQLite database table
                deleteTask.execute();
                Toast.makeText(getBaseContext(), "All markers are removed", Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onInfoWindowClick(Marker marker) {
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();
        setContentView(R.layout.custom_info_contents);


        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            tvLocInfo = (TextView)findViewById(R.id.title);
          tvLocInfo.setText("New marker added@" + marker.getTitle());
        }
        return false;
    }

    @Override
    public void onConnected( Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.MAPS_RECEIVE)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
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

    }

    private class LocationDeleteTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            /** Deleting all the locations stored in SQLite database */
            getContentResolver().delete(Myprovider.CONTENT_URI, null, null);
            return null;
        }
    }


 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/
    @Override
    public Loader<Cursor> onCreateLoader(int arg0,Bundle arg1) {

        // Uri to the content provider LocationsContentProvider
        Uri uri = Myprovider.CONTENT_URI;
        // Fetches all the rows from locations table
        return new CursorLoader(this, uri, null, null, null, null);
    }
    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
        int locationCount = 0;
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
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
          //  markerOptions.snippet("Ahmed");
         //  markerOptions.title(age+"\n"+gendre+"\n"+nation+"\n"+hefz+"\n"+sports+"\n"+volunteer);
            markerOptions.title("Name:"+name+"\n"+"Age:"+age+"\n"+"Gender:"+gendre+"\n"+"Nationality:"+nation+"\n"+
                    "Studies:"+study+"\n"+"Quran:"+hefz+"\n"+"Sports:"+sports+"\n"+"Volunteer Work:"+guid);
            markerOptions.position(location);
            mCurrLocationMarker = mMap.addMarker(markerOptions);
         //   mMap.setOnInfoWindowClickListener(this);
        //    setContentView(R.layout.custom_info_contents);
         //   tvLocInfo = (TextView)findViewById(R.id.title);
       //     tvLocInfo.setText(name);
            points.add(location);
          //  redrawLine();

            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter(){
                // Use default InfoWindow frame
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                // Defines the contents of the InfoWindow
                @Override
                public View getInfoContents(Marker marker) {

                    // Getting view from the layout file info_window_layout
                    View v = getLayoutInflater().inflate(R.layout.custom_info_contents, null);

                    // Getting reference to the TextView to set title
                    TextView note = (TextView) v.findViewById(R.id.textstudies);
                    note.setText(marker.getTitle() );

                    return v;
                }
            });
            arg1.moveToNext();
        }
        if(locationCount>0){
            // Moving CameraPosition to last clicked position
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,lng)));

            // Setting the zoom level in the map on last position  is clicked
            //     mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.action_nation:


                break;
            case R.id.action_gender:


                break;

        }
        return true;


    }
    private void redrawLine() {

        mMap.clear();  //clears all Markers and Polylines

        PolylineOptions options = new PolylineOptions().width(10).color(Color.CYAN).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
        }
      //  addMarker(); //add Marker in current position
        line= mMap.addPolyline(options); //add Polyline
    }
}