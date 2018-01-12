package com.example.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class myClass {
    public static void main(String[] args){
        try {
            URL url = new URL("http://msd.ncut.edu.tw/wbcmss/home.asp");

            String COOKIES_HEADER = "Set-Cookie";
            String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
            String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8";
            String ACCEPT_ENCODING = "gzip, deflate";
            String Accept_Language  = "zh-TW,zh;q=0.9,en-US;q=0.8,en;q=0.7";
            String Referer ="http://msd.ncut.edu.tw/wbcmss/home.asp";
            String HOST = "msd.ncut.edu.tw";


            HttpURLConnection connect = (HttpURLConnection)url.openConnection();

            CookieManager cookieManager = new CookieManager();
            connect.addRequestProperty("User-Agent", USER_AGENT);
            connect.addRequestProperty("Accept", ACCEPT);
            connect.addRequestProperty("Accept-Encoding", ACCEPT_ENCODING);
            connect.addRequestProperty("Accept-Language", Accept_Language);
            connect.addRequestProperty("Referer", Referer);
            connect.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            connect.setRequestProperty( "charset", "big5");
            connect.setRequestProperty("Host", HOST);


            Map<String, List<String>> headerFields = connect.getHeaderFields();
            System.out.println(headerFields);

            List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

            for(String cookie : cookiesHeader) {
                cookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
            }
            StringBuilder tmp = new StringBuilder();
            for(HttpCookie cookies : cookieManager.getCookieStore().getCookies()) {
                tmp.append(cookies.toString()).append(";");
            }

            //3A417100
            //zxc55004827

            connect = (HttpURLConnection)url.openConnection();

            connect.setDoOutput( true ); //
            connect.setInstanceFollowRedirects( true );
            connect.setRequestMethod( "POST" );

            connect.addRequestProperty("User-Agent", USER_AGENT);
            connect.addRequestProperty("Accept", ACCEPT);
            connect.setRequestProperty("Cookie", tmp.toString());
            connect.addRequestProperty("Accept-Encoding", ACCEPT_ENCODING);
            connect.addRequestProperty("Accept-Language", Accept_Language);
            connect.addRequestProperty("Referer", Referer);
            connect.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            connect.setRequestProperty( "charset", "big5");
            connect.setRequestProperty("Host", HOST);
            String urlParameters  = "stagex=pre&sid=3A417100&pass=zxc55004827&code=mnm4&code1=MNM4&action1=%BDT%A9w";  //輸入帳號密碼

            byte[] postData       = urlParameters.getBytes("big5");    //
            int    postDataLength = postData.length;
            connect.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));

            connect.getOutputStream().write(postData);

            BufferedReader b = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            String line;

            PrintWriter writer = new PrintWriter("main1.html", "BIG5");
            while((line = b.readLine()) != null){
                writer.println(line);
            }

            connect.disconnect();

            url = new URL("http://msd.ncut.edu.tw/wbcmss/show_timetable.asp?detail=yes");

            connect = (HttpURLConnection)url.openConnection();
            connect.setRequestProperty("Cookie", tmp.toString());

            connect.setInstanceFollowRedirects( true );
            connect.setRequestMethod( "GET" );
            connect.addRequestProperty("User-Agent", USER_AGENT);
            connect.addRequestProperty("Accept", ACCEPT);
            connect.addRequestProperty("Accept-Encoding", ACCEPT_ENCODING);
            connect.addRequestProperty("Accept-Language", Accept_Language);
            connect.addRequestProperty("Referer", Referer);
            connect.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            connect.setRequestProperty( "charset", "big5");

            System.out.println(connect.getHeaderFields());

            b = new BufferedReader(new InputStreamReader(connect.getInputStream(), "big5"));


            PrintWriter writer1 = new PrintWriter("main.html", "big5");
            while((line = b.readLine()) != null){
                writer1.println(line);
            }
            writer1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
