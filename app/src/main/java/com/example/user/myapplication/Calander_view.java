package com.example.user.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.Date;

public class Calander_view extends CalendarView {
    AlertDialog alertDialog;

    interface Callback {
        void callback(int year, int month, int day);
    }

    Callback callback;

    public Calander_view(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(this);
        alertDialog = builder.create();

        Calendar calendar = Calendar.getInstance();
        //設定日曆的選取範圍
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        this.setMinDate(calendar.getTimeInMillis());
        calendar.add(Calendar.DAY_OF_MONTH, 87);
        this.setMaxDate(calendar.getTimeInMillis());

        this.setOnDateChangeListener(new OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                if(callback != null) {
                    callback.callback(year, month + 1, dayOfMonth);//月份從0開始算,所以month+1
                }
                alertDialog.cancel();//
            }
        });
    }

    void setCallback (Callback callback){
        this.callback = callback;
    }

    public void choiseDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        //設定成傳進來的日期
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        this.setDate(calendar.getTimeInMillis());
        alertDialog.show();
    }
}
