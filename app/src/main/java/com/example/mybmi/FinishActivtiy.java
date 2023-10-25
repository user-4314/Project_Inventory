package com.example.mybmi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FinishActivtiy extends Activity {
    String place,Accountstr,name,ampm,mode;
    double year ,month,day,hour,minute;

    String result;
    ArrayList<String> itemNumber;
    ArrayList<String> itemSubnumber;
    ArrayList<String> itemName;
    ArrayList<String> checkStates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_activtiy);

        initViews();

        itemNumber = new ArrayList<>();
        itemSubnumber = new ArrayList<>();
        itemName = new ArrayList<>();
        checkStates = new ArrayList<>();

        Bundle bundle = this.getIntent().getExtras();
        Accountstr = bundle.getString("KEY_userinfo");
        place = bundle.getString("KEY_place");
        name = bundle.getString("KEY_name");
        ampm = bundle.getString("KEY_ampm");
        mode = bundle.getString("KEY_mode");
        year = Double.parseDouble(bundle.getString("KEY_year"));
        month = Double.parseDouble(bundle.getString("KEY_month"));
        day = Double.parseDouble(bundle.getString("KEY_day"));
        hour = Double.parseDouble(bundle.getString("KEY_hour"));
        minute = Double.parseDouble(bundle.getString("KEY_minute"));

        context.setText(place+"的盤點狀況");

        setListenseroperate();

    }

    private Button button_done;
    private Button button_back;
    private Button button_exit;
    private TextView context;
    private ListView datalistview;


    private void initViews() {
        button_done = (Button) findViewById(R.id.done);
        button_back = (Button) findViewById(R.id.back);
        button_exit = (Button) findViewById(R.id.exit);
        context = (TextView) findViewById(R.id.checkplace);
        datalistview = (ListView) findViewById(R.id.donelistview);
    }

    private void setListenseroperate()
    {
        button_done.setOnClickListener(gotodone);
        button_back.setOnClickListener(gotoback);
        button_exit.setOnClickListener(gotoexit);
    }

    private View.OnClickListener gotoback = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mode.equals("0")) {
                Intent intent = new Intent();
                intent.setClass(FinishActivtiy.this, QRcodeAcitivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("KEY_ampm", ampm);
                bundle.putString("KEY_place", place);
                bundle.putString("KEY_year", String.valueOf(year));
                bundle.putString("KEY_month", String.valueOf(month));
                bundle.putString("KEY_day", String.valueOf(day));
                bundle.putString("KEY_hour", String.valueOf(hour));
                bundle.putString("KEY_minute", String.valueOf(minute));
                bundle.putString("KEY_name", name);
                bundle.putString("KEY_userinfo", Accountstr);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            else{
                Intent intent = new Intent();
                intent.setClass(FinishActivtiy.this, RFIDActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("KEY_ampm", ampm);
                bundle.putString("KEY_place", place);
                bundle.putString("KEY_year", String.valueOf(year));
                bundle.putString("KEY_month", String.valueOf(month));
                bundle.putString("KEY_day", String.valueOf(day));
                bundle.putString("KEY_hour", String.valueOf(hour));
                bundle.putString("KEY_minute", String.valueOf(minute));
                bundle.putString("KEY_name", name);
                bundle.putString("KEY_userinfo", Accountstr);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    };

    private View.OnClickListener gotoexit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(FinishActivtiy.this, InitialActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("KEY_userinfo", Accountstr);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    private View.OnClickListener gotodone = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String url = "http://140.136.151.67/hsfinialList.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response){
                    result = response.trim();

                    if (result.equals("no")) {
                        Toast.makeText(FinishActivtiy.this, "查無資料", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            JSONArray array = new JSONArray(result);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);

                                String item_number = jsonObject.getString("item_number");
                                String item_name = jsonObject.getString("item_name");
                                String ckeckState = jsonObject.getString("CheckState");

                                itemNumber.add(item_number);
                                itemName.add(item_name);
                                checkStates.add(ckeckState);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }

                    datalistlayoutadapter adasports = new datalistlayoutadapter(FinishActivtiy.this);
                    datalistview.setAdapter(adasports);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(FinishActivtiy.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected  Map<String,String>getParams(){
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("QRplace", place);//和php的參數做連結
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(FinishActivtiy.this);
            requestQueue.add(stringRequest);

            }

    };

    public class datalistlayoutadapter extends BaseAdapter {

        private LayoutInflater listlayoutInflater;

        public datalistlayoutadapter(Context c){
            listlayoutInflater = LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
            //取得ArrayList的總數 (要注意，跟array不同之處)
            return itemName.size();
        }

        @Override
        public Object getItem(int position) {
            //要用get(position)取得資料 (要注意，跟array不同之處)
            return  itemName.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            convertView = listlayoutInflater.inflate(R.layout.detail,null);

            //設定自訂樣板上物件對應的資料。

            //新增要解析的參數名稱************************************
            TextView item__number = (TextView) convertView.findViewById(R.id.detail_itNumber);
            TextView item_name = (TextView) convertView.findViewById(R.id.detail_itNname);
            TextView CheckState = (TextView) convertView.findViewById(R.id.detail_CheckState);
            //要用get(position)取得資料 (要注意，跟array不同之處)
            //DecimalFormat nf = new DecimalFormat("0");
            item__number.setText(itemNumber.get(position));
            item_name.setText((itemName.get(position)));
            if(checkStates.get(position).equals("0")) {
                CheckState.setText("未盤點");
            }else{
                CheckState.setText(checkStates.get(position));
            }

            //新增要解析的參數名稱************************************

            return convertView;

        }
    }

}