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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btn;
    private Button btn2;
    private Button btn3;
    private TextView txt;
    private EditText edtac;
    private EditText edtpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.button);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        txt =findViewById(R.id.test_3);
        edtac =findViewById(R.id.editText);
        edtpwd =findViewById(R.id.editText2);

        btn.setOnClickListener(btnlistener);
        btn2.setOnClickListener(btnlistener2);
        btn3.setOnClickListener(btnlistener3);

    }
    //登入
    private Button.OnClickListener btnlistener = new Button.OnClickListener() {
        //String murl = "http://192.168.1.104/dogsql/Sign_in.php";
        //學校
        String murl = "http://120.96.63.47/dogsql/Sign_in.php";
        @Override
        public void onClick(View view) {
            try {
                String ac = edtac.getText().toString();
                String pwd = edtpwd.getText().toString();

                if("".equals(edtac.getText().toString()))
                {
                    Toast.makeText(MainActivity.this, "不得為空白,請輸入帳號,電子信箱",Toast.LENGTH_LONG).show();
                    return;
                }
                if("".equals(edtpwd.getText().toString()))
                {
                    Toast.makeText(MainActivity.this, "不得為空白,請輸入密碼",Toast.LENGTH_LONG).show();
                    return;
                }

                HttpClient client = new DefaultHttpClient();
                URI website = new URI(murl);
                HttpPost request = new HttpPost();

                List params = new ArrayList();
                params.add(new BasicNameValuePair("myac", ac));
                params.add(new BasicNameValuePair("mypwd", pwd));

                UrlEncodedFormEntity env = new UrlEncodedFormEntity (params, HTTP.UTF_8);
                request.setURI(website);
                request.setEntity(env);
                HttpResponse response = client.execute(request);
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String msg = EntityUtils.toString(resEntity);
                    //拆字串 回傳值 登入成功/id
                    //[0]==登入成功
                    //[1] == 該帳號id
                    String[] array = msg.split("/");
                    if("登入成功".equals(array[0].toString())){
                        Toast.makeText(MainActivity.this, array[0].toString(),Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this , homepage.class);
                        Bundle bundle = new Bundle();
                        //儲存資料　第一個為參數key，第二個為Value
                        bundle.putString("p_ac",edtac.getText().toString());
                        bundle.putString("p_id",array[1].toString());
                        //put進去傳輸資料
                        intent.putExtras(bundle);
                        startActivity(intent);
                        MainActivity.this.finish();
                    }else {
                        Toast.makeText(MainActivity.this,"登入失敗".toString(),Toast.LENGTH_LONG).show();
                        txt.setText("登入失敗");
                    }
                } else {
                    String msg = "無資料";
                    //txt.setText(msg);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    //註冊
    private Button.OnClickListener btnlistener2 = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, page2.class);
            startActivity(intent);
        }
    };
    //訪客
    private Button.OnClickListener btnlistener3 = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this , page4.class);
            startActivity(intent);
        }
    };

}
