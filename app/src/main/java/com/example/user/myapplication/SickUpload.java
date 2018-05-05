package com.example.user.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class CountryAdapter extends ArrayAdapter<String> {
    List<Integer> countryCode;

    public CountryAdapter(@NonNull Context context, int resource, @NonNull List<String> objects, List<Integer> countryCode) {
        super(context, resource, objects);
        this.countryCode = countryCode;
    }

    public int getItemCountryCode(int i) {
        return countryCode.get(i);
    }

    public void addItemCountryCode(int code) {
        countryCode.add(code);
    }

    public void deleteItemCountryCode(int index) {
        countryCode.remove(index);
    }
}

public class SickUpload extends AppCompatActivity {

    Spinner countriesSpinner;
    Button searchButton;
    ListView countriesList;
    CountryAdapter countryAdapter;
    SearchAlertDialog searchAlertDialog;
    boolean first = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sick_upload);
        countriesSpinner = findViewById(R.id.countries_spinner);
        searchButton = findViewById(R.id.search);
        countriesList = findViewById(R.id.countries_list);
        searchAlertDialog = new SearchAlertDialog(this);
        searchAlertDialog.setOnItemClickCallBack(new SearchAlertDialog.CallBack() {
            @Override
            public void callback(String name, int code) {
                addCountryToListView(name, code);
            }
        });
        countryAdapter = new CountryAdapter(SickUpload.this,android.R.layout.simple_list_item_1, new ArrayList<String>(), new ArrayList<Integer>());
        countryAdapter.setNotifyOnChange(true);
        countriesList.setAdapter(countryAdapter);

        countriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SickUpload.this);
                builder.setTitle("Info");
                builder.setMessage("是否清除國家:  " + countryAdapter.getItem(i));
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int o) {//複寫爸爸的資料
                        countryAdapter.remove(countryAdapter.getItem(i));
                        countryAdapter.deleteItemCountryCode(i);
                        countryAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("否", null);
                builder.show();
            }
        });

        String  cookie = CookieManager.getInstance().getCookie("http://140.128.78.77/");
        Boolean abroad = getIntent().getExtras().getBoolean("isAbroad");
        if(abroad){
            countriesSpinner.setVisibility(View.VISIBLE);
            countriesList.setVisibility(View.VISIBLE);
            searchButton.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpURLConnection connect = (HttpURLConnection)new URL("http://stdl.ncut.edu.tw/StdL/WebApi/ConfigureData/GetCountrySet").openConnection();
                        connect.setRequestProperty("Host", Malingering.HOST);
                        connect.setRequestProperty("Cookie", CookieManager.getInstance().getCookie("http://140.128.78.77/"));
                        connect.setRequestMethod("GET");
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        byte a[] = new byte[1024];
                        int len;
                        while((len = connect.getInputStream().read(a)) > 0) {
                            os.write(a, 0, len);
                        }
                        String json = os.toString();
                        JSONArray jsonArray = new JSONArray(json);
                        final List<String> nameList = new ArrayList<>();
                        final List<Integer> countryCode = new ArrayList<>();
                        for(int i = 1; i < jsonArray.length(); ++i) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            nameList.add(object.getString("chtName"));
                            countryCode.add(object.getInt("countryCode"));
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final CountryAdapter adapter = new CountryAdapter(SickUpload.this,android.R.layout.simple_spinner_item, nameList, countryCode);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                countriesSpinner.setAdapter(adapter);
                                countriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        if(first) {
                                            first = false;
                                            return;
                                        }
                                        addCountryToListView(adapterView.getSelectedItem().toString(), adapter.getItemCountryCode(i));
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }else {
            countriesSpinner.setVisibility(View.INVISIBLE);
            countriesList.setVisibility(View.INVISIBLE);
            searchButton.setVisibility(View.INVISIBLE);
        }
    }

    public void addCountryToListView(String name, int code) {
        countryAdapter.add(name);
        countryAdapter.addItemCountryCode(code);
        countryAdapter.notifyDataSetChanged();
    }

    public void searchCountry(View view) {
        searchAlertDialog.show();
    }
}
