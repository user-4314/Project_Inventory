package com.example.mybmi;

import androidx.appcompat.app.AppCompatActivity;

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

public class DeleteAccount extends AppCompatActivity {
    String Accountstr;
    TextView Delet_Account;
    EditText Delet_Password;
    TextView Delet_result;
    Button Delet_Sure;
    String result;
    String Passwordstr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        result=" ";
        Delet_Account = (TextView) findViewById(R.id.Delet_Account);
        Delet_Password = (EditText) findViewById(R.id.Delet_Password);
        Delet_result = (TextView) findViewById(R.id.Delet_result);
        Delet_Sure = (Button) findViewById(R.id.Delet_Sure);

        Bundle bundle = this.getIntent().getExtras();
        Accountstr = bundle.getString("KEY_userinfo");
        Delet_Account.setText("員工帳號:"+Accountstr);

        Delet_Sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Passwordstr = Delet_Password.getText().toString();
                String url = "http://140.136.151.67/hsdeletest.php";
                if (Accountstr.equals("") || Passwordstr.equals("")) {
                    Toast.makeText(DeleteAccount.this, "帳號及密碼不能為空白!!", Toast.LENGTH_LONG).show();
                } else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            result = response.trim();
                            Delet_result.setText(result);//跳出success的字串在textView裡
                            if (result.equals("success")) {
                                Intent intent = new Intent();
                                intent.setClass(DeleteAccount.this, MainActivity.class);
                                Toast.makeText(DeleteAccount.this, "刪除成功", Toast.LENGTH_LONG).show();
                                startActivity(intent);
                            } if(result.equals("nofound")){
                                Toast.makeText(DeleteAccount.this, "帳號密碼錯誤或不存在", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(DeleteAccount.this, "error", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(DeleteAccount.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("DeleName", Accountstr);//和php的參數做連結
                            params.put("DelePassword", Passwordstr);
                            return params;

                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(DeleteAccount.this);
                    requestQueue.add(stringRequest);

                }
            }

        });
    }
}