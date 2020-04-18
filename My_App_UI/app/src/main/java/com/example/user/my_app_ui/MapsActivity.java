package com.example.user.my_app_ui;

import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private static final String lost = "lost_dog";

    private double my_lat;
    private double my_lng;

    private Marker mfind;
    private Marker mlost;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });

    }
    private void bundletest(){
        //String murl = "http://192.168.1.104/dogsql/lost/lost_select_mysql.php";
        String murl = "http://120.96.63.47/dogsql/lost/test_ad.php";
        try {
            Log.d("test","test");
            Bundle bundle = getIntent().getExtras();
            String pid = bundle.getString("p_id");
            String did = bundle.getString("d_id");

            HttpClient client = new DefaultHttpClient();
            URI website = new URI(murl);
            HttpPost request = new HttpPost();
            List params = new ArrayList();
            params.add(new BasicNameValuePair("pid", pid));
            params.add(new BasicNameValuePair("did", did));
            UrlEncodedFormEntity env = new UrlEncodedFormEntity (params, HTTP.UTF_8);

            request.setURI(website);
            request.setEntity(env);
            HttpResponse response = client.execute(request);
            HttpEntity resEntity = response.getEntity();
            InputStream is = resEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            Log.d("test2",sb.toString());
            is.close();
            String result = sb.toString();
            String data1="";
            String data5="";
            int i = 0;
            JSONArray jArray = new JSONArray(result);
            while(i<jArray.length()){
                JSONObject json_data = jArray.getJSONObject(i);
                data1 =json_data.getString("l_location");
                data5 =json_data.getString("f_location");
                i++;
            }
            String[] temp = data1.toString().split(",");
            Double tempa = Double.valueOf(temp[0]).doubleValue(),tempb=Double.valueOf(temp[1]).doubleValue();
            mlost = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(tempa,tempb))
                    .title("遺失地點")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            mlost.setTag(0);

            String[] f_temp = data5.toString().split(",");
            Double f_tempa = Double.valueOf(f_temp[0]).doubleValue(),f_tempb=Double.valueOf(f_temp[1]).doubleValue();
            mfind = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(f_tempa,f_tempb))
                    .title("發現地點")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


            mfind.setTag(1);

            Log.d("find", String.valueOf(f_tempa));
            Log.d("find", String.valueOf(f_tempb));

//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(tempa,tempb), 15));
            markStartingLocationOnMap(mMap, new LatLng(tempa, tempb));
            addCameraToMap(new LatLng(tempa, tempb));
//            Toast.makeText(MapsActivity.this,data1.toString(),Toast.LENGTH_LONG).show();
//            return;


        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addCameraToMap(LatLng latLng){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(13)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void markStartingLocationOnMap(GoogleMap mapObject, LatLng location){
        mapObject.addMarker(new MarkerOptions().position(location).title("遺失地點"));
        mapObject.moveCamera(CameraUpdateFactory.newLatLng(location));
    }



//    private List<LatLng> decodePoly(String encoded) {
//        List<LatLng> poly = new ArrayList<LatLng>();
//        int index = 0, len = encoded.length();
//        int lat = 0, lng = 0;
//
//        while (index < len) {
//            int b, shift = 0, result = 0;
//            do {
//                b = encoded.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lat += dlat;
//
//            shift = 0;
//            result = 0;
//            do {
//                b = encoded.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lng += dlng;
//
//            LatLng p = new LatLng( (((double) lat / 1E5)),
//                    (((double) lng / 1E5) ));
//            poly.add(p);
//        }
//
//        return poly;
//    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Add some markers to the map, and add a data object to each marker.
//        mlost = mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(Double.valueOf(hashMap.get(TAG_LAT)) , Double.valueOf(hashMap.get(TAG_LNG)))
//                .title("lost_1"));
//        mlost.setTag(0);
        bundletest();

        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(this,
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }
}

