package com.example.mybmi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;
import android.widget.Toast;

public class LogingActivity extends Activity {
    EditText Login_Account;
    EditText Login_Password;
    TextView Login_result;
    Button Login_Sure;
    String result;
    String Passwordstr;
    String Accountstr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loging);
        result=" ";
        Login_Account = (EditText) findViewById(R.id.Login_Account);//可以測試一下R.id.Account
        Login_Password = (EditText) findViewById(R.id.Login_Password);
        Login_result = (TextView) findViewById(R.id.Login_result);
        Login_Sure = (Button) findViewById(R.id.Login_Sure);

        Login_Sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(LogingActivity.this,InitialActivity .class);
                startActivity(intent);

            }

        });

    }
}