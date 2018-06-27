package com.example.user.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

class JSONDatas implements Serializable {
    JSONArray array;
    JSONDatas(JSONArray array) { this.array = array; }
}

public class Malingering_Detail extends AppCompatActivity {
    public static final int PASS_CODE = 70;
    public static final int WAIT_CODE = 10;
    private  String cookie;
    private TextView test100,test102;
    ListView listview100;
    private myHandler handler = new myHandler(this);
    private String stdId;

    Runnable getMalingerData = new Runnable() {
        @Override
        public void run() {
            try {
                //{"leaveType":0,"studentID":"3A417104","startDate":"2016-02-25T16:00:00.000Z","endDate":"2018/06/24","approveType":0}
                HttpURLConnection connect = (HttpURLConnection) (new URL("http://stdl.ncut.edu.tw/StdL/WebApi/LeaveData/GetLeaveList")).openConnection();
                configPutConnection(connect);
                Date currentTime = Calendar.getInstance().getTime();
                CharSequence today = DateFormat.format("yyyy/MM/dd", currentTime);//取得目前時間
                connect.getOutputStream().write(("{\"leaveType\":0,\"studentID\":\"" +  stdId + "\",\"startDate\":\"2012-02-25T16:00:00.000Z\",\"endDate\":\"2020-02-25T16:00:00.000Z\",\"approveType\":0}").getBytes());//

                ByteArrayOutputStream os = new ByteArrayOutputStream();
                byte a[] = new byte[1000];
                int len;
                while((len = connect.getInputStream().read(a)) > 0) {//將JSON拿到的資料寫進os裡面
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

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public void  Application(View view){
        Message msg = new Message();
        msg.arg1 = 2;
        Bundle bundle = new Bundle();
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

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
            String punnm, createTime, endDate, mainContent,comment;
            int tranStatus, infoId;
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
        }//拿到infolist的東西

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {//畫面要顯示什麼
            if(view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.sick_table, null);//引用 sick_table的格式
            }


            TextView
                name = view.findViewById(R.id.name),
                startTime = view.findViewById(R.id.startTime),
                endDate = view.findViewById(R.id.endDate),
                cause = view.findViewById(R.id.cause);

            RequestInfo info = infoList.get(i);

            if(info.tranStatus == PASS_CODE){ //pass
                view.setBackgroundColor(-6232416);
            } else if (info.tranStatus == WAIT_CODE) { //wait
                view.setBackgroundColor(-1319524);
            } else {
                view.setBackgroundColor(-1268002);
            }
            name.setText(info.punnm); //顯示結果
            startTime.setText(info.createTime);
            endDate.setText(info.endDate);
            cause.setText(info.mainContent);


            return view;
        }
    }

    static class myHandler extends Handler {
        private WeakReference<Malingering_Detail> reference;

        myHandler(Malingering_Detail activity) {
            reference = new WeakReference<Malingering_Detail>(activity);
        }

        @Override

        public void handleMessage(Message msg) {
            final Malingering_Detail activity = reference.get();
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
                            info.createTime   = object.getString("createTime").split("T")[0];//起始日期
                            info.endDate      = object.getString("endDate").split("T")[0];//結束日期
                            info.mainContent  = object.getString("mainContent");//原因
                            info.punnm        = object.getString("punnm");//假別
                            info.tranStatus   = object.getInt("tranStatus");//是否通過
                            info.infoId       = object .getInt("documentId");//請假id
                            info.comment       = object .getString("comment");//請假理由
                            infoList.add(info);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    MyAdapter adapter;
                    adapter = new MyAdapter(activity , infoList);
                    activity.listview100.setAdapter(adapter);
                    activity.listview100.setOnItemClickListener(new AdapterView.OnItemClickListener() {//點擊LISTVIEW跳出視窗顯示詳細資訊
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            final MyAdapter.RequestInfo info = ((MyAdapter.RequestInfo)activity.listview100.getAdapter().getItem(i));
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle(info.punnm);
                            if(Objects.equals(info.comment, "null")){
                                builder.setMessage(info.mainContent);
                            } else {
                                builder.setMessage(info.mainContent + " 原因:" + info.comment);
                            }

                            builder.setPositiveButton("確認", null);
                            if(info.tranStatus == WAIT_CODE) {
                                builder.setNegativeButton("刪除此申請", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                        builder.setMessage("確認刪除?");
                                        builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            HttpURLConnection connect = (HttpURLConnection) new URL("http://stdl.ncut.edu.tw/StdL/WebApi/LeaveData/SetAbsenceInfo").openConnection();
                                                            activity.configPutConnection(connect);
                                                            connect.getOutputStream().write(("{\"clerkID\":\"" + activity.stdId + "\",\"list\":[{\"option\":\"2\",\"documentID\":" + info.infoId + ",\"studentID\":\"" + activity.stdId + "\"}]}").getBytes());
                                                            byte a[] = new byte[4];
                                                            int length =connect.getInputStream().read(a);
                                                            Message msg = new Message();
                                                            Bundle bundle = new Bundle();
                                                            bundle.putString("TITLE", "刪除資訊");
                                                            if(length == 4) {
                                                                bundle.putString("MESSAGE", "請假刪除成功");
                                                            } else {
                                                                bundle.putString("MESSAGE", "請假刪除失敗");
                                                            }
                                                            msg.arg1 = 1;
                                                            msg.setData(bundle);
                                                            myHandler.this.sendMessage(msg);
                                                            new Thread(activity.getMalingerData).start();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }}
                                                }).start();
                                            }
                                        });
                                        builder.setNegativeButton("取消", null);
                                        builder.show();
                                    }
                                });
                            }
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
                    intent.setClass(reference.get(), SickApplication.class);
                    intent.putExtras(activity.getIntent().getExtras());

                    //new一個Bundle物件，並將要傳遞的資料傳入

                    //將Bundle物件assign給intent
                    intent.putExtras(msg.getData());

                    //切換Activity
                    reference.get().startActivity(intent);


                    break;
            }
        }
    }

    protected void configPutConnection(HttpURLConnection connect) throws ProtocolException {
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_malingering__detail);
        cookie = CookieManager.getInstance().getCookie("http://140.128.78.77/");
        test100 = findViewById(R.id.textView100);
        test102 = findViewById(R.id.textView26);
        final Bundle bundle = this.getIntent().getExtras();
        stdId = bundle.getString("id");
        final String name = bundle.getString("name");
        test100.setText("學號:"+ stdId +"  "+"名子:"+name);
        listview100 = findViewById(R.id.listview100);

        new Thread(getMalingerData).start();
    }
}
