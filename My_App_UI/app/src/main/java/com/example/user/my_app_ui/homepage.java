package com.example.user.my_app_ui;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoaderInterface;

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
import java.util.Timer;
import java.util.TimerTask;

public class homepage extends AppCompatActivity {
    //private static final String TAG = "MainActivity";
    SwipeRefreshLayout mySwipeRefreshLayout;

    private Button btn_people;
    private Button btn_dog;
    private Button btn_dogloss;
    private Button btn_dog_select;
    private Button btn_lost;
    private TextView txt,txtdog,txtlose,txtfilename,txtvariety,txtvar;
    private Banner banner;
    public  class GlideImageLoader extends com.youth.banner.loader.ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        btn_people = findViewById(R.id.button);
        btn_dog = findViewById(R.id.button5);
        btn_dogloss = findViewById(R.id.button2);
        btn_dog_select = findViewById(R.id.button3);
        btn_lost = findViewById(R.id.button9);
        txt=findViewById(R.id.textView2);
        txtdog =findViewById(R.id.textView3);
        txtlose =findViewById(R.id.textView4);
        txtfilename = findViewById(R.id.textView36);
        txtvariety = findViewById(R.id.textView37);
        txtvar = findViewById(R.id.textView50);
        banner =findViewById(R.id.banner);

        btn_people.setOnClickListener(btnlistener);
        btn_dog.setOnClickListener(btnlistener2);
        btn_dogloss.setOnClickListener(btnlistener3);
        btn_dog_select.setOnClickListener(btnlistener4);
        btn_lost.setOnClickListener(btnlistener5);
        mySwipeRefreshLayout =  findViewById(R.id.swiperefresh);
        getbundle();

        mySwipeRefreshLayout.setOnRefreshListener(swiplistener);
        Toast.makeText(homepage.this, "向下滑動已刷新資料", Toast.LENGTH_SHORT).show();


    }
    //{Home}圖片輪播
    public void imgrand(){
        String fileuri = "http://120.96.63.47/dogsql/12354.php";
        String variety = txtvariety.getText().toString();
        String file =txtfilename.getText().toString();
        /*if ("柯基".equals(variety)){
            variety = "corgi";
        }else if("哈士奇".equals(variety)){
            variety ="husky";
        }else{
            variety ="shibainu";
        }*/
        try {
            HttpClient client = new DefaultHttpClient();
            URI website = new URI(fileuri);
            HttpPost request = new HttpPost();
            List params = new ArrayList();
            params.add(new BasicNameValuePair("myvar", variety));
            params.add(new BasicNameValuePair("myfile", file));
            UrlEncodedFormEntity env = new UrlEncodedFormEntity (params, HTTP.UTF_8);
            request.setURI(website);
            request.setEntity(env);
            HttpResponse response = client.execute(request);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                String msg = EntityUtils.toString(resEntity);
                Log.d("Nopemsg", msg.toString());
                String[] imgname = msg.split("/");
                banner.setImageLoader(new GlideImageLoader());
                List<String> images = new ArrayList<>();
                for(int i = 1; i < imgname.length; i++){
                    images.add("http://120.96.63.47/docker/third_who_dog/"+variety+"/tf_files/train/"+file+"/"+imgname[i]);
                }
                banner.setImages(images).setDelayTime(3000);
                banner.start();
            }
        }catch  (Exception e){

        }

    }

    //{Home_2}監聽向下滑動刷新
    private SwipeRefreshLayout.OnRefreshListener swiplistener = new SwipeRefreshLayout.OnRefreshListener() {
        String a = "0";
        @Override
        public void onRefresh() {
            myUpdateOperation();
            Toast.makeText(homepage.this, "向下滑動已刷新資料", Toast.LENGTH_SHORT).show();
            // 指定 progress animation 的顏色, 似乎沒有數量限制,
            // 顏色會依順序循環播放
            mySwipeRefreshLayout.setColorSchemeResources(
                    android.R.color.holo_red_light,
                    android.R.color.holo_blue_light,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_purple);
        }
    };
    //{Home_3}向下滑動刷新秒數
    private void myUpdateOperation(){
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                getbundle();
                mySwipeRefreshLayout.setRefreshing(false);
            }
        }, 3500);    // 3 秒
    }
    //{Home_1}接值轉資料
    public void getbundle(){
        Bundle bundle = getIntent().getExtras();
        String ac = bundle.getString("p_ac");
        String id = bundle.getString("p_id");
        //帳號 & id
        String uriname = "http://120.96.63.47/dogsql/homepage.php";
        try {



            HttpClient client = new DefaultHttpClient();
            URI website = new URI(uriname);
            HttpPost request = new HttpPost();
            List params = new ArrayList();
            params.add(new BasicNameValuePair("pid", id));
            params.add(new BasicNameValuePair("ac", ac));
            UrlEncodedFormEntity env = new UrlEncodedFormEntity (params, HTTP.UTF_8);
            request.setURI(website);
            request.setEntity(env);
            HttpResponse response = client.execute(request);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                String msg = EntityUtils.toString(resEntity);
                //Toast.makeText(homepage.this, msg.toString(), Toast.LENGTH_SHORT).show();
                String[] array = msg.split("/");
                //txt.setText(array[0].toString());
                txtdog.setText(array[1].toString()+"隻");
                txtlose.setText(array[2].toString()+"隻");
                txtfilename.setText(array[3].toString());
                txtvariety.setText(array[4].toString());
                txtvar.setText(array[5].toString());
                txt.setText(array[6].toString());

                imgrand();
            }
        }catch  (Exception e){
        }

    }



    //{Button}個人資料
    private Button.OnClickListener btnlistener = new Button.OnClickListener() {
        String a = "";
        @Override
        public void onClick(View view) {
            Bundle bundle = getIntent().getExtras();
            String ac = bundle.getString("p_ac");
            String id = bundle.getString("p_id");
            Intent intent = new Intent();
            intent.setClass(homepage.this , people_page_1.class);
            Bundle peoplepage = new Bundle();
            peoplepage.putString("p_ac",ac);
            peoplepage.putString("p_id",id);
            intent.putExtras(peoplepage);
            startActivity(intent);
        }
    };
    //{Button}狗狗資料
    private Button.OnClickListener btnlistener2 = new Button.OnClickListener() {
        String a = "";
        @Override
        public void onClick(View view) {
            Bundle bundle = getIntent().getExtras();
            String ac = bundle.getString("p_ac");
            String id = bundle.getString("p_id");
            Intent intent = new Intent();
            intent.setClass(homepage.this , dog_page_1.class);
            Bundle dogpage = new Bundle();
            dogpage.putString("p_ac",ac);
            dogpage.putString("p_id",id);
            intent.putExtras(dogpage);
            startActivity(intent);
            }
    };
    //{Button}狗狗掛失
    private Button.OnClickListener btnlistener3 = new Button.OnClickListener() {
        String a = "";
        @Override
        public void onClick(View view) {
            Bundle bundle = getIntent().getExtras();
            String ac = bundle.getString("p_ac");
            String id = bundle.getString("p_id");
            Intent intent = new Intent();
            intent.setClass(homepage.this , dogloss_page_1.class);
            Bundle dogloss = new Bundle();
            dogloss.putString("p_ac",ac);
            dogloss.putString("p_id",id);
            intent.putExtras(dogloss);
            startActivity(intent);
        }
    };
    //{Button}遺失通知
    private Button.OnClickListener btnlistener4 = new Button.OnClickListener() {
        String a = "";
        @Override
        public void onClick(View view) {
            Bundle bundle = getIntent().getExtras();
            String ac = bundle.getString("p_ac");
            String id = bundle.getString("p_id");
            Intent intent = new Intent();
            intent.setClass(homepage.this , dog_select_1.class);
            Bundle dogselect = new Bundle();
            dogselect.putString("p_ac",ac);
            dogselect.putString("p_id",id);
            intent.putExtras(dogselect);
            startActivity(intent);
        }
    };
    //{Button}遺失找尋
    private Button.OnClickListener btnlistener5 = new Button.OnClickListener() {
        String a = "";
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(homepage.this , page4.class);
            startActivity(intent);
        }
    };

    private static Boolean isExit = false;
    private static Boolean hasTask = false;
    Timer timerExit = new Timer();
    TimerTask task = new TimerTask() {
        String a ="";
        @Override
        public void run() {
            isExit = false;
            hasTask = false;
        }
    };
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(isExit == false ) {
                isExit = true;
                Toast.makeText(this, "再按一次Back退出APP", Toast.LENGTH_SHORT).show();
                if(!hasTask) {
                    timerExit.schedule(task, 2000);
                }
            }
            else{
                finish();
                System.exit(0);
            }
        }
        return false;
    }

}
