package com.yjn5031.studybuddy;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.HashMap;
import java.util.List;


public class CoffeeFragment extends Fragment {


    //makes the local variable to make refeence to the layout and the database
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;
    GoogleMap map;
    double currentLat = 0;
    double currentLon = 0;

    //creats the view and gets the layout componets to assigns it to the varble from the top
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_coffee, container, false);

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity().getApplicationContext());

        //checks the user persion to see if the user gave the perssison to find its currewt location
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //when permission is granted
            //call method
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


//        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(@NonNull GoogleMap googleMap) {
//                //when map is loaded
//                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                    @Override
//                    public void onMapClick(@NonNull LatLng latLng) {
//                        MarkerOptions markerOptions = new MarkerOptions();
//                        markerOptions.position(latLng);
//                        markerOptions.title(latLng.latitude + " : "+latLng.longitude);
//                        googleMap.clear();
//                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
//                        googleMap.addMarker(markerOptions);
//                    }
//                });
//
//            }
//        });

        return view;
    }

    // set up the api request to the the cafe near my location
    private void getCoffeeShopData(){
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"+
                "?location="+currentLat+","+currentLon +
                "&radius=5000"+
                "&types=cafe"+
                "&sensor=true"+
                "&key="+getResources().getString(R.string.google_map_key);

        new PlaceTask().execute(url);
    }


    //gets the uswer current location
    private void getCurrentLocation() {
        //inlizlize task locatior
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //gets the users lcoation and gets the nearewsrt coffe by and makes the makrer on the map view
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //when successful
                if(location != null){
                    currentLat = location.getLatitude();
                    currentLon = location.getLongitude();
                    //sync map
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            map = googleMap;

                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(currentLat, currentLon), 15
                            ));
                            getCoffeeShopData();

                        }
                    });
                }
            }
        });
    }

    //to get the user persion we had to overrid this method
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 44){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }

    //makes a task to get all the infoeramtion about the nearest coffee shop
    private class PlaceTask extends AsyncTask<String, Integer, String> {

        //does the task in the background thread and not the main thread
        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            new ParserTask().execute(s);
        }
    }


    //makes the downlaoded url and setup a connect to get the data from and api endpoint
    private String downloadUrl(String string) throws IOException {
        URL url = new URL(string);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();

        InputStream stream = connection.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        StringBuilder builder = new StringBuilder();

        String line = "";
        while((line = reader.readLine()) != null){
            builder.append(line);
        }

        String data = builder.toString();
        reader.close();
        return data;


    }

    // makes a thread to parse the json and put into a list
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>> {

        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            JsonParser jsonParser = new JsonParser();

            List<HashMap<String, String>> mapList = null;
            JSONObject object = null;

            try {
                object  = new JSONObject(strings[0]);
                mapList = jsonParser.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return mapList;

        }

        //after the excuation is completed the data is paresed and get  the lat and lng of the coffee shop
        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            map.clear();

            for(int i=0; i<hashMaps.size(); i++){
                HashMap<String, String> hashMapList = hashMaps.get(i);
                double lat = Double.parseDouble(hashMapList.get("lat"));
                double lng = Double.parseDouble(hashMapList.get("lng"));

                String name = hashMapList.get("name");
                LatLng latLng = new LatLng(lat, lng);

                MarkerOptions options = new MarkerOptions();

                options.title(name);
                options.position(latLng);
                map.addMarker(options);

            }
        }
    }

}