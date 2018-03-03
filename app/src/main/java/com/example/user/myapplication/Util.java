package com.example.user.myapplication;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by USER on 2018/2/28.
 */

public class Util {

    public static Document getDocumentFromUrlConnection(HttpURLConnection connect, String cookie) throws IOException {
        MainActivity.setHttpUrlConnection(connect);
        MainActivity.setHttpUrlConnectionCookie(connect, cookie);
        return getDocumentFromUrlConnection(connect);
    }

    public static Document getDocumentFromUrlConnection(HttpURLConnection connect) throws IOException {
        BufferedReader reader = MainActivity.getReader(connect, "utf-8");//轉換顯示格式
        StringBuilder all = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null){
            all.append(line);
        }
        return Jsoup.parse(all.toString());
    }
}
