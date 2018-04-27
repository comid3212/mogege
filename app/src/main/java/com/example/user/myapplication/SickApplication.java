package com.example.user.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.style.TtsSpan;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.AdapterView;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SickApplication extends AppCompatActivity {
    private  String cookie;
    private Button btntst;
    private myHandler handler = new myHandler(this);
    class myHandler extends Handler {
        private WeakReference<Activity> reference;

        myHandler(Activity activity) {
            reference = new WeakReference<Activity>(activity);
        }
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:



                    break;
                case 1:
                    AlertDialog.Builder builder = new AlertDialog.Builder(reference.get());
                    builder.setTitle("gg");
                    builder.setMessage("gg2");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                    break;
                case 2:
                    /*Intent intent = new Intent();
                    intent.setClass(reference.get(), Malingering_Detail.class);

                    //new一個Bundle物件，並將要傳遞的資料傳入

                    //將Bundle物件assign給intent
                    intent.putExtras(msg.getData());

                    //切換Activity
                    reference.get().startActivity(intent);*/


                    break;
            }
        }
    }

    public void  test(View view){
        Message msg = new Message();
        msg.arg1 = 1;
        Bundle bundle = new Bundle();
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sick_application);
        cookie = CookieManager.getInstance().getCookie("http://140.128.78.77/");
        btntst = (Button)findViewById(R.id.button15);
        final Bundle bundle = this.getIntent().getExtras();
        final String id = bundle.getString("id");
        final String name = bundle.getString("name");

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connect = null;
                try {
                    //{"leaveType":0,"studentID":"3A417104","startDate":"2016-02-25T16:00:00.000Z","endDate":"2018/06/24","approveType":0}
                    connect = (HttpURLConnection) (new URL("http://stdl.ncut.edu.tw/StdL/WebApi/LeaveData/GetTimeTable")).openConnection();
                    Util.setHttpUrlConnection(connect);
                    connect.setRequestProperty("Host", Malingering.HOST);
                    connect.setRequestProperty("Referer", Malingering.Referer);
                    connect.setRequestProperty("Origin", Malingering.Origin);
                    connect.setRequestProperty("User-Agent", Malingering.USER_AGENT);
                    connect.setRequestProperty("Accept", Malingering.ACCEPT);
                    connect.setRequestProperty("Content-Type", Malingering.CONTEXT_TYPE);
                    connect.setRequestProperty("Accept-Encoding", Malingering.ACCEPT_ENCODING);
                    connect.setRequestProperty("Cookie", cookie);
                    connect.setDoOutput(true);
                    connect.setRequestMethod("PUT");
                    Calendar calendar = Calendar.getInstance();
                    Date currentTime = calendar.getTime();
                    int years = calendar.get(Calendar.YEAR);
                    int months = calendar.get(Calendar.MONTH);

                    CharSequence today = DateFormat.format("yyyy/MM/dd", currentTime);//取得目前時間
                    calendar.add(Calendar.DATE, -1);
                    Date yester = calendar.getTime();
                    CharSequence yesterday = DateFormat.format("yyyy/MM/dd", yester);
                    int chinayears =years-1911;
                    if (months<9){
                         chinayears -=1 ;
                    }
                      String sendsickdata = "{\"sId\":\""+id+"\",\"schyy\":\""+String.valueOf(chinayears)+"\",\"smt\":\"2\",\"sdate\":\""+yesterday.toString()+"\",\"edate\":\""+today.toString()+"\"}";

                    connect.getOutputStream().write(sendsickdata.getBytes());
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    byte a[] = new byte[1000];
                    int len;
                    while((len = connect.getInputStream().read(a)) > 0) {
                        os.write(a, 0, len);
                    }
                    String json = new String(os.toByteArray());
                    JSONArray jsonArray = new JSONArray(json);

                    JSONDatas datas = new JSONDatas(jsonArray);
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("JSON", datas);
                    msg.arg1 = 0;
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
