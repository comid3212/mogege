package com.example.user.myapplication;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;
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

public class Doorline extends AppCompatActivity {
    private String cookie;

    private TextView result_view;
    private  ImageView resultView;
    private TextView english_view,copysience_view,service_view,work_view;
    private myHandler handler = new myHandler(this);
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    class myHandler extends Handler{
        private WeakReference<Activity> reference;
        myHandler(Activity activity){
            reference = new WeakReference<Activity>(activity);
        }


        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    break;
                case 1:
                    Bundle bundle = msg.getData();
                    ArrayList<String> Message = bundle.getStringArrayList("Mesg");
                    String moge = bundle.getString("PASS");
                    String moge2 = bundle.getString("PASS2");
                    result_view.setText(moge2);
                    String uri = "@drawable/" + moge;
                    int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                    Drawable image = getResources().getDrawable(imageResource);
                    resultView.setImageDrawable(image);
                    english_view.setText(Message.get(0));
                    copysience_view.setText(Message.get(1));
                    service_view.setText(Message.get(2));
                    work_view.setText(Message.get(3));


                    break;
            }
        }




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doorline);
        setTitle("畢業門檻");
        //cookie = this.getIntent().getExtras().getString("COOKIE");
        cookie = CookieManager.getInstance().getCookie("http://nmsd.ncut.edu.tw/");
        english_view = (TextView)findViewById(R.id. english_view);
        copysience_view = (TextView)findViewById(R.id.copysience_view);
        service_view = (TextView)findViewById(R.id.service_view );
        result_view = (TextView)findViewById(R.id.textView24 );
        work_view = (TextView)findViewById(R.id.work_view);

        resultView = (ImageView) findViewById( R.id.imageView4);

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
                    connect = (HttpURLConnection) (new URL("http://nmsd.ncut.edu.tw/wbcmss/Graduate/GradPass")).openConnection();
                    Document document = Util.getDocumentFromUrlConnection(connect, cookie);

                    Elements information = document.getElementsByTag("li");
                    ArrayList<String> Message = new ArrayList<String>();

                    String pass="";
                    String pass2="";
                    for(int i=49;i<53;i++)
                    {
                        Message.add(information.get(i).text());
                    }
                    if(Message.get(0).equals("【英文能力】 未通過")&&Message.get(1).equals("【資訊能力】 未通過")&&Message.get(2).equals("【服務學習】 未通過")&&Message.get(3).equals("【校外實習門檻】 未通過")){
                        pass ="ao1";
                        pass2="請再加油";
                    }
                    else if(Message.get(0).equals("【英文能力】 通過")&&Message.get(1).equals("【資訊能力】 通過")&&Message.get(2).equals("【服務學習】 通過")&&Message.get(3).equals("【校外實習門檻】 通過")){
                        pass ="ao2";
                        pass2="全部通過，太猛了吧";
                    }
                    else{
                        pass ="ao3";
                        pass2 ="有點成就唷";
                    }


                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("Mesg", Message);

                    bundle.putString("PASS", pass);
                    bundle.putString("PASS2", pass2);
                    msg.arg1 = 1;
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

