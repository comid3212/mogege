package com.example.user.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class JSONDatas implements Serializable {
    JSONArray array;
    JSONDatas(JSONArray array) { this.array = array; }
}

public class Malingering_Detail extends AppCompatActivity {
    private  String cookie;
    private TextView test100,test101;
    ListView listview100;
    private myHandler handler = new myHandler(this);
    private ListAdapter listAdapter;

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    static class MyAdapter extends BaseAdapter {

        Context context;
        List<RequestInfo> infoList;

        static class RequestInfo {
            String punnm, createTime, endDate, mainContent;
            int tranStatus;
        }

        public MyAdapter(Context context, List<RequestInfo> infoList){
            this.context = context;
            this.infoList = infoList;
        }

        @Override
        public int getCount() {
            return infoList.size();
        }

        @Override
        public Object getItem(int i) {
            return infoList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {//畫面要顯示什麼
            if(view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.sick_table, null);//引用 sick_table的格式
            }

            if(view.getTag() != null && Integer.parseInt(view.getTag().toString()) == i) {
                return view;
            }

            TextView
                name = view.findViewById(R.id.name),
                startTime = view.findViewById(R.id.startTime),
                endDate = view.findViewById(R.id.endDate),
                cause = view.findViewById(R.id.cause);

            RequestInfo info = infoList.get(i);

            if(info.tranStatus == 70){ //pass
                view.setBackgroundColor(-6232416);
            } else if (info.tranStatus == 10) { //wait
                view.setBackgroundColor(-1319524);
            } else {
                view.setBackgroundColor(-1268002);
            }
            name.setText(info.punnm);
            startTime.setText(info.createTime);
            endDate.setText(info.endDate);
            cause.setText(info.mainContent);

            return view;
        }
    }

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
                    JSONDatas jsonDatas = (JSONDatas)bundle.getSerializable("JSON");//網頁提供JSON格式
                    JSONArray array = jsonDatas.array;
                    List<MyAdapter.RequestInfo> infoList = new ArrayList<>();
                    for(int i = 0; i < array.length(); ++i) {
                        try {
                            MyAdapter.RequestInfo info = new MyAdapter.RequestInfo();
                            JSONObject object   = (JSONObject) array.get(i);
                            info.createTime   = object.getString("createTime").split("T")[0];
                            info.endDate      = object.getString("endDate").split("T")[0];
                            info.mainContent  = object.getString("mainContent");
                            info.punnm        = object.getString("punnm");
                            info.tranStatus      = object.getInt("tranStatus");
                            infoList.add(info);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    MyAdapter adapter;
                    adapter = new MyAdapter(reference.get() , infoList);
                    listview100.setAdapter(adapter);
                    listview100.setOnItemClickListener(new AdapterView.OnItemClickListener() {//點擊LISTVIEW跳出視窗顯示詳細資訊
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            MyAdapter.RequestInfo info = ((MyAdapter.RequestInfo)listview100.getAdapter().getItem(i));
                            AlertDialog.Builder builder = new AlertDialog.Builder(Malingering_Detail.this);
                            builder.setTitle(info.punnm);
                            builder.setMessage(info.mainContent);
                            builder.show();
                        }
                    });


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
        final String info = bundle.getString("apple");
        test100.setText(info);
        listview100 =(ListView) findViewById(R.id.listview100) ;
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
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

                    Date currentTime = Calendar.getInstance().getTime();
                    CharSequence today = DateFormat.format("yyyy/MM/dd", currentTime);//取得目前時間

                    connect.getOutputStream().write(("{\"leaveType\":0,\"studentID\":\"" + info.split("\"")[2] + "\",\"startDate\":\"2012-02-25T16:00:00.000Z\",\"endDate\":\"" + today.toString() + "\",\"approveType\":0}").getBytes());

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
