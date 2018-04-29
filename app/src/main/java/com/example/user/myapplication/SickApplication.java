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
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class SimplePair<T, S> implements Serializable{//將要存的資料實作成Serializable
    public T first;//課程時間
    public S second;//CHECKBOX狀態
    public SimplePair(T t, S s) {
        first = t; second = s;
    }
}

class MySimpleAdapter extends SimpleAdapter {

    private List<List<SimplePair<String, Boolean>>> checkBoxState = new ArrayList<>();

    public MySimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to, List<JSONArray> classHours) { // 建構子
        super(context, data, resource, from, to);

        for(int i = 0; i < classHours.size(); ++i) { //時段內有幾門課
            List<SimplePair<String, Boolean>> list = new ArrayList<>();
            JSONArray array = classHours.get(i);
            for(int o = 0; o < array.length(); ++o) {//該門課有幾節
                JSONObject object = null;
                try {
                    object = array.getJSONObject(o);
                    String hour = object.getString("hour");
                    list.add(new SimplePair<>(hour, true));//CHECK預設為勾選狀態
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            checkBoxState.add(list);
        }
    }

    public List<SimplePair<String, Boolean>> getCheckBoxStatue(int i) {
        return checkBoxState.get(i);
    }

    public void setCheckBoxState(List<SimplePair<String, Boolean>> list, int i) {
        checkBoxState.set(i, list);
    }

    public List<List<SimplePair<String, Boolean>>> getCheckBoxState() {
        return checkBoxState;
    }
}
class DateData implements Serializable {
    int year, month, day;
    public DateData(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
}
class PageData implements Serializable {
    List<List<SimplePair<String, Boolean>>> data;
    DateData startDate, endDate;
    public PageData(List<List<SimplePair<String, Boolean>>> data, DateData startDate, DateData endDate) {
        this.data = data;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}

public class SickApplication extends AppCompatActivity {
    private  String cookie;
    private Button btntst;
    private myHandler handler = new myHandler(this);
    int startYear, startMonth, startDay;
    int endYear, endMonth, endDay;
    private TextView startTime;
    private TextView endTime;
    boolean selectFlag = false;

    Runnable getDateData = new Runnable() {
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
                int years = calendar.get(Calendar.YEAR);
                int months = calendar.get(Calendar.MONTH);

                int chinayears =years-1911;
                if (months<9){
                    chinayears -=1 ;
                }
                //送出使用者ID與登入時間取得使用者當天的上課資料
                String sendsickdata = "{\"sId\":\""+id+"\",\"schyy\":\""+String.valueOf(chinayears)+"\",\"smt\":\"2\",\"sdate\":\""+ startYear + "/" + startMonth + "/" + startDay +"\",\"edate\":\""+ endYear + "/" + endMonth + "/" + endDay +"\"}";

                connect.getOutputStream().write(sendsickdata.getBytes());//送出資料給學校網址
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                byte a[] = new byte[1000];
                int len;
                while((len = connect.getInputStream().read(a)) > 0) { //並取得回傳值
                    os.write(a, 0, len);
                }

                String json = new String(os.toByteArray());

                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("JSON", json);
                msg.arg1 = 0;
                msg.setData(bundle);
                handler.sendMessage(msg);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    static class myHandler extends Handler {
        private WeakReference<SickApplication> reference;

        myHandler(SickApplication activity) {
            reference = new WeakReference<>(activity);
        }
        public void handleMessage(Message msg) {
            SickApplication activity = reference.get();
            switch (msg.arg1) {
                case 0:
                    try {
                        String json = msg.getData().getString("JSON");
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray jsonArray = jsonObject.getJSONArray("list"); // 宣告一個Array來接收JSON的回傳值
                        List<Map<String, String>> listData = new ArrayList<>();
                        List<JSONArray> classHours = new ArrayList<>();//

                        for(int i = 0; i < jsonArray.length(); ++i) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            Map<String, String> map = new HashMap<>();
                            map.put("TITLE", object.getString("date").split("T")[0].substring(5) + " " + object.getString("title"));
                            map.put("SUBTITLE", object.getString("roomNm") + " " + object.getString("teacher"));
                            classHours.add(object.getJSONArray("hours"));
                            listData.add(map);
                        }

                        activity.classInfoList.setAdapter(new MySimpleAdapter(
                                activity,
                                listData,
                                android.R.layout.simple_list_item_2,//listview版面設定為兩行
                                new String[] { "TITLE", "SUBTITLE" },//從這兩個地方拿資料
                                new int[] { android.R.id.text1, android.R.id.text2 },//將資料放在指定位置
                                classHours)
                        );
                    } catch (JSONException e) {
                        activity.classInfoList.setAdapter(new MySimpleAdapter(
                                activity,
                                new ArrayList<Map<String, ?>>(),
                                android.R.layout.simple_list_item_2,//listview版面設定為兩行
                                new String[] { "TITLE", "SUBTITLE" },//從這兩個地方拿資料
                                new int[] { android.R.id.text1, android.R.id.text2 },//將資料放在指定位置
                                new ArrayList<JSONArray>())
                        );
                    }
                    break;
                case 1:
                    if(activity.selectFlag) {
                        activity.calander_view.choiseDate(activity.startYear, activity.startMonth, activity.startDay);
                    } else {
                        activity.calander_view.choiseDate(activity.endYear, activity.endMonth, activity.endDay);
                    }
                    break;
            }
        }
    }

    public void onNextPageClicked(View view) {
        List<List<SimplePair<String, Boolean>>> data = ((MySimpleAdapter) classInfoList.getAdapter()).getCheckBoxState();//呼叫假爸爸並取得請假資料
        if(data.size() == 0) {//如果課堂為0
            Toast.makeText(this, "請假堂數為空", Toast.LENGTH_SHORT).show();
        } else {
            PageData pageData = new PageData(data, new DateData(startYear, startMonth, startDay), new DateData(endYear, endMonth, endDay));
            Intent intent = new Intent(this, SickCause.class);
            Bundle bundle = this.getIntent().getExtras();
            bundle.putSerializable("SickApplicationData", pageData);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public void datStart(View view) {
        Message msg = new Message();
        msg.arg1 = 1;
        Bundle bundle = new Bundle();
        msg.setData(bundle);
        handler.sendMessage(msg);
        selectFlag = true;
    }

    public void datEnd(View view) {
        Message msg = new Message();
        msg.arg1 = 1;
        Bundle bundle = new Bundle();
        msg.setData(bundle);
        handler.sendMessage(msg);
        selectFlag = false;
    }

    Calander_view calander_view;

    String id;
    String name;
    ListView classInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sick_application);
        cookie = CookieManager.getInstance().getCookie("http://140.128.78.77/");
        btntst = (Button)findViewById(R.id.button15);
        final Bundle bundle = this.getIntent().getExtras();
        id = bundle.getString("id");
        name = bundle.getString("name");
        Calendar calendar = Calendar.getInstance();
        startYear = endYear = calendar.get(Calendar.YEAR);
        startMonth = endMonth = calendar.get(Calendar.MONTH) + 1;
        startDay = endDay = calendar.get(Calendar.DAY_OF_MONTH);
        startTime = findViewById(R.id.startDay);
        endTime = findViewById(R.id.endDay);
        classInfoList = findViewById(R.id.classInfoList);
        startTime.setEnabled(false);
        endTime.setEnabled(false);

        startTime.setText(startYear + "/" + startMonth + "/" + startDay);
        endTime.setText(endYear + "/" + endMonth + "/" + endDay);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_calander_view, null);//拿到calander_view
        calander_view = view.findViewById(R.id.calendarView);
        calander_view.setCallback(new Calander_view.Callback() {//拿到Calander_view使用者選取的日期
            @Override
            public void callback(int year, int month, int day) {
                if(selectFlag) {//用flag表示,if true等於1 ,代表選擇的是起始日期,反之
                    startYear = year;
                    startMonth = month;
                    startDay = day;
                    startTime.setText(year + "/" + month + "/" + day);
                } else {
                    endYear = year;
                    endMonth = month;
                    endDay = day;
                    endTime.setText(year + "/" + month + "/" + day);
                }
                new Thread(getDateData).start();
            }
        });

        new Thread(getDateData).start();

        classInfoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {//當listview被點擊
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int o, long l) {
                final MySimpleAdapter mySimpleAdapter = (MySimpleAdapter) classInfoList.getAdapter();
                AlertDialog.Builder builder = new AlertDialog.Builder(SickApplication.this);
                builder.setTitle(((TextView)view.findViewById(android.R.id.text1)).getText().toString());
                LinearLayout layout = new LinearLayout(SickApplication.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                builder.setView(layout);
                final List<SimplePair<String, Boolean>> statue = mySimpleAdapter.getCheckBoxStatue(o);//checkbox的回傳值
                for(int i = 0; i < statue.size(); ++i) {
                    CheckBox checkBox = new CheckBox(SickApplication.this);
                    final SimplePair<String, Boolean> s = statue.get(i);
                    checkBox.setText(s.first);//設定課程時間
                    checkBox.setChecked(s.second);//勾選狀態
                    layout.addView(checkBox);//新增checkbox
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {//當checkbox被改變
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            s.second = b;//checkbox就會等於使用者更改的結果
                        }
                    });
                }
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {//當dialog被關閉
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        mySimpleAdapter.setCheckBoxState(statue, o);//儲存checkbox的狀態
                    }
                });
                builder.show();//顯示結果'
            }
        });
    }
}
