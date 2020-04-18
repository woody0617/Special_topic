package com.example.user.my_app_ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class page5 extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    private Button btn_loc_y,btn_loc_n;
    private TextView tv_l,t_loc,txttime,pathbun;
    private LocationManager locationManager;
    private String lat,lng ="no value";
    private Banner banner;
    //tv_l>>
    String timea ="";//時間
    String address = "";//地址
    String f_locat = "";//經緯度
    public  class GlideImageLoader1 extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page5);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        tv_l = findViewById(R.id.t_ll);
        t_loc = findViewById(R.id.t_loc);
        banner =findViewById(R.id.banner);
        txttime = findViewById(R.id.textView37);
        pathbun = findViewById(R.id.textView38);
        btn_loc_n = findViewById(R.id.btn_loc_n);
        btn_loc_y = findViewById(R.id.btn_loc_y);
        rebun();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }

        new GetCoordinates().execute( tv_l.getText().toString().replace(" ","+"));

        btn_loc_y.setOnClickListener(YES);
        btn_loc_n.setOnClickListener(NO);
        nowtime();

    }
    //圖片輪播
    private void rebun(){
        Bundle pathad = getIntent().getExtras();
        String path = pathad.getString("path");
        //Log.d("MSG5", path);
        String[] array = path.split("/");

        //pathbun.setText(String.valueOf(array.length));
        banner.setImageLoader(new GlideImageLoader1());
        List<String> images = new ArrayList<>();
        for(int i = 3; i < array.length; i++){
            images.add("http://120.96.63.47/docker/third_who_dog/"+array[1]+"/tf_files/lose/"+array[2]+"/"+array[i]);
        }
        banner.setImages(images).setDelayTime(4000);
        banner.start();

    }


    //{btn}按NO
    private Button.OnClickListener NO = new Button.OnClickListener() {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(page5.this , page4.class);
            startActivity(intent);
            page5.this.finish();
        }
    };
    //{btn}按YES
    private Button.OnClickListener YES = new Button.OnClickListener() {

        public String murl = "http://120.96.63.47/dogsql/lost/lostuplod.php";
        @Override
        public void onClick(View view) {
            try {
                Bundle pathad = getIntent().getExtras();
                String path = pathad.getString("path");
                Log.d("MSG5", path);
                String[] array = path.split("/");
                Log.d("MSG6", array[2]);
                String[] a = array[2].split("d");
                Log.d("MSG7", a[0]);
                Log.d("MSG8", a[1]);
                String[] b = a[1].split("_");


                HttpClient client = new DefaultHttpClient();
                URI website = new URI(this.murl);
                HttpPost request = new HttpPost();
                List params = new ArrayList();
                params.add(new BasicNameValuePair("did", b[0]));
                params.add(new BasicNameValuePair("pid", b[1]));
                params.add(new BasicNameValuePair("mytime", timea));
                params.add(new BasicNameValuePair("myad", address));
                params.add(new BasicNameValuePair("mylocat", f_locat));

                UrlEncodedFormEntity env = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                request.setURI(website);
                request.setEntity(env);
                HttpResponse response = client.execute(request);
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    String msg = EntityUtils.toString(resEntity);
                    Toast.makeText(page5.this, msg.toString(),Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.setClass(page5.this,MainActivity.class);
                    startActivity(intent);
                    page5.this.finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

        //目前時間
    public void nowtime(){
        Calendar c = Calendar.getInstance();
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH)+1);
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String time = "拍照時間 : "+year+" 年 "+month+" 月 "+day+" 日 "+hour+" 時 ";
        txttime.setText(time.toString());
        timea = year+"/"+month+"/"+day+"/"+hour;

    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(page5.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (page5.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(page5.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            //GPS定址
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //被動定位
            Location location1 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            //僅限裝置(偏省電)
            Location location2 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Log.d("NET", String.valueOf(location));
            Log.d("NET", String.valueOf(location1));
            Log.d("NET", String.valueOf(location2));
            if (location != null) {

                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lat = String.valueOf(latti);
                lng = String.valueOf(longi);

                tv_l.setText(lat + ","+ lng);
                f_locat = String.valueOf(lat + ","+ lng);
            } else  if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lat = String.valueOf(latti);
                lng = String.valueOf(longi);

                tv_l.setText(lat + ","+ lng);
                f_locat = String.valueOf(lat + ","+ lng);


            } else  if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lat = String.valueOf(latti);
                lng = String.valueOf(longi);

                tv_l.setText(lat + ","+ lng);
                f_locat = String.valueOf(lat + ","+ lng);


            }else{

                Toast.makeText(this,"抓不到你的位置",Toast.LENGTH_SHORT).show();

            }
        }
    }
    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("請開你的GPS定位")
                .setCancelable(false)
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    public class HttpConnection {
        public HttpConnection() {

        }

        public String getHTTPData(String requestURL)
        {
            URL url;
            String response = "";
            try{
                url = new URL(requestURL);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(1500);
                conn.setConnectTimeout(1500);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

                int responseCode = conn.getResponseCode();
                if(responseCode == HttpsURLConnection.HTTP_OK)
                {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while((line = br.readLine()) != null)
                        response+=line;
                }
                else
                    response = "";

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
    }
    private class GetCoordinates extends AsyncTask<String,Void,String> {
        ProgressDialog dialog = new ProgressDialog(page5.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("請稍後....");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }



        @Override
        protected String doInBackground(String... strings) {
            String response;
            try{
                String latlng = strings[0];

                HttpConnection http = new HttpConnection();
//                String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s",address);
                String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?latlng=%s&language=zh-TW&CA&key=AIzaSyBuIAr171HUkpYR296sAtC8fhq0dB39A_M",latlng);
                Log.d("GEM",url);

                response = http.getHTTPData(url);
                Log.d("GEM", latlng);
                Log.d("GEM",response + "/n");
                return response;
            }
            catch (Exception ex)
            {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject jsonObject = new JSONObject(s);
                String loc = ((JSONArray)jsonObject.get("results")).getJSONObject(0).get("formatted_address").toString();
                Log.d("GEM", loc);
                t_loc.setText("拍照地點 : "+String.format(" %s",loc));
                address = String.format(" %s",loc);
                if(dialog.isShowing())
                    dialog.dismiss();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
