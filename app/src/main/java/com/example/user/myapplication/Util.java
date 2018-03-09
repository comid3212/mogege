package com.example.user.myapplication;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by USER on 2018/2/28.
 */

public class Util {

    static final String COOKIES_HEADER = "Set-Cookie";
    static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
    static final String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8";
    static final String ACCEPT_ENCODING = "gzip, deflate";
    static final String Accept_Language  = "zh-TW,zh;q=0.9,en-US;q=0.8,en;q=0.7";
    static final String Referer ="http://msd.ncut.edu.tw/wbcmss/home.asp";
    static final String HOST = "msd.ncut.edu.tw";

    public static Document getDocumentFromUrlConnection(HttpURLConnection connect, String cookie) throws IOException {
        Util.setHttpUrlConnection(connect);
        Util.setHttpUrlConnectionCookie(connect, cookie);
        return getDocumentFromUrlConnection(connect);
    }

    public static Document getDocumentFromUrlConnection(HttpURLConnection connect) throws IOException {
        BufferedReader reader = Util.getReader(connect, "utf-8");//轉換顯示格式
        if(reader == null) return null;
        StringBuilder all = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null){
            all.append(line);
        }
        return Jsoup.parse(all.toString());
    }
    static void setHttpUrlConnection(HttpURLConnection urlConnection){
        urlConnection.setInstanceFollowRedirects( true );
        urlConnection.addRequestProperty("User-Agent", USER_AGENT);
        urlConnection.addRequestProperty("Accept", ACCEPT);
        urlConnection.addRequestProperty("Accept-Encoding", ACCEPT_ENCODING);
        urlConnection.addRequestProperty("Accept-Language", Accept_Language);
        urlConnection.addRequestProperty("Referer", Referer);
        urlConnection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
        urlConnection.setRequestProperty( "charset", "big5");
        urlConnection.setRequestProperty("Host", HOST);
    }

    static void setHttpUrlConnectionCookie(HttpURLConnection urlConnection, String cookie) {
        urlConnection.setRequestProperty("Cookie", cookie);
    }

    static void sendData(HttpURLConnection urlConnection, String data){
        byte[] postData;
        try {
            urlConnection.setDoOutput( true );
            urlConnection.setInstanceFollowRedirects( true );
            urlConnection.setRequestMethod( "POST" );
            postData = data.getBytes("big5");
            int  postDataLength = postData.length;
            urlConnection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            urlConnection.getOutputStream().write(postData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedReader getReader(HttpURLConnection urlConnection){
        try {
            return new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "big5"));
        } catch (IOException e) {
            return null;
        }
    }
    public static BufferedReader getReader(HttpURLConnection urlConnection, String type){
        try {
            return new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), type));
        } catch (IOException e) {
            return null;
        }
    }
}
