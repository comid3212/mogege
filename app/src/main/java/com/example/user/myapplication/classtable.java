package com.example.user.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
class ClassInformation {
    public List<String> className = new LinkedList<>();
    public List<Integer> classLength = new LinkedList<>();
}

class MyAdapter extends BaseAdapter {
    private Context context;
    private List<ClassInformation> classes;

    public MyAdapter(Context context, List<ClassInformation> classes) {
        this.context = context;
        this.classes = classes;
    }

    @Override
    public int getCount() {
        return classes.size();
    }

    @Override
    public Object getItem(int i) {
        return classes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.class_form, null);
        }

        if(view.getTag() != null && Integer.parseInt(view.getTag().toString()) == i) {
            return view;
        }



        LinearLayout layout = view.findViewById(R.id.classLayout);
        layout.removeAllViews();
        ClassInformation classInformation = classes.get(i);
        for(int o = 0; o < classInformation.classLength.size(); ++o) {
            String name = classInformation.className.get(o);
            Integer length = classInformation.classLength.get(o);
            MyTextView textView = new MyTextView(context);
            textView.setSingleLine(true);
            if(name.length() > 24)
                textView.setText(name.substring(0, 24));
            else
                textView.setText(name);
            textView.setGravity(TextView.TEXT_ALIGNMENT_CENTER);
            if(length != 1) {
                textView.down = false;
                textView.setBackgroundColor(-268383028);
            }
           /* else if(!name.equals("")){
                textView.down = false;
                textView.setBackgroundColor(-268383028);
            }*/
            layout.addView(textView, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,1.0f
            ));
            int index = 1;
            for(int s = 1; s < length; ++s) {
                MyTextView textView1 = new MyTextView(context);
                textView1.setGravity(TextView.TEXT_ALIGNMENT_CENTER);
                textView1.down = false;
                textView1.top = false;
                textView1.setBackgroundColor(-268383028);
                int f = index * 24, e = index * 24 + 24;
                if(e > name.length()) {
                    e = name.length();
                }
                if(f < name.length()) {
                    textView1.setText(name.substring(f, e));
                    index += 1;
                }
                layout.addView(textView1, new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1.0f
                ));
            }
        }
        view.setTag(i);

        return view;
    }
}

public class classtable extends AppCompatActivity {
    private String cookie;
    private myHandler handler = new myHandler(this);
    private HorizontalListView ListView;

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
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
                    break;
                case 1:
                    break;
                case 2:
                    Toast.makeText(reference.get(), msg.getData().getString("MESSAGE"), Toast.LENGTH_LONG).show();
                    reference.get().finish();
                    break;

                case 3:
                    Bundle bundle = msg.getData();
                    int classPerDay = bundle.getInt("ClassPerDay");
                    ArrayList<String> className = bundle.getStringArrayList("ClassName");
                    ArrayList<Integer> classLength = bundle.getIntegerArrayList("ClassLength");
                    List<ClassInformation> classInformations = new LinkedList<>();
                    for(int i = 0; i < 8; ++i) {
                        classInformations.add(new ClassInformation());
                    }
                    classInformations.get(1).className.add("星期一");
                    classInformations.get(1).classLength.add(1);
                    classInformations.get(2).className.add("星期二");
                    classInformations.get(2).classLength.add(1);
                    classInformations.get(3).className.add("星期三");
                    classInformations.get(3).classLength.add(1);
                    classInformations.get(4).className.add("星期四");
                    classInformations.get(4).classLength.add(1);
                    classInformations.get(5).className.add("星期五");
                    classInformations.get(5).classLength.add(1);
                    classInformations.get(6).className.add("星期六");
                    classInformations.get(6).classLength.add(1);
                    classInformations.get(7).className.add("星期日");
                    classInformations.get(7).classLength.add(1);

                    LinearLayout timeLayout = (LinearLayout)findViewById(R.id.timeLayout);
                    timeLayout.removeAllViews();

                    MyTextView textView = new MyTextView(reference.get());
                    timeLayout.addView(textView, new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            1.0f
                    ));

                    for(int i = 0; i < classPerDay; ++i) {
                        MyTextView textView1 = new MyTextView(reference.get());
                        textView1.setText(String.valueOf(i + 1));
                        textView1.setBackgroundColor(Color.argb(150, 238, 236, 166));
                        timeLayout.addView(textView1, new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                1.0f
                        ));
                    }

                    int index = 0;
                    for(int i = 1; i < 8; ++i) {
                        ClassInformation info = classInformations.get(i);
                        for(int nowTime = 0; nowTime < classPerDay; ++index) {
                            info.className.add(className.get(index));
                            info.classLength.add(classLength.get(index));
                            nowTime += classLength.get(index);
                        }
                    }

                    MyAdapter adapter;
                    classInformations.remove(0);
                    adapter = new MyAdapter(reference.get() , classInformations);

                    ListView.setAdapter(adapter);
                    //將ListAdapter設定至ListView裡面
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_classtable);
        ListView = (HorizontalListView) findViewById(R.id.testlist);
        cookie = this.getIntent().getExtras().getString("COOKIE");
        super.onCreate(savedInstanceState);

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
                    connect = (HttpURLConnection) (new URL("http://nmsd.ncut.edu.tw/wbcmss/Query/Schedule")).openConnection();
                    Util.setHttpUrlConnection(connect);
                    Util.setHttpUrlConnectionCookie(connect, cookie);
                    Util.getReader(connect);
                    BufferedReader reader = Util.getReader(connect, "utf-8");
                    StringBuilder all = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null){
                        all.append(line);
                    }
                    Document document = Jsoup.parse(all.toString());
                    Elements timeLine = document.getElementsByClass("time-line");

                    int classPerDay = 16;
                    ArrayList<String> className = new ArrayList<>();
                    ArrayList<Integer> classLength = new ArrayList<>();

                    for(int week = 0; week < timeLine.size(); ++week) {
                        Elements classSet = timeLine.get(week).getElementsByClass("time-line-span col-xs-12");
                        int nowTime = 0;
                        for(int timeIndex = 0; timeIndex < classSet.size(); ++timeIndex) {
                            String top = classSet.get(timeIndex).attr("style").split(":")[1];
                            top = top.split(";")[0];
                            top = top.substring(1, top.length()-2);
                            int time = (int)Double.parseDouble(top);
                            time = (time - 23) / 80;
                            if(nowTime < time) {
                                for(int i  = nowTime; i < time; ++i){
                                    className.add("");
                                    classLength.add(1);
                                }
                            }

                            String name = classSet.get(timeIndex).attr("title");
                            String stimespan = classSet.get(timeIndex).getElementsByClass("line-span").get(0).attr("style").split(":")[1];
                            stimespan = stimespan.split(";")[0];
                            stimespan = stimespan.substring(1, stimespan.length() - 2);
                            int timespan = (int)Double.parseDouble(stimespan);
                            timespan = (timespan - 19) / 80;
                            className.add(name);
                            classLength.add(timespan + 1);
                            nowTime = time + timespan + 1;
                        }
                        for(int i = nowTime; i < classPerDay; ++i) {
                            className.add("");
                            classLength.add(1);
                        }
                    }

                    Message msg = new Message();
                    msg.arg1 = 3;
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("ClassName", className);
                    bundle.putIntegerArrayList("ClassLength", classLength);
                    bundle.putInt("ClassPerDay", classPerDay);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}




