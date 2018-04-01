package com.example.user.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

class JSONDatas implements Serializable {
    JSONArray array;
    JSONDatas(JSONArray array) { this.array = array; }
}

public class Malingering_Detail extends AppCompatActivity {
    private  String cookie;
    private TextView test100;

    private myHandler handler = new myHandler(this);
    class myHandler extends Handler {
        private WeakReference<Activity> reference;

        myHandler(Activity activity) {
            reference = new WeakReference<Activity>(activity);
        }

        @Override

        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    Bundle bundle = new Bundle();
                    bundle = msg.getData();
                    //test100.setText("");
                    JSONDatas jsonDatas = (JSONDatas)bundle.getSerializable("JSON");
                    JSONArray array = jsonDatas.array;
                    for(int i = 0; i < array.length(); ++i) {
                        try {
                            JSONObject object   = (JSONObject) array.get(i);
                            String createTime   = object.getString("createTime").split("T")[0];
                            String endDate      = object.getString("endDate").split("T")[0];
                            String mainContent  = object.getString("mainContent");
                            String punnm        = object.getString("punnm");
                            int tranStatus      = object.getInt("tranStatus");
                           // test100.append(punnm + "\t" + createTime + "\t" + endDate + "\t" + mainContent + "\t" + ((tranStatus == 80) ? "莓過" : "過") + '\n');

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    break;
                case 1:
                    AlertDialog.Builder builder = new AlertDialog.Builder(reference.get());
                    builder.setTitle(msg.getData().getString("TITLE"));
                    builder.setMessage(msg.getData().getString("MESSAGE"));
                    builder.setPositiveButton("OK", null);
                    builder.show();

                    break;
                case 2:
                    Intent intent = new Intent();
                    intent.setClass(reference.get(), Malingering_Detail.class);

                    //new一個Bundle物件，並將要傳遞的資料傳入

                    //將Bundle物件assign給intent
                    intent.putExtras(msg.getData());

                    //切換Activity
                    reference.get().startActivity(intent);


                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_malingering__detail);
        cookie = CookieManager.getInstance().getCookie("http://140.128.78.77/");
        test100 =(TextView)findViewById(R.id.textView100) ;
        final Bundle bundle = this.getIntent().getExtras();
        test100.setText(bundle.getString("apple"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connect = null;
                try {
                    //{"leaveType":0,"studentID":"3A417104","startDate":"2016-02-25T16:00:00.000Z","endDate":"2018/06/24","approveType":0}
                    connect = (HttpURLConnection) (new URL("http://stdl.ncut.edu.tw/StdL/WebApi/LeaveData/GetLeaveList")).openConnection();
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

                    connect.getOutputStream().write("{\"leaveType\":0,\"studentID\":\"3A417100\",\"startDate\":\"2012-02-25T16:00:00.000Z\",\"endDate\":\"2018/06/24\",\"approveType\":0}".getBytes());

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
