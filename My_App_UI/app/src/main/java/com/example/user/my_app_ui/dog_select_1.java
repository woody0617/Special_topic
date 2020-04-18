package com.example.user.my_app_ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

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
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class dog_select_1 extends AppCompatActivity {
    private Button btn_map;
    private  Spinner spnname;
    private TextView txtpid;
    private TextView txtdid;
    private TextView txtvariety;
    private TextView txtfeature;
    private TextView txtlostyear;
    private TextView txtlostmonth;
    private TextView txtlostday;
    private TextView txtlosttime;
    private TextView txtlost_ad;
    private TextView txtfind,fif,fyear,fmonth,fday,fhoure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_select_1);
        btn_map = findViewById(R.id.button9);
        spnname = findViewById(R.id.spinner2);
        txtpid = findViewById(R.id.txtpid);
        txtdid = findViewById(R.id.txtdid);
        txtvariety = findViewById(R.id.textView10);
        txtfeature = findViewById(R.id.textView11);
        txtlostyear = findViewById(R.id.textView27);
        txtlostmonth= findViewById(R.id.textView30);
        txtlostday= findViewById(R.id.textView31);
        txtlosttime= findViewById(R.id.textView32);
        txtlost_ad= findViewById(R.id.loss_ad);
        txtfind = findViewById(R.id.find_t);
        fif = findViewById(R.id.f_if);
        fyear = findViewById(R.id.textView40);
        fmonth = findViewById(R.id.textView41);
        fday = findViewById(R.id.textView42);
        fhoure = findViewById(R.id.textView43);


        Bundle dogpage = getIntent().getExtras();
        String id = dogpage.getString("p_id");
        txtpid.setText(id);
        dogname(id);
        spnname.setOnItemSelectedListener(new spnnameonclik());
        btn_map.setOnClickListener(btnlistener);

    }
    // {Button}把資料傳到下一頁google
    private Button.OnClickListener btnlistener = new Button.OnClickListener() {

        @Override
        public void onClick(View view) {
            String pid = txtpid.getText().toString();
            String did = txtdid.getText().toString();
            Intent intent = new Intent();
            intent.setClass(dog_select_1.this , MapsActivity.class);
            Bundle idpass = new Bundle();
            idpass.putString("p_id",pid);
            idpass.putString("d_id",did);
            intent.putExtras(idpass);
            startActivity(intent);

        }
    };
    //[下拉選單]狗名對應資料庫
    public void dogname(String a) {
        //String uriname = "http://192.168.1.104/dogsql/lost/lost_select_spname.php";
        String uriname = "http://120.96.63.47/dogsql/lost/lost_select_spname.php";
        try {
            HttpClient client = new DefaultHttpClient();
            URI website = new URI(uriname);
            HttpPost request = new HttpPost();
            List params = new ArrayList();
            params.add(new BasicNameValuePair("myid", a));
            UrlEncodedFormEntity env = new UrlEncodedFormEntity (params, HTTP.UTF_8);
            request.setURI(website);
            request.setEntity(env);
            HttpResponse response = client.execute(request);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                String msg = EntityUtils.toString(resEntity);
                String[] array = msg.split("/");
                List Dogname =new ArrayList<>();
                Dogname.clear();
                if("nolost" .equals(msg.toString())){
                    Dogname.add("目前沒有狗掛失");
                    ArrayAdapter<String> DognameList = new ArrayAdapter<>(dog_select_1.this,
                            android.R.layout.simple_spinner_dropdown_item, Dogname);
                    DognameList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnname.setAdapter(DognameList);
                    return;
                }else {
                    for (int j=0;j<=array.length;j++){
                        Dogname.add(array[j]);
                        ArrayAdapter<String> DognameList = new ArrayAdapter<>(dog_select_1.this,
                                android.R.layout.simple_spinner_dropdown_item, Dogname);
                        DognameList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnname.setAdapter(DognameList);
                    }
                }
            }
        }catch  (Exception e){
        }
    }
    //更新查詢
    private class  spnnameonclik implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            selectin(txtpid.getText().toString());
            //selectdoglist(txtpid.getText().toString());
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
    //每次變動狗名>>查詢
    private void selectin(String a){
        //String murl = "http://192.168.1.104/dogsql/lost/lost_select_mysql.php";
        String murl = "http://120.96.63.47/dogsql/lost/lost_select_mysql.php";
        try {
            String name = spnname.getSelectedItem().toString();

            HttpClient client = new DefaultHttpClient();
            URI website = new URI(murl);
            HttpPost request = new HttpPost();
            List params = new ArrayList();
            params.add(new BasicNameValuePair("myid", a));
            params.add(new BasicNameValuePair("myname", name));
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
            is.close();
            String result = sb.toString();
            String data1="";
            String data2="";
            String data3="";
            String data4="";

            int i = 0;
            JSONArray jArray = new JSONArray(result);
            while(i<jArray.length()){
                JSONObject json_data = jArray.getJSONObject(i);
                data1 =json_data.getString("d_id");
                data2 =json_data.getString("d_name");
                data3 =json_data.getString("d_feature");
                data4 =json_data.getString("d_variety");
                i++;
            }
            txtdid.setText(data1.toString());
            txtvariety.setText(data4.toString());
            txtfeature.setText(data3.toString());
            selectdoglist(txtpid.getText().toString());

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void selectdoglist(String a){
        //String muilost = "http://192.168.1.104/dogsql/lost/lost_select_mysql_lost.php";
        String muilost = "http://120.96.63.47/dogsql/lost/lost_select_mysql_lost.php";
        try {
            String did = "";
            did = txtdid.getText().toString();

            HttpClient client = new DefaultHttpClient();
            URI website = new URI(muilost);
            HttpPost request = new HttpPost();
            List params = new ArrayList();
            params.add(new BasicNameValuePair("mypid", a));
            params.add(new BasicNameValuePair("mydid", did));
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
            is.close();
            String result = sb.toString();
            String data1="";
            String data2="";
            String data3="";
            String data4="";
            String data5 = "";
            String finddata1 = "";
            String finddata2 = "";
            String finddata3 = "";

            int i = 0;
            JSONArray jArray = new JSONArray(result);
            while(i<jArray.length()){
                JSONObject json_data = jArray.getJSONObject(i);
                data1 =json_data.getString("l_yon");
                data2 =json_data.getString("l_location");
                data3 =json_data.getString("l_time");
                data4 =json_data.getString("l_address");
                finddata1 =json_data.getString("f_address");
                finddata2 =json_data.getString("f_location");
                finddata3 =json_data.getString("f_time");

                i++;
            }
            String[] array = data3.split("/");
            txtlostyear.setText(array[0]);
            txtlostmonth.setText(array[1]);
            txtlostday.setText(array[2]);
            txtlosttime.setText(array[3]);
            txtlost_ad.setText("-"+data4);
            txtfind.setText("-"+finddata1);

            if ("0".equals(data1.toString())){
                fif.setText("未找到");
            }else {
                fif.setText("已找到");
            }
            if("".equals(finddata3)){
                fyear.setText("");
                fmonth.setText("");
                fday.setText("");
                fhoure.setText("");
            }else{
                String[] time = finddata3.split("/");
                fyear.setText(time[0]);
                fmonth.setText(time[1]);
                fday.setText(time[2]);
                fhoure.setText(time[3]);
            }





        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
