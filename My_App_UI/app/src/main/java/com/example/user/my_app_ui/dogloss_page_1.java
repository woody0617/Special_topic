package com.example.user.my_app_ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static java.util.Collections.binarySearch;

public class dogloss_page_1 extends AppCompatActivity {
    private Spinner spn;
    private TextView txtvariety;
    private TextView txtfeature;
    private TextView test;
    private TextView dogid;
    private TextView txtid;
    private Spinner spnYear;
    private Spinner spnMonth;
    private Spinner spnDay;
    private Spinner spntime;
    private EditText edtad;
    private Button btn;
    private static ProgressDialog dialog= null;
    //public  String murl = "http://192.168.1.104/dogsql/dog/DogSelectmysql.php";
    public  String murl = "http://120.96.63.47/dogsql/dog/DogSelectmysql.php";

    public static int flag = 0;
    private static final int DIALOG1_KEY = 0;
    private static  int a = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dogloss_page_1);

        test = findViewById(R.id.textView18);
        spnYear =findViewById(R.id.spinner3);
        spnMonth =findViewById(R.id.spinner4);
        spnDay =findViewById(R.id.spinner5);
        spntime = findViewById(R.id.spinner7);
        edtad = findViewById(R.id.editText14);
        spn = findViewById(R.id.spinner2);
        btn = findViewById(R.id.button);
        txtid = findViewById(R.id.txtpid);
        dogid = findViewById(R.id.textView19);
        txtvariety = findViewById(R.id.textView10);
        txtfeature = findViewById(R.id.textView11);



        Bundle dogpage = getIntent().getExtras();
        String id = dogpage.getString("p_id");
        txtid.setText(id.toString());
        dogname(id);
        spn.setOnItemSelectedListener(new dogloss_page_1.spnnameonclik());
        spnMonth.setOnItemSelectedListener(new dogloss_page_1.ProvOnItemSelectedListener());
        spnYear.setOnItemSelectedListener(new dogloss_page_1.ProvOnItemSelectedListener());
        spntimelist();
        spnyearlist();
        spnmonthlist();
        edtad.setText("");
        btn.setOnClickListener(btnlistener);
        //btn.setOnClickListener();


    }

    //[button]位址轉經緯度
    public void start(){
        new GetCoordinates().execute(edtad.getText().toString().replace("","+"));
        //String ad = test.getText().toString();
    }
    private class GetCoordinates extends AsyncTask<String,Void,String> {
        ProgressDialog dialog = new ProgressDialog(dogloss_page_1.this);

        /*@Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("請稍後....");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }*/
        @Override
        protected String doInBackground(String... strings) {
            String response;
            try{
                String address = strings[0];
                HttpConnection http = new HttpConnection();
//                String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s",address);
                String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s,+CA&key=AIzaSyBuIAr171HUkpYR296sAtC8fhq0dB39A_M",address);
                response = http.getHTTPData(url);
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

                String lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lat").toString();
                String lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lng").toString();

                test.setText(String.format("%s,%s",lat,lng));

                if(dialog.isShowing())
                    dialog.dismiss();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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

    //更新查詢
    private class  spnnameonclik implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            selectin(txtid.getText().toString());
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    //上傳
    private Button.OnClickListener btnlistener = new Button.OnClickListener() {
        //public String murl = "http://192.168.1.104/dogsql/lost/uplossdate.php";
        public String murl = "http://120.96.63.47/dogsql/lost/uplossdate.php";

        @Override
        public void onClick(View view) {
            String abcmurl = "http://120.96.63.47/dogsql/lost/lost_Verify.php";
            try {
                String pid = txtid.getText().toString();
                String did = dogid.getText().toString();
                String variety = txtvariety.getText().toString();
                if ("柯基".equals(variety)){
                    variety = "corgi";
                }else if("哈士奇".equals(variety)){
                    variety ="husky";
                }else{
                    variety ="shibainu";
                }
                HttpClient client = new DefaultHttpClient();
                URI website = new URI(abcmurl);
                HttpPost request = new HttpPost();
                List paramsabc = new ArrayList();
                paramsabc.add(new BasicNameValuePair("pid", pid));
                paramsabc.add(new BasicNameValuePair("did", did));
                paramsabc.add(new BasicNameValuePair("myvariety", variety));
                UrlEncodedFormEntity env = new UrlEncodedFormEntity (paramsabc, HTTP.UTF_8);
                request.setURI(website);
                request.setEntity(env);
                HttpResponse response = client.execute(request);
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String msg = EntityUtils.toString(resEntity);
                    Log.d("MGG",msg);
                    //1.對不起，此狗已註冊掛失  2.請先上傳圖片 3.圖片上傳數量過少
                    if("1".equals(msg.toString())){
                        Toast.makeText(dogloss_page_1.this,"對不起，此狗已註冊掛失",Toast.LENGTH_LONG).show();
                        return ;
                    }else if("2".equals(msg.toString())){
                        Toast.makeText(dogloss_page_1.this,"請先上傳圖片",Toast.LENGTH_LONG).show();
                        return ;
                    }else if("3".equals(msg.toString())){
                        Toast.makeText(dogloss_page_1.this,"圖片上傳數量過少",Toast.LENGTH_LONG).show();
                        return ;
                    }
                }
                /*防呆===============================================================*/
            }catch (Exception e) {
                e.printStackTrace();
            }
            if("".equals(edtad.getText().toString())&&"".equals(test.getText().toString())){
                Toast.makeText(dogloss_page_1.this,"請輸入地址",Toast.LENGTH_LONG).show();
                flag = 0;
                return ;
            }
            //00000
            if (flag == 0) {
                start();
                Toast.makeText(dogloss_page_1.this,"確定送出掛失，請在點擊",Toast.LENGTH_LONG).show();
                flag = 1;
            }else {

                try {
                    //2222
                    if("".equals(test.getText().toString())){
                        Toast.makeText(dogloss_page_1.this,"經緯度未轉換請再點一次",Toast.LENGTH_LONG).show();
                        flag = 0;
                        return ;
                    }

                    String pid = txtid.getText().toString();
                    String ad = test.getText().toString();
                    String did = dogid.getText().toString();
                    String name = spn.getSelectedItem().toString();
                    String adname = edtad.getText().toString();
                    String variety = txtvariety.getText().toString();
                    String feature = txtfeature.getText().toString();
                    //取得系統時間
                    Calendar c = Calendar.getInstance();
                    int now_year = c.get(Calendar.YEAR);
                    int now_month = c.get(Calendar.MONTH)+1;
                    int now_day = c.get(Calendar.DAY_OF_MONTH);
                    int now_time = c.get(Calendar.HOUR_OF_DAY);
                    String agey = spnYear.getSelectedItem().toString();
                    int agem = Integer.valueOf(spnMonth.getSelectedItem().toString());
                    int aged = Integer.valueOf(spnDay.getSelectedItem().toString());
                    int time = Integer.valueOf(spntime.getSelectedItem().toString());
                    // || <<or的意思***比較輸入的值是否超過目前月日***月日判斷 8>>08  1>>01  10>>10
                    String i ="";
                    String j ="";
                    String k ="";
                    //是否為今年
                    if(now_year==Integer.valueOf(agey)){
                        //如果是就比較月日時有無超過目前時間
                        if (now_month==agem && now_day<aged){
                            Toast.makeText(dogloss_page_1.this,"輸入日期不合，請重新輸入",Toast.LENGTH_LONG).show();
                            return ;
                        }else if(now_month<agem) {
                            Toast.makeText(dogloss_page_1.this,"輸入月份不合，請重新輸入",Toast.LENGTH_LONG).show();
                            return ;
                        }else if(now_month==agem && now_day == aged && now_time<time) {
                            Toast.makeText(dogloss_page_1.this,"輸入時間不合，請重新輸入",Toast.LENGTH_LONG).show();
                            return ;
                        }else{
                            if (agem < 10) {
                                i = "0" + String.valueOf(agem);
                            } else {
                                i = String.valueOf(agem);
                            }
                            if (aged < 10) {
                                j = "0" + String.valueOf(aged);
                            } else {
                                j = String.valueOf(aged);
                            }
                            if (time < 10) {
                                k = "0" + String.valueOf(time);
                            } else {
                                k = String.valueOf(time);
                            }
                        }
                    }else {
                        if (agem < 10) {
                            i = "0" + String.valueOf(agem);
                        } else {
                            i = String.valueOf(agem);
                        }
                        if (aged < 10) {
                            j = "0" + String.valueOf(aged);
                        } else {
                            j = String.valueOf(aged);
                        }
                        if (time < 10) {
                            k = "0" + String.valueOf(time);
                        } else {
                            k = String.valueOf(time);
                        }
                    }
                    String age =agey+"/"+i+"/"+j+"/"+k;
                    //img_movepath(pid,did,variety);
                    String yon = "0";

                    String murla = "http://120.96.63.47/dogsql/lost/uplossdate.php";
                    HttpClient client = new DefaultHttpClient();
                    URI website = new URI(murla);
                    HttpPost request = new HttpPost();
                    List params = new ArrayList();
                    params.add(new BasicNameValuePair("pid", pid));
                    params.add(new BasicNameValuePair("dogid", did));
                    params.add(new BasicNameValuePair("myname", name));
                    params.add(new BasicNameValuePair("myvariety", variety));
                    params.add(new BasicNameValuePair("myfeature", feature));
                    params.add(new BasicNameValuePair("mytime", age));
                    params.add(new BasicNameValuePair("myaddress", ad));
                    params.add(new BasicNameValuePair("myadname", adname));
                    params.add(new BasicNameValuePair("myyon", yon));
                    UrlEncodedFormEntity env = new UrlEncodedFormEntity (params, HTTP.UTF_8);
                    request.setURI(website);
                    request.setEntity(env);
                    HttpResponse response = client.execute(request);
                    HttpEntity resEntity = response.getEntity();
                    if (resEntity != null) {
                        String msg = EntityUtils.toString(resEntity);
                        Toast.makeText(dogloss_page_1.this,msg,Toast.LENGTH_LONG).show();
                        final ProgressDialog pd1 = ProgressDialog.show(dogloss_page_1.this,"","模組訓練中...",true, true);
                        Thread thread = new Thread() {
                            public void run() {
                                try {
                                    String pid = txtid.getText().toString();
                                    String did = dogid.getText().toString();
                                    String variety = txtvariety.getText().toString();
                                    img_movepath(pid,did,variety);
                                    sleep(0000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                pd1.dismiss();//重要
                                dogloss_page_1.this.finish();
                            }
                        };
                        thread.start();


                    } else {
                        String msg = "無資料";
                    }
                    //無法跨頁傳值給查詢所以無法從這頁跳過去
                    flag = 0;
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            /*if(flag == 1){
                final ProgressDialog pd1 = ProgressDialog.show(dogloss_page_1.this,"","模組訓練中...",true, true);
                Thread thread = new Thread() {
                    public void run() {
                        try {
                            String pid = txtid.getText().toString();
                            String did = dogid.getText().toString();
                            String variety = txtvariety.getText().toString();
                            //showDialog(DIALOG1_KEY);
                            img_movepath(pid,did,variety);
                            sleep(0000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        pd1.dismiss();// 万万不可少这句，否则会程序会卡死。
                    }
                };
                thread.start();
            }*/



            /*String abcmurl = "http://120.96.63.47/dogsql/lost/lost_Verify.php";

            try {
                String pid = txtid.getText().toString();
                String did = dogid.getText().toString();
                String variety = txtvariety.getText().toString();
                if ("柯基".equals(variety)){
                    variety = "corgi";
                }else if("哈士奇".equals(variety)){
                    variety ="husky";
                }else{
                    variety ="shibainu";
                }
                HttpClient client = new DefaultHttpClient();
                URI website = new URI(abcmurl);
                HttpPost request = new HttpPost();
                List paramsabc = new ArrayList();
                paramsabc.add(new BasicNameValuePair("pid", pid));
                paramsabc.add(new BasicNameValuePair("did", did));
                paramsabc.add(new BasicNameValuePair("myvariety", variety));
                UrlEncodedFormEntity env = new UrlEncodedFormEntity (paramsabc, HTTP.UTF_8);
                request.setURI(website);
                request.setEntity(env);
                HttpResponse response = client.execute(request);
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String msg = EntityUtils.toString(resEntity);
                    Log.d("MGG",msg);
                    //1.對不起，此狗已註冊掛失  2.請先上傳圖片 3.圖片上傳數量過少
                    if("1".equals(msg.toString())){
                        Toast.makeText(dogloss_page_1.this,"對不起，此狗已註冊掛失",Toast.LENGTH_LONG).show();
                        return;
                    }else if("2".equals(msg.toString())){
                        Toast.makeText(dogloss_page_1.this,"請先上傳圖片",Toast.LENGTH_LONG).show();
                        return;
                    }else if("3".equals(msg.toString())){
                        Toast.makeText(dogloss_page_1.this,"圖片上傳數量過少",Toast.LENGTH_LONG).show();
                        return;
                    }

                }
            }catch (Exception e) {
                e.printStackTrace();
            }*/

            /*if("".equals(edtad.getText().toString())&&"".equals(test.getText().toString())){
                Toast.makeText(dogloss_page_1.this,"請輸入地址",Toast.LENGTH_LONG).show();
                flag = 0;
                return;
            }
            try {
                //00000
                if (flag == 0) {
                    start();
                    Toast.makeText(dogloss_page_1.this,"確定送出掛失，請在點擊",Toast.LENGTH_LONG).show();
                    flag = 1;
                    dialog.dismiss();

                }else{
                    //2222
                    if("".equals(test.getText().toString())){
                        Toast.makeText(dogloss_page_1.this,"請輸入地址",Toast.LENGTH_LONG).show();
                        flag = 0;
                        return;
                    }
                    String pid = txtid.getText().toString();
                    String ad = test.getText().toString();
                    String did = dogid.getText().toString();
                    String name = spn.getSelectedItem().toString();
                    String adname = edtad.getText().toString();
                    String variety = txtvariety.getText().toString();
                    String feature = txtfeature.getText().toString();
                    //取得系統時間
                    Calendar c = Calendar.getInstance();
                    int now_year = c.get(Calendar.YEAR);
                    int now_month = c.get(Calendar.MONTH)+1;
                    int now_day = c.get(Calendar.DAY_OF_MONTH);
                    int now_time = c.get(Calendar.HOUR_OF_DAY);
                    String agey = spnYear.getSelectedItem().toString();
                    int agem = Integer.valueOf(spnMonth.getSelectedItem().toString());
                    int aged = Integer.valueOf(spnDay.getSelectedItem().toString());
                    int time = Integer.valueOf(spntime.getSelectedItem().toString());
                    // || <<or的意思***比較輸入的值是否超過目前月日***月日判斷 8>>08  1>>01  10>>10
                    String i ="";
                    String j ="";
                    String k ="";
                    //是否為今年
                    if(now_year==Integer.valueOf(agey)){
                        //如果是就比較月日時有無超過目前時間
                        if (now_month==agem && now_day<aged){
                            Toast.makeText(dogloss_page_1.this,"輸入日期不合，請重新輸入",Toast.LENGTH_LONG).show();
                            return;
                        }else if(now_month<agem) {
                            Toast.makeText(dogloss_page_1.this,"輸入月份不合，請重新輸入",Toast.LENGTH_LONG).show();
                            return;
                        }else if(now_time<time) {
                            Toast.makeText(dogloss_page_1.this,"輸入時間不合，請重新輸入",Toast.LENGTH_LONG).show();
                            return;
                        }else{
                            if (agem < 10) {
                                i = "0" + String.valueOf(agem);
                            } else {
                                i = String.valueOf(agem);
                            }
                            if (aged < 10) {
                                j = "0" + String.valueOf(aged);
                            } else {
                                j = String.valueOf(aged);
                            }
                            if (time < 10) {
                                k = "0" + String.valueOf(time);
                            } else {
                                k = String.valueOf(time);
                            }
                        }
                    }else {
                        if (agem < 10) {
                            i = "0" + String.valueOf(agem);
                        } else {
                            i = String.valueOf(agem);
                        }
                        if (aged < 10) {
                            j = "0" + String.valueOf(aged);
                        } else {
                            j = String.valueOf(aged);
                        }
                        if (time < 10) {
                            k = "0" + String.valueOf(time);
                        } else {
                            k = String.valueOf(time);
                        }
                    }

                    String age =agey+"/"+i+"/"+j+"/"+k;
                    //img_movepath(pid,did,variety);
                    String yon = "0";

                    HttpClient client = new DefaultHttpClient();
                    URI website = new URI(this.murl);
                    HttpPost request = new HttpPost();
                    List params = new ArrayList();
                    params.add(new BasicNameValuePair("pid", pid));
                    params.add(new BasicNameValuePair("dogid", did));
                    params.add(new BasicNameValuePair("myname", name));
                    params.add(new BasicNameValuePair("myvariety", variety));
                    params.add(new BasicNameValuePair("myfeature", feature));
                    params.add(new BasicNameValuePair("mytime", age));
                    params.add(new BasicNameValuePair("myaddress", ad));
                    params.add(new BasicNameValuePair("myadname", adname));
                    params.add(new BasicNameValuePair("myyon", yon));
                    UrlEncodedFormEntity env = new UrlEncodedFormEntity (params, HTTP.UTF_8);
                    request.setURI(website);
                    request.setEntity(env);
                    HttpResponse response = client.execute(request);
                    HttpEntity resEntity = response.getEntity();
                    if (resEntity != null) {
                        String msg = EntityUtils.toString(resEntity);
                        Toast.makeText(dogloss_page_1.this,msg,Toast.LENGTH_LONG).show();
                        dogloss_page_1.this.finish();
                    } else {
                        String msg = "無資料";
                    }
                    //無法跨頁傳值給查詢所以無法從這頁跳過去
                    flag = 0;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
    };


    //複製資料夾&重新辨識
    public void img_movepath(String pid,String did,String variety){

        String img_murl = "http://120.96.63.47/dogsql/imgpath/move.php";
        try {
            HttpClient client = new DefaultHttpClient();
            URI website = new URI(img_murl);
            HttpPost request = new HttpPost();
            List paramsabc = new ArrayList();
            paramsabc.add(new BasicNameValuePair("pid", pid));
            paramsabc.add(new BasicNameValuePair("did", did));
            paramsabc.add(new BasicNameValuePair("variety", variety));
            UrlEncodedFormEntity env = new UrlEncodedFormEntity (paramsabc, HTTP.UTF_8);
            request.setURI(website);
            request.setEntity(env);
            HttpResponse response = client.execute(request);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                String msg = EntityUtils.toString(resEntity);
                //Toast.makeText(dogloss_page_1.this,msg.toString(),Toast.LENGTH_LONG).show();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG1_KEY: {
                String abcmurl = "http://120.96.63.47/dogsql/lost/lost_Verify.php";
                try {
                    String pid = txtid.getText().toString();
                    String did = dogid.getText().toString();
                    String variety = txtvariety.getText().toString();
                    if ("柯基".equals(variety)){
                        variety = "corgi";
                    }else if("哈士奇".equals(variety)){
                        variety ="husky";
                    }else{
                        variety ="shibainu";
                    }
                    HttpClient client = new DefaultHttpClient();
                    URI website = new URI(abcmurl);
                    HttpPost request = new HttpPost();
                    List paramsabc = new ArrayList();
                    paramsabc.add(new BasicNameValuePair("pid", pid));
                    paramsabc.add(new BasicNameValuePair("did", did));
                    paramsabc.add(new BasicNameValuePair("myvariety", variety));
                    UrlEncodedFormEntity env = new UrlEncodedFormEntity (paramsabc, HTTP.UTF_8);
                    request.setURI(website);
                    request.setEntity(env);
                    HttpResponse response = client.execute(request);
                    HttpEntity resEntity = response.getEntity();
                    if (resEntity != null) {
                        String msg = EntityUtils.toString(resEntity);
                        Log.d("MGG",msg);
                        //1.對不起，此狗已註冊掛失  2.請先上傳圖片 3.圖片上傳數量過少
                        if("1".equals(msg.toString())){
                            Toast.makeText(dogloss_page_1.this,"對不起，此狗已註冊掛失",Toast.LENGTH_LONG).show();
                            return null;
                        }else if("2".equals(msg.toString())){
                            Toast.makeText(dogloss_page_1.this,"請先上傳圖片",Toast.LENGTH_LONG).show();
                            return null;
                        }else if("3".equals(msg.toString())){
                            Toast.makeText(dogloss_page_1.this,"圖片上傳數量過少",Toast.LENGTH_LONG).show();
                            return null;
                        }
                    }
                    /*防呆===============================================================*/
                }catch (Exception e) {
                    e.printStackTrace();
                }
                if("".equals(edtad.getText().toString())&&"".equals(test.getText().toString())){
                    Toast.makeText(dogloss_page_1.this,"請輸入地址",Toast.LENGTH_LONG).show();
                    flag = 0;
                    return null;
                }
                //00000
                if (flag == 0) {
                    start();
                    Toast.makeText(dogloss_page_1.this,"確定送出掛失，請在點擊",Toast.LENGTH_LONG).show();
                    flag = 1;
                }else {

                    try {
                        //2222
                        if("".equals(test.getText().toString())){
                            Toast.makeText(dogloss_page_1.this,"請輸入地址",Toast.LENGTH_LONG).show();
                            flag = 0;
                            return null;
                        }

                        String pid = txtid.getText().toString();
                        String ad = test.getText().toString();
                        String did = dogid.getText().toString();
                        String name = spn.getSelectedItem().toString();
                        String adname = edtad.getText().toString();
                        String variety = txtvariety.getText().toString();
                        String feature = txtfeature.getText().toString();
                        //取得系統時間
                        Calendar c = Calendar.getInstance();
                        int now_year = c.get(Calendar.YEAR);
                        int now_month = c.get(Calendar.MONTH)+1;
                        int now_day = c.get(Calendar.DAY_OF_MONTH);
                        int now_time = c.get(Calendar.HOUR_OF_DAY);
                        String agey = spnYear.getSelectedItem().toString();
                        int agem = Integer.valueOf(spnMonth.getSelectedItem().toString());
                        int aged = Integer.valueOf(spnDay.getSelectedItem().toString());
                        int time = Integer.valueOf(spntime.getSelectedItem().toString());
                        // || <<or的意思***比較輸入的值是否超過目前月日***月日判斷 8>>08  1>>01  10>>10
                        String i ="";
                        String j ="";
                        String k ="";
                        //是否為今年
                        if(now_year==Integer.valueOf(agey)){
                            //如果是就比較月日時有無超過目前時間
                            if (now_month==agem && now_day<aged){
                                Toast.makeText(dogloss_page_1.this,"輸入日期不合，請重新輸入",Toast.LENGTH_LONG).show();
                                return null;
                            }else if(now_month<agem) {
                                Toast.makeText(dogloss_page_1.this,"輸入月份不合，請重新輸入",Toast.LENGTH_LONG).show();
                                return null;
                            }else if(now_time<time) {
                                Toast.makeText(dogloss_page_1.this,"輸入時間不合，請重新輸入",Toast.LENGTH_LONG).show();
                                return null;
                            }else{
                                if (agem < 10) {
                                    i = "0" + String.valueOf(agem);
                                } else {
                                    i = String.valueOf(agem);
                                }
                                if (aged < 10) {
                                    j = "0" + String.valueOf(aged);
                                } else {
                                    j = String.valueOf(aged);
                                }
                                if (time < 10) {
                                    k = "0" + String.valueOf(time);
                                } else {
                                    k = String.valueOf(time);
                                }
                            }
                        }else {
                            if (agem < 10) {
                                i = "0" + String.valueOf(agem);
                            } else {
                                i = String.valueOf(agem);
                            }
                            if (aged < 10) {
                                j = "0" + String.valueOf(aged);
                            } else {
                                j = String.valueOf(aged);
                            }
                            if (time < 10) {
                                k = "0" + String.valueOf(time);
                            } else {
                                k = String.valueOf(time);
                            }
                        }
                        String age =agey+"/"+i+"/"+j+"/"+k;
                        //img_movepath(pid,did,variety);
                        String yon = "0";

                        String murla = "http://120.96.63.47/dogsql/lost/uplossdate.php";
                        HttpClient client = new DefaultHttpClient();
                        URI website = new URI(murla);
                        HttpPost request = new HttpPost();
                        List params = new ArrayList();
                        params.add(new BasicNameValuePair("pid", pid));
                        params.add(new BasicNameValuePair("dogid", did));
                        params.add(new BasicNameValuePair("myname", name));
                        params.add(new BasicNameValuePair("myvariety", variety));
                        params.add(new BasicNameValuePair("myfeature", feature));
                        params.add(new BasicNameValuePair("mytime", age));
                        params.add(new BasicNameValuePair("myaddress", ad));
                        params.add(new BasicNameValuePair("myadname", adname));
                        params.add(new BasicNameValuePair("myyon", yon));
                        UrlEncodedFormEntity env = new UrlEncodedFormEntity (params, HTTP.UTF_8);
                        request.setURI(website);
                        request.setEntity(env);
                        HttpResponse response = client.execute(request);
                        HttpEntity resEntity = response.getEntity();
                        if (resEntity != null) {
                            String msg = EntityUtils.toString(resEntity);
                            Toast.makeText(dogloss_page_1.this,msg,Toast.LENGTH_LONG).show();

                            //dogloss_page_1.this.finish();

                        } else {
                            String msg = "無資料";
                        }
                        //無法跨頁傳值給查詢所以無法從這頁跳過去
                        flag = 0;
                    }catch (Exception e) {
                        e.printStackTrace();


                    }
                }

                /*===============================================================*/
                /*if(a == 0){
                    DialogInterface.OnCancelListener cancelListener;
                    cancelListener = new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Toast.makeText(dogloss_page_1.this, "已取消", Toast.LENGTH_LONG).show();
                            a = a + 1;
                        }
                    };
                    ProgressDialog pd1 = ProgressDialog.show(dogloss_page_1.this,"","歐拉歐拉歐拉歐拉",false, true, cancelListener);
                }else{
                    DialogInterface.OnCancelListener cancelListener;
                    cancelListener = new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Toast.makeText(dogloss_page_1.this, "已取消2", Toast.LENGTH_LONG).show();
                            a = a*0;
                        }
                    };
                    ProgressDialog pd2 = ProgressDialog.show(dogloss_page_1.this,"","姆搭姆搭姆搭姆搭",false, true, cancelListener);
                }*/
            }
        }
        return null;
    }







    //[下拉選單]狗名對應資料庫
    public void dogname(String a) {
        //String uriname = "http://192.168.1.104/dogsql/dog/dogtest.php";
        String uriname = "http://120.96.63.47/dogsql/dog/dogtest.php";
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
                //txt.setText(msg);
                String[] array = msg.split("/");
                List Dogname =new ArrayList<>();
                for (int j=0;j<=array.length;j++){
                    Dogname.add(array[j]);
                    ArrayAdapter<String> DognameList = new ArrayAdapter<>(dogloss_page_1.this,
                            android.R.layout.simple_spinner_dropdown_item, Dogname);
                    DognameList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spn.setAdapter(DognameList);
                }
            }
        }catch  (Exception e){
        }
         Log.d("MM","44");
    }
    //[下拉選單]寫入時間到目前時間
    private void spntimelist() {
        Calendar c = Calendar.getInstance();//取得系統time
        int hour = c.get(Calendar.HOUR_OF_DAY);
        List Dogtime =new ArrayList<>();
        for (int i= 0 ; i < 24; i++) {
            Dogtime.add(i);
        }
        int b = binarySearch(Dogtime, hour);
        ArrayAdapter<String> DogtimeList = new ArrayAdapter<>(dogloss_page_1.this,
                android.R.layout.simple_spinner_dropdown_item, Dogtime);
        DogtimeList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spntime.setAdapter(DogtimeList);
        spntime.setSelection(b,true);//透過位置抓取輸入的年分變成預設
    }
    //[下拉選單]寫入預設年到目前年
    private void spnyearlist() {
        Calendar c = Calendar.getInstance();//取得系統年
        int year = c.get(Calendar.YEAR);
        List Dogyear =new ArrayList<>();
        for (int i= year-2 ; i <= year; i++) {
            Dogyear.add(i);
        }
        int b = binarySearch(Dogyear, year);//比對list裡有無a值有就回傳索引值
        ArrayAdapter<String> DogyearList = new ArrayAdapter<>(dogloss_page_1.this,
                android.R.layout.simple_spinner_dropdown_item, Dogyear);
        DogyearList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnYear.setAdapter(DogyearList);
        spnYear.setSelection(b,true);//透過位置抓取輸入的年分變成預設
    }
    //[下拉選單]寫入1~12月
    public void spnmonthlist() {
        int month = 12;//取得系統1~12月

        Calendar c = Calendar.getInstance();//取得系統年
        int month_now =  c.get(Calendar.MONTH)+ 1;
        List Dogmonth =new ArrayList<>();
        for (int i = 1; i <= month; i++) {
            Dogmonth.add(i);
        }
        int b = binarySearch(Dogmonth, month_now);//比對list裡有無a值有就回傳索引值
        ArrayAdapter<String> lunchList = new ArrayAdapter<>(dogloss_page_1.this,
                android.R.layout.simple_spinner_dropdown_item, Dogmonth);
        spnMonth.setAdapter(lunchList);
        spnMonth.setSelection(b,true);//透過位置抓取輸入的分變成預設
    }
    //[下拉選單]抓取day值每次判斷天數
    private class  ProvOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
            spndaylist();
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            List doglist =new ArrayList<>();
            doglist.add("-");
            ArrayAdapter<String> lunchList = new ArrayAdapter<>(dogloss_page_1.this,
                    android.R.layout.simple_spinner_dropdown_item, doglist);
            spnDay.setAdapter(lunchList);
        }

    }
    //判斷day值觸發事件
    public  void spndaylist() {
        int month =0;
        int year =0;
        int i;
        Calendar c = Calendar.getInstance();//取得系統時間
        int day = c.get(Calendar.DAY_OF_MONTH);
        month = Integer.parseInt(spnMonth.getSelectedItem().toString());
        year = Integer.parseInt(spnYear.getSelectedItem().toString());

        List dogday =new ArrayList<>();
        dogday.clear();
        /*1>判斷偶數奇數
        2>2月/8月/其他偶數
        3>2月28/29日 8月也是31天
        4>判斷閏年*/
        //1~7月
        if(month < 8){
            if(month % 2 == 0){
                if(month==2){//2月
                    if(year%4==0){
                        if(year%100==0){
                            if(year%400==0){//29天
                                for ( i = 1; i <= 29; i++) {
                                    dogday.add(i);
                                }
                            }else{//29天
                                for ( i = 1; i <= 28; i++) {
                                    dogday.add(i);
                                }
                            }
                        }else{//29天
                            for (i = 1; i <= 29; i++) {
                                dogday.add(i);
                            }
                        }
                    }else{//28天
                        for (i = 1; i <= 28; i++) {
                            dogday.add(i);
                        }
                    }
                }else{//4.6月
                    for (i = 1; i <= 30; i++) {
                        dogday.add(i);
                    }
                }
                //1.3.5.7月
            }else{
                for (i = 1; i <= 31; i++) {
                    dogday.add(i);
                }
            }
        }else{//8~12月
            if(month % 2 == 0){
                for (i = 1; i <= 31; i++) {
                    dogday.add(i);
                }
            }else{
                for (i = 1; i <= 30; i++) {
                    dogday.add(i);
                }
            }
        }
        int b = binarySearch(dogday, day);
        ArrayAdapter<String> lunchList = new ArrayAdapter<>(dogloss_page_1.this,
                android.R.layout.simple_spinner_dropdown_item, dogday);
        spnDay.setAdapter(lunchList);
        spnDay.setSelection(b,true);

    }
    //查詢資料帶入
    public void selectin(String a){
        try {
            String name = spn.getSelectedItem().toString();
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
            String data5="";
            int i = 0;
            JSONArray jArray = new JSONArray(result);
            while(i<jArray.length()){
                JSONObject json_data = jArray.getJSONObject(i);
                data1 =json_data.getString("d_id");
                data2 =json_data.getString("d_name");
                data3 =json_data.getString("d_feature");
                data4 =json_data.getString("d_age");
                data5 =json_data.getString("d_variety");
                i++;
            }
            txtvariety.setText(data5);
            txtfeature.setText(data3);
            dogid.setText(data1);

        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
