package com.example.user.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class SickCause extends AppCompatActivity {
private TextView textView7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sick_cause);
        PageData pageData = (PageData) getIntent().getExtras().getSerializable("SickApplicationData");
        textView7 = findViewById(R.id.textView7);
        textView7.setText(pageData.startDate.year + "-" + pageData.startDate.month + "-" + pageData.startDate.day);
    }
}
