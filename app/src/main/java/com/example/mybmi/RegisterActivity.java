package com.example.mybmi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends Activity {
    EditText Account;
    EditText Password;
    TextView result_textView;
    Button sure;
    String result;
    String Passwordstr;
    String Accountstr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        result = " ";
        Account = (EditText) findViewById(R.id.Account);
        Password = (EditText) findViewById(R.id.Password);
        result_textView = (TextView) findViewById(R.id.result_textView);
        sure = (Button) findViewById(R.id.Regis_Sure);

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Accountstr = Account.getText().toString();
                Passwordstr = Password.getText().toString();
                String url = "http://140.136.151.67/Login.php";

                if (Accountstr.equals("")) {
                    Toast.makeText(RegisterActivity.this, "員工資料處不能為空白!!", Toast.LENGTH_LONG).show();
                } else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            result = response.trim();
                            result_textView.setText(result);//跳出success的字串在textView裡
                            if (result.equals("Record added successfully")) {
                                Intent intent = new Intent();
                                intent.setClass(RegisterActivity.this, MainActivity.class);
                                Toast.makeText(RegisterActivity.this, "註冊成功", Toast.LENGTH_LONG).show();
                                startActivity(intent);
                            } else {
                                Toast.makeText(RegisterActivity.this, "帳號密碼已存在", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("LoginName", Accountstr);//和php的參數做連結
                            params.put("LoginPassword", Passwordstr);
                            return params;

                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
                    requestQueue.add(stringRequest);

                }
            }

            });


    }
}
