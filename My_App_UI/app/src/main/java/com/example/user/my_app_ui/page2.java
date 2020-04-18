package com.example.user.my_app_ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class page2 extends AppCompatActivity {
    private Button btn;
    private TextView txt;
    private TextView txt1;
    private TextView txt2;
    private EditText edtname;
    private EditText edtphone;
    private EditText edtemail;
    private EditText edtpwd;
    private EditText edtrepwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);

        btn = findViewById(R.id.button);
        txt = findViewById(R.id.f_y);
        txt1 = findViewById(R.id.textView17);
        txt2 = findViewById(R.id.test_2);
        edtname =findViewById(R.id.editText);
        edtphone =findViewById(R.id.editText2);
        edtemail =findViewById(R.id.editText5);
        edtpwd =findViewById(R.id.editText13);
        edtrepwd =findViewById(R.id.editText11);

        btn.setOnClickListener(btnlistener);

    }
    //"下一步"頁面跳轉
    private Button.OnClickListener btnlistener = new Button.OnClickListener() {
        String a="";
        //String murl = "http://192.168.1.104/dogsql/email_Comparison.php";
        //學校
        String murl = "http://120.96.63.47/dogsql/email_Comparison.php";
        @Override
        public void onClick(View view) {
            if("".equals(edtname.getText().toString()))
            {
                Toast.makeText(page2.this, "此欄位不得為空白，請輸入使用者名稱",Toast.LENGTH_LONG).show();
                return;
            }
            if(edtname.getText().length()>10 | edtname.getText().length()<2){
                Toast.makeText(page2.this, "字數介於2~10個字之間，請重新輸入",Toast.LENGTH_LONG).show();
                return;
            }
            if("".equals(edtphone.getText().toString()))
            {
                Toast.makeText(page2.this, "此欄位不得為空白，請輸入手機號碼",Toast.LENGTH_LONG).show();
                return;
            }
            if(edtphone.getText().length()>10){
                Toast.makeText(page2.this, "號碼不得超過10碼",Toast.LENGTH_LONG).show();
                return;
            }
            //=====信箱驗證是否註冊過======
            //信箱欄位(空白)
            if("".equals(edtemail.getText().toString()))
            {
                Toast.makeText(page2.this, "此欄位不得為空白，請輸入電子信箱",Toast.LENGTH_LONG).show();
                return;
            }
            //信箱註冊字數不得超過30
            if(edtemail.getText().length()>30){
                Toast.makeText(page2.this, "字數不得超過30字",Toast.LENGTH_LONG).show();
                return;
            }
            //驗證註冊
            try {
                String ac = edtemail.getText().toString();

                HttpClient client = new DefaultHttpClient();
                URI website = new URI(murl);
                HttpPost request = new HttpPost();
                List params = new ArrayList();
                params.add(new BasicNameValuePair("myac", ac));

                UrlEncodedFormEntity env = new UrlEncodedFormEntity (params, HTTP.UTF_8);
                request.setURI(website);
                request.setEntity(env);
                HttpResponse response = client.execute(request);
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String msg = EntityUtils.toString(resEntity);
                    if("帳號已被註冊過".equalsIgnoreCase(msg.toString())){
                        Toast.makeText(page2.this, msg.toString(),Toast.LENGTH_LONG).show();
                        return;
                     }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }

            //密碼欄位(空白)
            if("".equals(edtpwd.getText().toString()))
            {
                Toast.makeText(page2.this, "此欄位不得為空白，請輸入密碼",Toast.LENGTH_LONG).show();
                return;
            }
            //密碼欄位(字數)
            if(edtpwd.getText().length()>15 && edtpwd.getText().length()<3 )
            {
                Toast.makeText(page2.this, "字數介於3~15字之間，請輸入密碼",Toast.LENGTH_LONG).show();
                return;
            }
            //比對密碼輸入是否一樣
            if(edtrepwd.getText().toString().equals(edtpwd.getText().toString())) {
                Intent intent = new Intent();
                intent.setClass(page2.this,page3.class);//資料傳輸設定
                Bundle bundle = new Bundle();
                //儲存資料　第一個為參數key，第二個為Value
                bundle.putString("p_name",edtname.getText().toString());
                bundle.putString("p_phone",edtphone.getText().toString());
                bundle.putString("p_mail",edtemail.getText().toString());
                bundle.putString("p_pwd",edtpwd.getText().toString());
                //put進去傳輸資料
                intent.putExtras(bundle);
                startActivity(intent);
                }else{
                Toast.makeText(page2.this, "輸入錯誤，請再次輸入密碼",Toast.LENGTH_LONG).show();
                return;
            }
        }
    };

}
