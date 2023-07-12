package com.example.mybmi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheckBorrowActivity extends AppCompatActivity {
    String Accountstr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_borrow);

        initViews();
        Bundle bundle = this.getIntent().getExtras();
        Accountstr = bundle.getString("KEY_userinfo");
        info.setText(Accountstr+"的租借情況");
        setListensoperate();
    }
    private Button button_back;
    private Button button_sure;
    private TextView info;
    private void initViews() {
        button_back = (Button) findViewById(R.id.backbutton);
        button_sure = (Button) findViewById(R.id.borrowbutton);
        info = (TextView) findViewById(R.id.borrowtextView);
    }

    private void setListensoperate()
    {
        button_back.setOnClickListener(gotoback);
        button_sure.setOnClickListener(gotosee);
    }
    private View.OnClickListener gotoback = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(CheckBorrowActivity.this,InitialActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("KEY_userinfo", Accountstr);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };
    private View.OnClickListener gotosee = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //加查詢個人清單
        }
    };
}