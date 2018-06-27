package com.example.user.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class SickCause extends AppCompatActivity {
    RadioGroup isAbroad;
    TextView mainContent, comment,textabroad;
    int type = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sick_cause);
        textabroad = (TextView) findViewById(R.id.textView30);
        isAbroad = findViewById(R.id.is_abroad);
        textabroad.setVisibility(View.INVISIBLE);
        mainContent = findViewById(R.id.main_content);
        comment = findViewById(R.id.comment);
        ((RadioGroup)findViewById(R.id.leave_type)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                type = 0;
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.a:
                        textabroad.setVisibility(View.VISIBLE);
                        isAbroad.setVisibility(View.VISIBLE);
                        break;
                    case R.id.d:
                        type += 1;
                    case R.id.c:
                        type += 1;
                    case R.id.b:
                        type += 1;
                        textabroad.setVisibility(View.INVISIBLE);
                        isAbroad.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        });
    }

    public void nextPage(View view) {
        if(type == -1) {
            Toast.makeText(this, "請選假別", Toast.LENGTH_SHORT).show();
            return;
        }
        if(type == 0 && isAbroad.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "請選擇是否要出國", Toast.LENGTH_SHORT).show();
            return;
        }
        if(mainContent.getText().toString().isEmpty()) {
            Toast.makeText(this, "理由不能為空", Toast.LENGTH_SHORT).show();
            return;
        }
        Bundle bundle = getIntent().getExtras();
        bundle.putInt("leaveType", 822+type);
        bundle.putString("mainContent", mainContent.getText().toString());
        bundle.putString("comment", comment.getText().toString());
        bundle.putBoolean("isAbroad", isAbroad.getCheckedRadioButtonId() == R.id.yes);

        Intent intent = new Intent(this, SickUpload.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
