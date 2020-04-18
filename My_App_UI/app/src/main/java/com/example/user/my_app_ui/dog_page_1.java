package com.example.user.my_app_ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.util.Collections.binarySearch;

public class dog_page_1 extends AppCompatActivity implements View.OnClickListener {
    private Spinner spnYear;
    private Spinner spnMonth;
    private Spinner spnDay;
    private Spinner spndogid;
    private Spinner spnvariety;
    private Spinner spnname;

    private EditText edtfeature;
    private EditText edtname;

    private Button btnupdate;
    private Button btn4;
    private Button btn8;
    private TextView txtid;
    private TextView txtd_id;

    private int serverResponseCode = 0;
    private ProgressDialog dialog = null;
    private String upLoadServerUri = null;
    private String imagepath=null;
    ArrayList<String> imagesEncodedList;
    int all_size = 0,now_size = 0;
    int defaultday = 0,now_day = 0;

    //public  String murl = "http://192.168.1.104/dogsql/dog/DogSelectmysql.php";
    public  String murl = "http://120.96.63.47/dogsql/dog/DogSelectmysql.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_page_1);
        spnname =findViewById(R.id.spinner);
        spnYear =findViewById(R.id.spinner3);
        spnMonth =findViewById(R.id.spinner4);
        spnDay =findViewById(R.id.spinner5);
        spndogid =findViewById(R.id.spinner);
        spnvariety =findViewById(R.id.spinner2);
        edtfeature=findViewById(R.id.editText4);
        edtname = findViewById(R.id.editText12);
        txtid = findViewById(R.id.txtpid);
        txtd_id = findViewById(R.id.test5);
        btnupdate = findViewById(R.id.button);
        btn4 =findViewById(R.id.button4);
        btn8 =findViewById(R.id.button8);

        Bundle dogpage = getIntent().getExtras();
        String id = dogpage.getString("p_id");
        txtid.setText(id.toString());
        dogname(id);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            btn4.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        } else {
            btn4.setEnabled(true);
        }

        upLoadServerUri = "http://120.96.63.47/dogsql/imgpath/multi_upload.php";

        spnname.setOnItemSelectedListener(new spnnameonclik());
        btnupdate.setOnClickListener(btnlistener);
        btn4.setOnClickListener(this);
        btn8.setOnClickListener(btnadd);
        //spnvariety.setOnItemSelectedListener(new spnvarietychange());

    }
    //[Button]上傳照片(資料夾)

        //String muri_mkdir_path = "http://192.168.1.104/dogsql/dog_mkdir_path.php";
        String muri_mkdir_path = "http://120.96.63.47/dogsql/imgpath/dog_mkdir_path.php";
        int count = 0;
        @Override
        public void onClick(View view) {
            try {
                final String dogid = txtd_id.getText().toString();
                final String peopleid = txtid.getText().toString();


                if (count == 0) {

                    HttpClient client = new DefaultHttpClient();
                    URI website = new URI(muri_mkdir_path);
                    HttpPost request = new HttpPost();

                    List params = new ArrayList();
                    params.add(new BasicNameValuePair("dogid", dogid));
                    params.add(new BasicNameValuePair("myid", peopleid));
                    UrlEncodedFormEntity env = new UrlEncodedFormEntity (params, HTTP.UTF_8);
                    request.setURI(website);
                    request.setEntity(env);
                    HttpResponse response = client.execute(request);
                    HttpEntity resEntity = response.getEntity();
                    if (resEntity != null) {
                        String msg = EntityUtils.toString(resEntity);
                        Toast.makeText(dog_page_1.this, msg.toString(),Toast.LENGTH_LONG).show();
                        //return;
                    }

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, ""), 1);

                    btn4.setText("上傳圖片");
                    count = 1;


                } else {
                    dialog = ProgressDialog.show(dog_page_1.this, "", "上傳中......", true);

//                    Toast.makeText(MainActivity.this,imagepath+"",Toast.LENGTH_SHORT).show();
//
                    all_size = imagesEncodedList.size();
                    now_size = all_size;
                    for (final String a :
                            imagesEncodedList) {
                        Log.d("test", a + " ");
                        new Thread(new Runnable() {
                            public void run() {
                                String variety = spnvariety.getSelectedItem().toString();
                                if ("柯基".equals(variety)){
                                    variety = "corgi";
                                }else if("哈士奇".equals(variety)){
                                    variety ="husky";
                                }else{
                                    variety ="shibainu";
                                }
                                uploadFile(a,dogid,peopleid,variety);
                            }
                        }).start();
                    }
                    btn4.setText("選擇圖片");
                    count = 0;


                }
            }catch (Exception e) {
                e.printStackTrace();
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
    //[下拉選單]品種選項
    public void spnvarietylist(String a){
        List Dogvariety = new ArrayList<String>();
        Dogvariety.add("柯基");
        Dogvariety.add("哈士奇");
        Dogvariety.add("柴犬");
        int b = Dogvariety.indexOf(a);//字串找位置
        ArrayAdapter<String> DogvarietyList = new ArrayAdapter<>(dog_page_1.this,
                android.R.layout.simple_spinner_dropdown_item,Dogvariety);
        spnvariety.setAdapter(DogvarietyList);
        spnvariety.setSelection(b,true);
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
                Dogname.clear();
                for (int j=0;j<=array.length;j++){
                    Dogname.add(array[j]);
                    ArrayAdapter<String> DognameList = new ArrayAdapter<>(dog_page_1.this,
                            android.R.layout.simple_spinner_dropdown_item, Dogname);
                    DognameList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnname.setAdapter(DognameList);
                }
            }
        }catch  (Exception e){
        }
    }

    //[下拉選單]寫入預設年到目前年
    public void spnyearlist(int a) {
        Calendar c = Calendar.getInstance();//取得系統年
        int year = c.get(Calendar.YEAR);
        List Dogyear =new ArrayList<>();
        for (int i = 2000; i <= year; i++) {
            Dogyear.add(i);
        }
        int b = binarySearch(Dogyear, a);//比對list裡有無a值有就回傳索引值

        ArrayAdapter<String> DogyearList = new ArrayAdapter<>(dog_page_1.this,
                android.R.layout.simple_spinner_dropdown_item, Dogyear);

        DogyearList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnYear.setAdapter(DogyearList);
        spnYear.setSelection(b,true);//透過位置抓取輸入的年分變成預設

    }
    //[下拉選單]寫入1~12月
    public void spnmonthlist(int a) {
        int month = 12;//取得系統1~12月
        List Dogmonth =new ArrayList<>();
        for (int i = 1; i <= month; i++) {
            Dogmonth.add(i);
        }
        int b = binarySearch(Dogmonth, a);//比對list裡有無a值有就回傳索引值
        ArrayAdapter<String> lunchList = new ArrayAdapter<>(dog_page_1.this,
                android.R.layout.simple_spinner_dropdown_item, Dogmonth);
        spnMonth.setAdapter(lunchList);
        spnMonth.setSelection(b,true);//透過位置抓取輸入的分變成預設
    }
    //[下拉選單]抓取day值每次判斷天數
    private class  ProvOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
            if (defaultday ==0){
                spndaylist2(now_day);
                //defaultday = defaultday + 1;
            }else {
                /*spndaylist();
                defaultday = 0;*/
            }

        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            List doglist =new ArrayList<>();
            doglist.add("-");
            ArrayAdapter<String> lunchList = new ArrayAdapter<>(dog_page_1.this,
                    android.R.layout.simple_spinner_dropdown_item, doglist);
            spnDay.setAdapter(lunchList);
        }
    }
    //判斷day值觸發事件
    public  void spndaylist() {
        int month =0;
        int year =0;
        int i;

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

        ArrayAdapter<String> lunchList = new ArrayAdapter<>(dog_page_1.this,
                android.R.layout.simple_spinner_dropdown_item, dogday);
        spnDay.setAdapter(lunchList);

        }
    //[下拉選單]判斷day值對應原本生日
    public  void spndaylist2(int a) {
        spndaylist();
        int month =0;
        int year =0;
        int i;

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
        int b = binarySearch(dogday, a);
        ArrayAdapter<String> lunchList = new ArrayAdapter<>(dog_page_1.this,
                android.R.layout.simple_spinner_dropdown_item, dogday);
        spnDay.setAdapter(lunchList);
        spnDay.setSelection(b,true);//透過位置抓取輸入的天變成預設
    }
    //每次變動狗名>>查詢
    private void selectin(String a){
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
            txtd_id.setText(data1.toString());
            edtfeature.setText(data3);
            String age = data4;
            //拆字串
            String[] array = age.split("/");
            now_day =Integer.valueOf(array[2]);
            //年
            spnyearlist(Integer.valueOf(array[0]));
            //月
            spnmonthlist(Integer.valueOf(array[1]));
            //日
            spnMonth.setOnItemSelectedListener(new ProvOnItemSelectedListener());
            spnYear.setOnItemSelectedListener(new ProvOnItemSelectedListener());
            spndaylist2(Integer.valueOf(array[2]));
            //名字
            edtname.setText(data2);
            //品種
            spnvarietylist(data5);


        }catch (Exception e) {
            e.printStackTrace();
        }

    }
    //[Button]修改
    private Button.OnClickListener btnlistener = new Button.OnClickListener() {
        //String murl = "http://192.168.1.104/dogsql/dog/DogUpdate.php";
        String murl = "http://120.96.63.47/dogsql/dog/DogUpdate.php";
        @Override
        public void onClick(View view) {
            if("".equals(edtname.getText().toString()))
            {
                Toast.makeText(dog_page_1.this, "姓名不能空白，請輸入狗狗名稱",Toast.LENGTH_LONG).show();
                return;
            }
            if(edtname.getText().length()>15){
                Toast.makeText(dog_page_1.this, "字數不得超過15字",Toast.LENGTH_LONG).show();
                return;
            }
            if("".equals(edtfeature.getText().toString()))
            {
                Toast.makeText(dog_page_1.this, "特徵不能空白，請輸入狗狗特徵",Toast.LENGTH_LONG).show();
                return;
            }
            if(edtfeature.getText().length()>30){
                Toast.makeText(dog_page_1.this, "字數不得超過30字",Toast.LENGTH_LONG).show();
                return;
            }
            try {
                //月日判斷 8>>08  1>>01  10>>10
                String dogid = txtd_id.getText().toString();
                String name = edtname.getText().toString();
                String feature =edtfeature.getText().toString();
                String i ="";
                String j ="";
                String agey = spnYear.getSelectedItem().toString();
                int agem = Integer.valueOf(spnMonth.getSelectedItem().toString());
                int aged = Integer.valueOf(spnDay.getSelectedItem().toString());
                Calendar c = Calendar.getInstance();
                int now_year = c.get(Calendar.YEAR);
                int now_month = c.get(Calendar.MONTH)+1;
                int now_day = c.get(Calendar.DAY_OF_MONTH);
                if(now_year==Integer.valueOf(agey)){
                    //如果是就比較月日時有無超過目前時間
                    if (now_month==agem && now_day<aged){
                        Toast.makeText(dog_page_1.this,"輸入日期不合，請重新輸入",Toast.LENGTH_LONG).show();
                        return ;
                    }else if(now_month<agem) {
                        Toast.makeText(dog_page_1.this,"輸入月份不合，請重新輸入",Toast.LENGTH_LONG).show();
                        return ;
                    }else{
                        if(agem<10){
                            i = "0"+String.valueOf(agem);
                        }else {
                            i = String.valueOf(agem);
                        }
                        if(aged<10){
                            j = "0"+String.valueOf(aged);
                        }else{
                            j=String.valueOf(aged);
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
                }
                String age =agey+"/"+i+"/"+j;
                String variety = spnvariety.getSelectedItem().toString();
                //varietychange();
                String varch = "";
                if ("柯基".equals(variety)){
                    varch = "corgi";
                }else if("哈士奇".equals(variety)){
                    varch ="husky";
                }else{
                    varch ="shibainu";
                }


                HttpClient client = new DefaultHttpClient();
                URI website = new URI(this.murl);
                HttpPost request = new HttpPost();

                List params = new ArrayList();
                params.add(new BasicNameValuePair("pid", txtid.getText().toString()));
                params.add(new BasicNameValuePair("dogid", dogid));
                params.add(new BasicNameValuePair("myname", name));
                params.add(new BasicNameValuePair("myage", age));
                params.add(new BasicNameValuePair("myvariety", variety));
                params.add(new BasicNameValuePair("myvarch", varch));
                params.add(new BasicNameValuePair("myfeature", feature));
                UrlEncodedFormEntity env = new UrlEncodedFormEntity (params, HTTP.UTF_8);
                request.setURI(website);
                request.setEntity(env);
                HttpResponse response = client.execute(request);
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String msg = EntityUtils.toString(resEntity);
                    Log.d("MGG",msg);

                }
                Bundle dogpage = getIntent().getExtras();
                String id = dogpage.getString("p_id");
                dogname(id);
                Toast.makeText(dog_page_1.this, "更新資料完成",Toast.LENGTH_LONG).show();
                //====要寫修改成功提示====


            }catch (Exception e) {
                e.printStackTrace();
            }
        }

    };
    //[Button]新增狗
    private Button.OnClickListener btnadd = new Button.OnClickListener(){
        String murl="0";
        @Override
        public void onClick(View view) {
            Bundle dogpage = getIntent().getExtras();
            String id = dogpage.getString("p_id");
            Intent intent = new Intent();
            intent.setClass(dog_page_1.this , dog_page_2_add.class);
            Bundle dogpageadd = new Bundle();
            dogpageadd.putString("p_id",id);
            intent.putExtras(dogpageadd);
            startActivity(intent);
        }
    };

    //{IMG_Permission}判斷權限是否開啟
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                btn4.setEnabled(true);
            }
        }
    }

    //{IMG_ActivityResult}接收結果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("tt"," 212");
        imagesEncodedList = new ArrayList<String>();
        try {
            // When an Image is picked
            if (requestCode == 1 && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                if(data.getData()!=null){

                    Uri mImageUri=data.getData();

                    imagesEncodedList.add(getPathFromUri(dog_page_1.this,mImageUri));

                    // Get the cursor


                }else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();

                            Log.d("path",getPathFromUri(dog_page_1.this,uri)+" ");
                            imagesEncodedList.add(getPathFromUri(dog_page_1.this,uri));


                        }
                        Log.v("LOG_TAG", "選圖" + mArrayUri.size());
                    }
                }
            } else {
                Toast.makeText(this, "你沒選擇圖片",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.d("error",e.getMessage());
            Toast.makeText(this, "有錯誤", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //{IMG_Getpath}抓取圖片路徑
    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    //{IMG_GetDataColumn}取得儲存的資料型態
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     * {IMG_ESD}外部儲存空間存取架構
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     * {IMG_DD}下載存取架構
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     * {IMG_MD}媒體存取架構
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     * {IMG_GPU}GOOGLE相簿存取架構
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**{IMG_Upload}上傳跟通訊協定*/
    public int uploadFile(String sourceFileUri,String did,String pid,String variety) {


        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

//            dialog.dismiss();

            Log.e("uploadFile", "來源檔案沒被選到: "+imagepath);

            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(dog_page_1.this, "檔案沒被寫入", Toast.LENGTH_SHORT).show();//這行會跳顯示
                }
            });

            return 0;

        }
        else
        {
            try {
                /*String dogid = txtd_id.getText().toString();
                String peopleid = txtid.getText().toString();*/
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"did\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(did);
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"pid\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(pid);
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"variety\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(variety);
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();
                InputStream is = conn.getInputStream();
                String inputStr,show="";
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                while((inputStr = streamReader.readLine())!=null) {
                    show+=inputStr;
                }

                /*Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);
                Log.d("test",show+" "+all_size);*/
                if(serverResponseCode == 200){

                    runOnUiThread(new Runnable() {
                        public void run() {
                            all_size -=1;
//                            String msg = "File Upload Completed.\n\n See uploaded file your server. \n\n";
//                            messageText.setText(msg);
//                            Toast.makeText(dog_page_1.this, "now "+all_size+"/"+now_size, Toast.LENGTH_SHORT).show();
                            Log.d("MSG", all_size+"/"+now_size);
                            if(all_size==0){
                                dialog.dismiss();
                            }
                        }
                    });
                }else{
                    all_size -=1;
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                all_size -=1;
//                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(dog_page_1.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                all_size -=1;
//                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(dog_page_1.this, "抓到錯誤:請看logcat ", Toast.LENGTH_SHORT).show();
                    }
                });
                //Log.e("Upload file to server Exception", "Exception : "  + e.getMessage(), e);
            }
//            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }
}
