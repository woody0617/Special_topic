package com.example.user.my_app_ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.*;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;


public class people_page_1 extends AppCompatActivity {
    private Button btn;

    private EditText edtphone;
    private EditText edtname;
    private TextView txtpwd;
    private TextView txtemail;
    //public String murl = "http://192.168.1.104/dogsql/member/selectmysql.php";
    public String murl = "http://120.96.63.47/dogsql/member/selectmysql.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_page_1);

        btn = findViewById(R.id.button);
        edtname = findViewById(R.id.editText2);
        txtemail = findViewById(R.id.textemail);
        edtphone = findViewById(R.id.editText3);
        txtpwd = findViewById(R.id.textView25);
        selectin();
        btn.setOnClickListener(btnlistener);
        }
        //上傳
    private Button.OnClickListener btnlistener = new Button.OnClickListener() {
        //public String murl = "http://192.168.1.104/dogsql/member/updatean.php";
        public String murl = "http://120.96.63.47/dogsql/member/updatean.php";
        @Override
        public void onClick(View view) {
            try {


                Bundle peoplepage = getIntent().getExtras();
                String ac =  peoplepage.getString("p_ac");
                String id = peoplepage.getString("p_id");
                String ename = edtname.getText().toString();
                String ephone = edtphone.getText().toString();

                if("".equals(edtname.getText().toString()))
                {
                    Toast.makeText(people_page_1.this, "此欄位不得為空白，請輸入使用者名稱",Toast.LENGTH_LONG).show();
                    return;
                }
                if(edtname.getText().length()>10 | edtname.getText().length()<2){
                    Toast.makeText(people_page_1.this, "字數介於3~10個字之間，請重新輸入",Toast.LENGTH_LONG).show();
                    return;
                }
                if("".equals(edtphone.getText().toString()))
                {
                    Toast.makeText(people_page_1.this, "此欄位不得為空白，請輸入手機號碼",Toast.LENGTH_LONG).show();
                    return;
                }
                if(edtphone.getText().length()>10){
                    Toast.makeText(people_page_1.this, "號碼不得超過10碼",Toast.LENGTH_LONG).show();
                    return;
                }


                HttpClient client = new DefaultHttpClient();
                URI website = new URI(this.murl);
                HttpPost request = new HttpPost();

                List params = new ArrayList();
                params.add(new BasicNameValuePair("myid", id));
                params.add(new BasicNameValuePair("myname", ename));
                params.add(new BasicNameValuePair("myphone", ephone));


                UrlEncodedFormEntity env = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                request.setURI(website);
                request.setEntity(env);
                HttpResponse response = client.execute(request);
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String msg = EntityUtils.toString(resEntity);
                    //拆字串 回傳值 登入成功/id
                    //[0]==登入成功
                    //[1] == 該帳號id
                    if("1".equals(msg.toString())){
                        Toast.makeText(people_page_1.this, "修改成功".toString(),Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(people_page_1.this,"登入失敗".toString(),Toast.LENGTH_LONG).show();
                    }
                } else {
                    String msg = "無資料";
                    //txt.setText(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private void selectin(){
        try {
            Bundle peoplepage = getIntent().getExtras();
            String ac =  peoplepage.getString("p_ac");
            String id = peoplepage.getString("p_id");

            HttpClient client = new DefaultHttpClient();
            URI website = new URI(this.murl);
            HttpPost request = new HttpPost();

            List params = new ArrayList();
            params.add(new BasicNameValuePair("myac", ac));
            params.add(new BasicNameValuePair("myid", id));
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
                data1 =json_data.getString("p_name");
                data2 =json_data.getString("p_phone");
                data3 =json_data.getString("p_email");
                data4 =json_data.getString("p_pwd");

                i++;
            }
            edtname.setText(data1);
            edtphone.setText(data2);
            txtemail.setText(data3);
            txtpwd.setText(data4);

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

}
