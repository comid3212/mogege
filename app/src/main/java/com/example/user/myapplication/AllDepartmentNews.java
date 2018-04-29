package com.example.user.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.os.Handler;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.ShutdownChannelGroupException;
import java.util.ArrayList;


public class AllDepartmentNews extends AppCompatActivity {
    private myHandler handler = new myHandler(this);
    private TextView textView;

    public void News1(View view) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        String X ="http://chem.ncut.edu.tw/files/40-1047-1027-1.php";
        bundle.putString("GetWeb",X);
        msg.arg1 = 1;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
    public void News2(View view) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        String X ="http://me.web2.ncut.edu.tw/files/40-1039-641-1.php";
        bundle.putString("GetWeb",X);
        msg.arg1 = 1;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
    public void News3(View view) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        String X ="http://rac2.ncut.edu.tw/files/40-1036-194-1.php";
        bundle.putString("GetWeb",X);
        msg.arg1 = 1;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
    public void News4(View view) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        String X ="http://pmti.ncut.edu.tw/bin/home.php";
        bundle.putString("GetWeb",X);
        msg.arg1 = 1;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
    public void News5(View view) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        String X ="http://ee1.web2.ncut.edu.tw/files/40-1040-736-1.php";
        bundle.putString("GetWeb",X);
        msg.arg1 = 1;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
    public void News6(View view) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        String X ="http://csie.ncut.edu.tw/news.php?content=1";
        bundle.putString("GetWeb",X);
        msg.arg1 = 1;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
    public void News7(View view) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        String X ="http://landscape.ma.ncut.edu.tw/news/news.php?class=101";
        bundle.putString("GetWeb",X);
        msg.arg1 = 1;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
    public void News8(View view) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        String X ="http://ae.ncut.edu.tw/news/inventory/id/11";
        bundle.putString("GetWeb",X);
        msg.arg1 = 1;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
    public void News9(View view) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        String X ="http://www.ie.ncut.edu.tw/";
        bundle.putString("GetWeb",X);
        msg.arg1 = 1;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
    public void News10(View view) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        String X ="http://www.badger.ncut.edu.tw/news/news.php?class=101,102,103,104,105";
        bundle.putString("GetWeb",X);
        msg.arg1 = 1;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
    public void News11(View view) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        String X ="http://mis.web2.ncut.edu.tw/files/40-1019-25-1.php";
        bundle.putString("GetWeb",X);
        msg.arg1 = 1;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
    public void News12(View view) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        String X ="http://dm.web2.ncut.edu.tw/files/40-1021-27-1.php";
        bundle.putString("GetWeb",X);
        msg.arg1 = 1;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
    public void News13(View view) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        String X ="http://www.rsm.ncut.edu.tw/news/inventory/id/11";
        bundle.putString("GetWeb",X);
        msg.arg1 = 1;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
    public void News14(View view) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        String X ="http://em.web2.ncut.edu.tw/files/40-1014-20-1.php";
        bundle.putString("GetWeb",X);
        msg.arg1 = 1;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
    public void News15(View view) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        String X ="http://cci.web2.ncut.edu.tw/files/40-1018-288-1.php";
        bundle.putString("GetWeb",X);
        msg.arg1 = 1;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
    public void News16(View view) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        String X ="http://itie.web2.ncut.edu.tw/bin/home.php";
        bundle.putString("GetWeb",X);
        msg.arg1 = 1;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
    public void News17(View view) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        String X ="http://www.gen.ncut.edu.tw/bin/home.php";
        bundle.putString("GetWeb",X);
        msg.arg1 = 1;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
    public void News18(View view) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        String X ="http://liberal.ncut.edu.tw/bin/home.php";
        bundle.putString("GetWeb",X);
        msg.arg1 = 1;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

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
                    Intent intent = new Intent();
                    intent.setClass(reference.get(), AllDepartmentView.class);
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
        setContentView(R.layout.activity_all_department_news);
    }
}
