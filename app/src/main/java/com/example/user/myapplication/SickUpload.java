package com.example.user.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

class CountryInfo{
    int country;
    boolean highRisk;
    CountryInfo(){}
    CountryInfo(int country, boolean highRisk) {
       this.country = country;
       this.highRisk = highRisk;
    }
}

class CountryAdapter extends ArrayAdapter<String> {
    List<CountryInfo> countryInfoList;

    public CountryAdapter(@NonNull Context context, int resource, @NonNull List<String> objects, List<CountryInfo> countryInfoList) {
        super(context, resource, objects);
        this.countryInfoList = countryInfoList;
    }

    public CountryInfo getItemCountryInfo(int i) {
        return countryInfoList.get(i);
    }

    public void addItemCountryInfo(CountryInfo info) {
        countryInfoList.add(info);
    }

    public void deleteItemCountryInfo(int index) {
        countryInfoList.remove(index);
    }
}

public class SickUpload extends AppCompatActivity {

    final static int PICK_IMAGE = 9999;

    Spinner countriesSpinner;
    Button searchButton;
    ListView countriesList;
    CountryAdapter countryAdapter;
    SearchAlertDialog searchAlertDialog;
    TextView imageInfo;
    ImageView uploadPreview;
    boolean first = true;
    Uri imageFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sick_upload);
        countriesSpinner = findViewById(R.id.countries_spinner);
        searchButton = findViewById(R.id.search);
        countriesList = findViewById(R.id.countries_list);
        imageInfo = findViewById(R.id.imgName);
        uploadPreview = findViewById(R.id.upload_preview);
        searchAlertDialog = new SearchAlertDialog(this);
        searchAlertDialog.setOnItemClickCallBack(new SearchAlertDialog.CallBack() {
            @Override
            public void callback(String name, CountryInfo info) {
                addCountryToListView(name, info);
            }
        });
        countryAdapter = new CountryAdapter(SickUpload.this,android.R.layout.simple_list_item_1, new ArrayList<String>(), new ArrayList<CountryInfo>());
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
                        countryAdapter.deleteItemCountryInfo(i);
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
                        final List<CountryInfo> countryInfo = new ArrayList<>();
                        for(int i = 1; i < jsonArray.length(); ++i) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            nameList.add(object.getString("chtName"));
                            countryInfo.add(new CountryInfo(object.getInt("countryCode"), false));
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final CountryAdapter adapter = new CountryAdapter(SickUpload.this,android.R.layout.simple_spinner_item, nameList, countryInfo);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                countriesSpinner.setAdapter(adapter);
                                countriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        if(first) {
                                            first = false;
                                            return;
                                        }
                                        addCountryToListView(adapterView.getSelectedItem().toString(), adapter.getItemCountryInfo(i));
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

    public void addCountryToListView(String name, CountryInfo info) {
        countryAdapter.add(name);
        countryAdapter.addItemCountryInfo(info);
        countryAdapter.notifyDataSetChanged();
    }

    public void searchCountry(View view) {
        searchAlertDialog.show();
    }

    public void uploadImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                imageFile = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageFile);
                uploadPreview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveAndUpload(View view) {
        Thread uploadImage = null;
        final String imgJson = "";
        final JSONObject object = new JSONObject();
        if(imageFile != null) {
            uploadImage = new Thread(new Runnable() {
            @Override
                public void run() {
                    try {
                        String attachmentName = "png";
                        String attachmentFileName = "upload.png";
                        String crlf = "\r\n";
                        String twoHyphens = "--";
                        String boundary =  "----WebKitFormBoundaryWFpCB3WqnxksNbpo";
                        HttpURLConnection connect = (HttpURLConnection)new URL("http://140.128.78.77/stdl/WebApi/LeaveData/Upload").openConnection();
                        /**/
                        connect.setRequestProperty("Host", Malingering.HOST);
                        connect.setRequestProperty("Referer", Malingering.Referer);
                        connect.setRequestProperty("Origin", Malingering.Origin);
                        connect.setRequestProperty("User-Agent", Malingering.USER_AGENT);
                        connect.setRequestProperty("Accept", Malingering.ACCEPT);
                        connect.setRequestProperty("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundaryWFpCB3WqnxksNbpo");
                        connect.setRequestProperty("Accept-Encoding", Malingering.ACCEPT_ENCODING);
                        connect.setRequestProperty("Cookie", CookieManager.getInstance().getCookie("http://140.128.78.77/"));
                        connect.setRequestMethod("POST");
                        connect.setUseCaches(false);
                        connect.setDoOutput(true);
                        DataOutputStream request = new DataOutputStream(
                                connect.getOutputStream());
                        request.writeBytes(twoHyphens + boundary + crlf);
                        request.writeBytes("Content-Disposition: form-data; name=\"myFile\"; filename=\"" + UUID.randomUUID().toString() + ".jpg\"" + crlf);
                        request.writeBytes("Content-Type: image/jpeg" + crlf);
                        request.writeBytes(crlf);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(SickUpload.this.getContentResolver(), imageFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        request.write(stream.toByteArray());
                        request.writeBytes(crlf);
                        request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
                        request.flush();
                        request.close();

                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        byte a[] = new byte[1024];
                        int len;
                        while((len = connect.getInputStream().read(a)) > 0) {
                            os.write(a, 0, len);
                        }
                        object.put("info", new JSONArray(os.toString()));
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
            });
        } else {
            try {
                object.put("info", JSONObject.NULL);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(uploadImage != null) {
            uploadImage.start();
        }
        Bundle bundle = getIntent().getExtras();
        try {
            object.put("comment", bundle.getString("comment"));
            PageData pageData = (PageData) bundle.getSerializable("SickApplicationData");
            Calendar calendar = Calendar.getInstance();
            int years = calendar.get(Calendar.YEAR);
            int months = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DATE);
            object.put("createTime", years + "-" + months + "-" + day);
            object.put("createUser", bundle.getString("id"));
            object.put("documentId", 0);
            object.put("startDate", pageData.startDate.toString());
            object.put("endDate", pageData.endDate.toString());
            //info
            object.put("isForeign", (bundle.getBoolean("isAbroad")) ? 1 : 0);
            //object.put("leaveDays", ); TODO
            object.put("leaveType", bundle.getInt("leaveType"));
            object.put("mainContent", bundle.getString("mainContent"));
            int chinayears =years-1911;
            if (months<9){
                chinayears -=1 ;
            }
            object.put("schyy", chinayears);
            chinayears = 1;
            if (2 < months && months < 9){
                chinayears = 2;
            }
            object.put("smt", chinayears);
            object.put("studentID", bundle.getString("id"));
            object.put("tranStatus", 0);
            object.put("userdpt", 1);

            List<ClassInfo> data = pageData.data;
            Set<String> leaveDaysCount = new TreeSet<>();
            JSONArray jsonArray = new JSONArray();

            for(int i = 0; i < data.size(); ++i) {
                ClassInfo classInfo = data.get(i);
                boolean hasClass = false;
                StringBuilder period = new StringBuilder();
                for(int index = 0; index < classInfo.period.size(); ++index) {
                    SimplePair<String, Boolean> hour = classInfo.period.get(index);
                    if(hour.second) {
                        if(period.length() != 0) {
                            period.append(",");
                        }
                        period.append(hour.first);
                        hasClass = true;
                    }
                }
                if(hasClass) {
                    JSONObject hour = new JSONObject();
                    hour.put("cid", classInfo.cid);
                    hour.put("courseType", classInfo.courseType);
                    hour.put("leaveDate", classInfo.leaveDate);
                    hour.put("period", period.toString());
                    hour.put("sbid", classInfo.sbid);
                    hour.put("sbType", classInfo.sbtype);
                    jsonArray.put(i, hour);
                    leaveDaysCount.add(classInfo.leaveDate);
                }
            }

            object.put("timeInfo", jsonArray);

            if(bundle.getBoolean("isAbroad")) {
                JSONArray tourHist = new JSONArray();
                for (int i = 0; i < countryAdapter.getCount(); ++i) {
                    JSONObject tour = new JSONObject();
                    CountryInfo info = countryAdapter.getItemCountryInfo(i);
                    tour.put("country", info.country);
                    tour.put("startDate", pageData.startDate.toString());
                    tour.put("endDate", pageData.endDate.toString());
                    tour.put("highRisk", info.highRisk);
                    tour.put("otherCountry", JSONObject.NULL);
                    tourHist.put(i, tour);
                }
                object.put("tourHist", tourHist);
            } else {
                object.put("tourHist", JSONObject.NULL);
            }

            object.put("leaveDays", leaveDaysCount.size());
            object.put("documentId", 0);
            object.put("punnm", JSONObject.NULL);
            object.put("appInfo", JSONObject.NULL);

            uploadImage.join();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection connect = null;
                    try {
                        connect = (HttpURLConnection) (new URL("http://140.128.78.77/stdl/WebApi/LeaveData/SetLeaveData")).openConnection();
                        Util.setHttpUrlConnection(connect);
                        connect.setRequestProperty("Host", Malingering.HOST);
                        connect.setRequestProperty("Referer", Malingering.Referer);
                        connect.setRequestProperty("Origin", Malingering.Origin);
                        connect.setRequestProperty("User-Agent", Malingering.USER_AGENT);
                        connect.setRequestProperty("Accept", Malingering.ACCEPT);
                        connect.setRequestProperty("Content-Type", Malingering.CONTEXT_TYPE);
                        connect.setRequestProperty("Accept-Encoding", Malingering.ACCEPT_ENCODING);
                        connect.setRequestProperty("Cookie", CookieManager.getInstance().getCookie("http://140.128.78.77/"));
                        connect.setDoOutput(true);
                        connect.setRequestMethod("PUT");
                        String js = object.toString();
                        connect.getOutputStream().write(js.getBytes());
                        int code = connect.getResponseCode();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
