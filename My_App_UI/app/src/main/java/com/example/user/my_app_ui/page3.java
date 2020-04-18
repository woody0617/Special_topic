package com.example.user.my_app_ui;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class page3 extends AppCompatActivity {
    private Button btn;
    private EditText edtname;
    private EditText edtfeature;
    private Spinner spnvariety;
    private Spinner spnYear;
    private Spinner spnMonth;
    private Spinner spnDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page3);
        btn = findViewById(R.id.button);
        edtname =findViewById(R.id.editText6);
        edtfeature =findViewById(R.id.editText10);
        spnYear =findViewById(R.id.spinner3);
        spnMonth =findViewById(R.id.spinner4);
        spnDay =findViewById(R.id.spinner5);
        spnvariety=findViewById(R.id.spinner6);
        spnyearlist();
        spnmonthlist();
        spngenderlist();
        spnMonth.setOnItemSelectedListener(new page3.ProvOnItemSelectedListener());
        spnYear.setOnItemSelectedListener(new page3.ProvOnItemSelectedListener());
        btn.setOnClickListener(btnlistener);
    }
    //========email再驗證=======
    public void reemail() throws URISyntaxException, IOException {
        Bundle bundle = getIntent().getExtras();
        String mail = bundle.getString("p_mail");

        //String murimail = "http://192.168.1.104/dogsql/email_Comparison.php";
        //學校
        String murimail = "http://120.96.63.47/dogsql/email_Comparison.php";
        HttpClient client = new DefaultHttpClient();
        URI website = new URI(murimail);
        HttpPost request = new HttpPost();
        List params = new ArrayList();
        params.add(new BasicNameValuePair("myac", mail));
        UrlEncodedFormEntity env = new UrlEncodedFormEntity(params, HTTP.UTF_8);
        request.setURI(website);
        request.setEntity(env);
        HttpResponse response = client.execute(request);
        HttpEntity resEntity = response.getEntity();
        if (resEntity != null) {
            String msg = EntityUtils.toString(resEntity);
            // txt.setText(msg);
            if ("帳號已被註冊過".equals(msg.toString())) {
                Toast.makeText(page3.this, msg.toString(), Toast.LENGTH_LONG).show();
                return;
            } else {
                return;
            }
        }
    }
    //"完成"頁面跳轉
    private Button.OnClickListener btnlistener = new Button.OnClickListener() {
        //String murl = "http://192.168.1.104/dogsql/member/insert.php";
        //學校
        String murl = "http://120.96.63.47/dogsql/member/insert.php";
        @Override
        public void onClick(View view) {
            if("".equals(edtname.getText().toString()))
            {
                Toast.makeText(page3.this, "姓名不能空白，請輸入狗狗名稱",Toast.LENGTH_LONG).show();
                return;
            }
            if(edtname.getText().length()>10 &&edtname.getText().length()<3 ){
                Toast.makeText(page3.this, "字數不得超過15字",Toast.LENGTH_LONG).show();
                return;
            }
            if("".equals(edtfeature.getText().toString()))
            {
                Toast.makeText(page3.this, "特徵不能空白，請輸入狗狗特徵",Toast.LENGTH_LONG).show();
                return;
            }
            if(edtfeature.getText().length()>30){
                Toast.makeText(page3.this, "字數不得超過30字",Toast.LENGTH_LONG).show();
                return;
            }

            /*String a = "名字"+edtname.getText().toString()+"生日"+spnYear.getSelectedItem().toString()+"年"+spnMonth.getSelectedItem().toString()+"月"+spnDay.getSelectedItem().toString()+"日"+"品種"+spnvariety.getSelectedItem().toString();
            txt.setText(a);*/

            try {
                reemail();
                Bundle bundle = getIntent().getExtras();
                String name = bundle.getString("p_name");
                String phone = bundle.getString("p_phone");
                String mail = bundle.getString("p_mail");
                String pwd = bundle.getString("p_pwd");
                String dname =edtname.getText().toString();
                String dvariety = spnvariety.getSelectedItem().toString();
                String dfeature =edtfeature.getText().toString();
                String i,j ="";
                String agey = spnYear.getSelectedItem().toString();
                int agem = Integer.valueOf(spnMonth.getSelectedItem().toString());
                int aged = Integer.valueOf(spnDay.getSelectedItem().toString());

                Calendar c = Calendar.getInstance();
                int now_year = c.get(Calendar.YEAR);
                int now_month = c.get(Calendar.MONTH)+1;
                int now_day = c.get(Calendar.DAY_OF_MONTH);
                if(now_year==Integer.valueOf(agey)){
                    if (now_month==agem && now_day<aged){
                        Toast.makeText(page3.this,"輸入日期不合，請重新輸入",Toast.LENGTH_LONG).show();
                        return ;
                    }else if(now_month<agem) {
                        Toast.makeText(page3.this,"輸入月份不合，請重新輸入",Toast.LENGTH_LONG).show();
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

                HttpClient client = new DefaultHttpClient();
                URI website = new URI(murl);
                HttpPost request = new HttpPost();
                List params = new ArrayList();
                params.add(new BasicNameValuePair("my_name", name));
                params.add(new BasicNameValuePair("my_phone", phone));
                params.add(new BasicNameValuePair("my_mail", mail));
                params.add(new BasicNameValuePair("my_pwd", pwd));
                params.add(new BasicNameValuePair("d_name", dname));
                params.add(new BasicNameValuePair("d_age", age));
                params.add(new BasicNameValuePair("d_variety", dvariety));
                params.add(new BasicNameValuePair("d_feature", dfeature));

                UrlEncodedFormEntity env = new UrlEncodedFormEntity (params,HTTP.UTF_8);
                request.setURI(website);
                //設定POST的List
                request.setEntity(env);

                HttpResponse response = client.execute(request);
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String msg = EntityUtils.toString(resEntity);
                    Toast.makeText(page3.this, msg.toString(),Toast.LENGTH_LONG).show();
                    //txt.setText(msg);
                    Intent intent = new Intent();
                    intent.setClass(page3.this , MainActivity.class);
                    startActivity(intent);
                    page3.this.finish();
                } else {
                    String msg = "無資料";
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    //寫入品種
    public void spngenderlist() {
        List Doggender =new ArrayList<>();
        Doggender.add("柯基");
        Doggender.add("哈士奇");
        Doggender.add("柴犬");
        ArrayAdapter<String> DoggenderList = new ArrayAdapter<>(page3.this,
                android.R.layout.simple_spinner_dropdown_item, Doggender);

        DoggenderList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnvariety.setAdapter(DoggenderList);
    }
    //寫入預設年到目前年
    public void spnyearlist() {
        Calendar c = Calendar.getInstance();//取得系統年
        int year = c.get(Calendar.YEAR);
        List Dogyear =new ArrayList<>();
        for (int i = 2000; i <= year; i++) {
            Dogyear.add(i);
        }
        ArrayAdapter<String> DogyearList = new ArrayAdapter<>(page3.this,
                android.R.layout.simple_spinner_dropdown_item, Dogyear);

        DogyearList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnYear.setAdapter(DogyearList);
        }
    //寫入1~12月
    public void spnmonthlist() {
        int month = 12;//取得系統1~12月
        List Dogmonth =new ArrayList<>();
        for (int i = 1; i <= month; i++) {
            Dogmonth.add(i);
        }
        ArrayAdapter<String> lunchList = new ArrayAdapter<>(page3.this,
                android.R.layout.simple_spinner_dropdown_item, Dogmonth);
        spnMonth.setAdapter(lunchList);
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
        if (month % 2 == 0){//偶數
            if(month==2){
                if(year%4==0){
                    if(year%100==0){
                        if(year%400==0){//28天
                            for ( i = 1; i <= 28; i++) {
                                dogday.add(i);
                            }
                        }else{//29天
                            for ( i = 1; i <= 29; i++) {
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
            }else if(month==8){//奇數
                for (i = 1; i <= 31; i++) {
                    dogday.add(i);
                }
            }else{//偶數
                for (i = 1; i <= 30; i++) {
                    dogday.add(i);
                }
            }
        }else{
            //奇數
            for (i = 1; i <= 31; i++) {
                dogday.add(i);
            }
        }

        ArrayAdapter<String> lunchList = new ArrayAdapter<>(page3.this,
                android.R.layout.simple_spinner_dropdown_item, dogday);
        spnDay.setAdapter(lunchList);
    }
    //抓取day值
    private class  ProvOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
            spndaylist();
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            List doglist =new ArrayList<>();
            doglist.add("-");
            ArrayAdapter<String> lunchList = new ArrayAdapter<>(page3.this,
                    android.R.layout.simple_spinner_dropdown_item, doglist);
            spnDay.setAdapter(lunchList);
        }
    }
}
