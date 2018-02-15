package com.example.user.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
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
class ClassInfomations{
    public List<String> className = new LinkedList<>();
    public List<Integer> classLength = new LinkedList<>();
}
class MyAdapters extends BaseAdapter {
    private Context context;
    private List<ClassInfomation> classes;

    public MyAdapters(Context context, List<ClassInfomation> classes) {
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
        } else {
        }

        LinearLayout layout = (LinearLayout)view.findViewById(R.id.classLayout);
        layout.removeAllViews();
        ClassInfomation classInfomation = classes.get(i);
        for(int o = 0; o < classInfomation.classLength.size(); ++o) {
            String name = classInfomation.className.get(o);
            Integer length = classInfomation.classLength.get(o);
            MyTextView textView = new MyTextView(context);
            textView.setText(name);
            textView.setGravity(TextView.TEXT_ALIGNMENT_CENTER);

            layout.addView(textView, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,1.0f
            ));
            for(int s = 1; s < length; ++s){
                MyTextView textView1 = new MyTextView(context);
                textView1.setGravity(TextView.TEXT_ALIGNMENT_CENTER);
                textView1.down = false;
                textView1.top = false;
                layout.addView(textView1, new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1.0f
                ));
            }
        }

        return view;
    }
}

public class GradeList extends AppCompatActivity {
    private String cookie;
    private String years;
    private int gridHeight, gridWidth;
    private RelativeLayout layout;
    private RelativeLayout tmpLayout;
    private static boolean isFirst = true;
    private myHandler handler = new myHandler(this);
    private HorizontalListView ListView;

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
                    ArrayList<String> className = bundle.getStringArrayList("ClassName");
                    ArrayList<Integer> classLength = bundle.getIntegerArrayList("ClassLength");
                    List<ClassInfomation> classInfomations = new LinkedList<>();
                    for(int i = 0; i < 8; ++i) {
                        classInfomations.add(new ClassInfomation());
                    }
                    //classInfomations.get(1).className.add("學年");
                    //classInfomations.get(1).classLength.add(1);
                    classInfomations.get(1).className.add("學期");
                    classInfomations.get(1).classLength.add(1);
                    classInfomations.get(2).className.add("科目名稱");
                    classInfomations.get(2).classLength.add(1);
                    classInfomations.get(3).className.add("修別");
                    classInfomations.get(3).classLength.add(1);
                    classInfomations.get(4).className.add("學分");
                    classInfomations.get(4).classLength.add(1);
                    classInfomations.get(5).className.add("成績");
                    classInfomations.get(5).classLength.add(1);
                    //classInfomations.get(6).className.add("排名");
                    //classInfomations.get(6).classLength.add(1);
                    int[] padList = new int[8];
                    int count = 0;
                    for(int i=7;i<className.size() - 1;i++) {
                        while(padList[count] > 1) {
                            padList[count] -= 1;
                            count += 1;
                            count %= 8;
                        }
                        int length = classLength.get(i - 7);
                        classInfomations.get(count).className.add(className.get(i - 7));
                        classInfomations.get(count).classLength.add(length);
                        if(length != 1) {
                            padList[count] = length;
                        }
                        count += 1;
                        count %= 8;
                    }


                    for(int i = 0; i < classInfomations.get(0).classLength.size(); ++i) {
                        MyTextView textView1 = new MyTextView(reference.get());
                        textView1.setText(String.valueOf(i + 1));
                        ;
                    }

                    MyAdapter adapter;
                    classInfomations.remove(0);
                    adapter = new MyAdapter(reference.get() , classInfomations);

                    ListView.setAdapter(adapter);
                    //將ListAdapter設定至ListView裡面
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_grade_list);
        ListView = (HorizontalListView) findViewById(R.id.testlist);
        cookie = this.getIntent().getExtras().getString("COOKIE");
        final String a = this.getIntent().getExtras().getString("學年");
        final String b = this.getIntent().getExtras().getString("學期");
        super.onCreate(savedInstanceState);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connect = null;
                try {
                    connect = (HttpURLConnection) (new URL("http://msd.ncut.edu.tw/wbcmss/score_browse.asp?table=V05")).openConnection();
                    MainActivity.setHttpUrlConnection(connect);
                    MainActivity.setHttpUrlConnectionCookie(connect, cookie);
                    MainActivity.getReader(connect);
                    BufferedReader reader = MainActivity.getReader(connect);
                    StringBuilder all = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null){
                        all.append(line);
                    }
                    Document document = Jsoup.parse(all.toString());
                    Elements information = document.getElementsByTag("td");
                    ArrayList<String> className = new ArrayList<String>();
                    ArrayList<Integer> classLength = new ArrayList<>();
                    for(int i=0;i<information.size() - 1;)
                    {
                        if( information.get(i).text().equals(a) && information.get(i+1).text().equals(b)){
                            for(int a=0;a<8;a++) {
                                className.add(information.get(i).text());
                                classLength.add(1);
                                i++;
                            }
                        } else {
                        i++;
                        }
                    }
                    Message msg = new Message();
                    msg.arg1 = 3;
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("ClassName", className);
                    bundle.putIntegerArrayList("ClassLength", classLength);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}




