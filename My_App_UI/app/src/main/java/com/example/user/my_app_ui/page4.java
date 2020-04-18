package com.example.user.my_app_ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class page4 extends AppCompatActivity implements View.OnClickListener {

    private TextView txt;
    private Button uploadButton, carmerabutton,imgButton;
    private ImageView imageview;
    private final int OPEN_RESULT = 1;
    private int serverResponseCode = 0;
    private static ProgressDialog dialog= null;
    private String upLoadServerUri = null;
    private String imagepath=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        txt = findViewById(R.id.textView39);
        setContentView(R.layout.activity_page4);

        uploadButton = (Button)findViewById(R.id.uploadButton);
//        messageText  = (TextView)findViewById(R.id.messageText);
        carmerabutton = (Button)findViewById(R.id.carmeraButton);
        imgButton = findViewById(R.id.imgButton);
        imageview = (ImageView)findViewById(R.id.imageView_pic);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            carmerabutton.setEnabled(false);
            imgButton.setEnabled(false);
            uploadButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        } else {
            carmerabutton.setEnabled(true);
            imgButton.setEnabled(true);
            uploadButton.setEnabled(true);
        }

        carmerabutton.setOnClickListener(this);
        imgButton.setOnClickListener(this);
        uploadButton.setOnClickListener(this);
        upLoadServerUri = "http://120.96.63.47/dogsql/imgpath/single_upload.php";


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                imgButton.setEnabled(true);
                carmerabutton.setEnabled(true);
                uploadButton.setEnabled(true);
            }
        }
    }
    @Override
    public void onClick(View arg0) {
        if(arg0==carmerabutton)
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, OPEN_RESULT);
        }
        else if(arg0==imgButton){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "開始選圖"), 1);
        }
        else if (arg0==uploadButton) {

            dialog = ProgressDialog.show(page4.this, "", "請稍後........", true,false);

//            messageText.setText("上傳中....");

            if(imagepath == null)
            {
                dialog.dismiss();
                Toast.makeText(page4.this, "請選圖",Toast.LENGTH_LONG).show();
                return;
            }
            new Thread(new Runnable() {
                public void run() {
                    uploadFile(imagepath);
                    imgcopy();
                    imgtest();
                }
            }).start();

        }
        Log.d("MSG",String.valueOf(imagepath));

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {
            //Bitmap photo = (Bitmap) data.getData().getPath();
            Uri selectedImageUri = data.getData();
            imagepath = getPath(selectedImageUri);
            Bitmap bitmap= BitmapFactory.decodeFile(imagepath);
            imageview.setImageBitmap(bitmap);

        }
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
//        Cursor cursor = managedQuery(uri, projection, null, null, null);
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    public int uploadFile(String sourceFileUri) {


        String fileName = sourceFileUri;
        String msg = "";

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

            dialog.dismiss();

//            Log.e("UP", "Source File not exist :"+imagepath);

            runOnUiThread(new Runnable() {
                public void run() {
//                    messageText.setText("檔案沒被寫入.JPG :"+ imagepath);
                }
            });

            return 0;

        }
        else
        {
            try {

                // open a URL connection to the Servlet
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
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);

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

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    runOnUiThread(new Runnable() {
                        public void run() {
//                            String msg = "File Upload Completed.\n\n See uploaded file your server. \n\n";
//                            messageText.setText(msg);
                            Toast.makeText(page4.this, "上傳成功", Toast.LENGTH_SHORT).show();//這行會跳顯示
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
//                        messageText.setText("URL");
                        Toast.makeText(page4.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
//                        messageText.setText("抓到錯誤 : 請看LOG ");
                        Toast.makeText(page4.this, "抓到錯誤 : 請看LOG", Toast.LENGTH_SHORT).show();
                    }
                });
                //Log.e("Upload file to server Exception", "Exception : "  + e.getMessage(), e);
            }
//            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }

    public void imgcopy(){
        String murl = "http://120.96.63.47/dogsql/imgpath/move2.php";
        try {
            HttpClient client = new DefaultHttpClient();
            URI website = new URI(murl);
            HttpPost request = new HttpPost();
            request.setURI(website);
            HttpResponse response = client.execute(request);
            HttpEntity resEntity = response.getEntity();
            if(resEntity != null){
                final String msg00 = EntityUtils.toString(resEntity);

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(page4.this,"複製圖片", Toast.LENGTH_SHORT).show();//這行會跳顯示
                    }
                });
            }
        }catch (Exception e) {
            e.printStackTrace();

        }
        //Log.d("GG","YO");

    }
    public void imgtest(){
        String murl = "http://120.96.63.47/dogsql/hello123.php";
        try {
            HttpClient client = new DefaultHttpClient();
            URI website = new URI(murl);
            HttpPost request = new HttpPost();
            request.setURI(website);
            HttpResponse response = client.execute(request);
            HttpEntity resEntity = response.getEntity();
            if(resEntity != null){
                final String msg_1 = EntityUtils.toString(resEntity).trim();
                Log.d("MSG2", msg_1.toString());
                final String [] array = msg_1.split("/");

                runOnUiThread(new Runnable() {
                    public void run() {
                        if("www".equals(array[1])){
                            Log.d("MSG3", "這不是狗");
                            Toast.makeText(page4.this,"這不是狗",Toast.LENGTH_LONG).show();//這沒跳
                            return;
                        }else if("nolost".equals(array[1])){
                            Log.d("MSG3", "沒有遺失");
                            Toast.makeText(page4.this,"目前沒有狗遺失",Toast.LENGTH_LONG).show();//這沒跳
                            return;
                        }else{
                            Log.d("MSG3", "這是狗");
                            Log.d("MSG4", array[1]);
                            Bundle pathad = new Bundle();
                            pathad.putString("path",msg_1);
                            Intent intent4 = new Intent();
                            intent4.setClass(page4.this , page5.class);
                            intent4.putExtras(pathad);
                            startActivity(intent4);
                            page4.this.finish();
                        }
                        Toast.makeText(page4.this,"這是狗", Toast.LENGTH_SHORT).show();//這行會跳顯示
                    }
                });

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("GG","HI");
        dialog.dismiss();

    }


}
