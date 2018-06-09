package com.example.user.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 2018/5/5.
 */

class AlertCountryAdapter extends CountryAdapter {
    public AlertCountryAdapter(@NonNull Context context, int resource, @NonNull List<String> objects, List<CountryInfo> countryCode) {
        super(context, resource, objects, countryCode);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTextSize(TypedValue.COMPLEX_UNIT_DIP,36);
        return view;
    }
}

public class SearchAlertDialog {
    AlertDialog alertDialog;
    GridLayout gridLayout;
    ListView listView;
    EditText searchEdit;
    Button searchButton;

    CallBack callBack;

    interface CallBack {
        void callback(String name, CountryInfo info);
    }

    public SearchAlertDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        gridLayout = new GridLayout(context);
        searchEdit = new EditText(context);
        searchButton = new Button(context);
        searchButton.setText("Search");
        listView = new ListView(context);

        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.columnSpec = GridLayout.spec(0, 1);
        layoutParams.rowSpec = GridLayout.spec(0, 1);
        layoutParams.width = GridLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = GridLayout.LayoutParams.MATCH_PARENT;
        gridLayout.addView(searchEdit, layoutParams);

        layoutParams = new GridLayout.LayoutParams();
        layoutParams.columnSpec = GridLayout.spec(0, 1);
        layoutParams.rowSpec = GridLayout.spec(1, 1);
        layoutParams.width = GridLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = GridLayout.LayoutParams.MATCH_PARENT;
        gridLayout.addView(searchButton, layoutParams);

        layoutParams = new GridLayout.LayoutParams();
        layoutParams.columnSpec = GridLayout.spec(0, 1);
        layoutParams.rowSpec = GridLayout.spec(2, 1);
        layoutParams.width = GridLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = GridLayout.LayoutParams.MATCH_PARENT;
        gridLayout.addView(listView, layoutParams);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HttpURLConnection connect = (HttpURLConnection) new URL("http://stdl.ncut.edu.tw/StdL/WebApi/ConfigureData/GetCountrySet/" + searchEdit.getText().toString()).openConnection();
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
                            final List<String> countryName = new ArrayList<String>();
                            final List<CountryInfo> countryCode = new ArrayList<CountryInfo>();
                            for(int i = 0; i < jsonArray.length(); ++i) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                countryName.add(object.getString("chtName"));
                                countryCode.add(new CountryInfo(object.getInt("countryCode"), false)); // TODO high risk
                            }
                            searchButton.post(new Runnable() {
                                @Override
                                public void run() {
                                    listView.setAdapter(new AlertCountryAdapter(context,android.R.layout.simple_spinner_item, countryName, countryCode));
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(callBack != null) {
                    callBack.callback(((TextView)view).getText().toString(), ((CountryAdapter) listView.getAdapter()).getItemCountryInfo(i));
                }
                alertDialog.cancel();
            }
        });

        builder.setView(gridLayout);

        builder.setTitle("Search");

        alertDialog = builder.create();
    }

    public void setOnItemClickCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show() {
        alertDialog.show();
    }
}
