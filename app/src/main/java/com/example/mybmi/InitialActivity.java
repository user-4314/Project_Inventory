package com.example.mybmi;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InitialActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener {
    String Accountstr;
    String result;
    String Place,Name,Item;
    ArrayList<String> itemName;//存名字
    ArrayList<String> itemNumber;//存標號
    ArrayList<String> unitName;
    ArrayList<String> rent;
    ArrayList<String> discard;
    ArrayList<String> personName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        initViews();
        itemName = new ArrayList<>();//初始化名稱陣列
        itemNumber  = new ArrayList<>();//初始化標號陣列
        unitName = new ArrayList<>();
        personName = new ArrayList<>();
        rent = new ArrayList<>();
        discard = new ArrayList<>();
        Bundle bundle = this.getIntent().getExtras();
        Accountstr = bundle.getString("KEY_userinfo");

        setListensName();
        setListensPlace();
        setListensItem();

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);

        navigationView.setNavigationItemSelectedListener(this);
    }
    private Button button_item;
    private ListView datalistview;
    private ImageButton button_name;
    private Button button_place;
    private Spinner str_place;
    private Spinner str_item;
    private EditText str_name;
    private void initViews() {
        button_item = (Button) findViewById(R.id.sure_item);
        datalistview = (ListView) findViewById(R.id.itemlistview);
        button_name = (ImageButton) findViewById(R.id.sure_name);
        button_place = (Button) findViewById(R.id.sure_place);
        str_name = (EditText) findViewById(R.id.search_name);
        str_place = (Spinner) findViewById(R.id.search_place);
        str_item = (Spinner) findViewById(R.id.search_item);
    }
    private void setListensName()
    {

        button_name.setOnClickListener(gotoname);
    }
    private void setListensPlace()
    {
        button_place.setOnClickListener(gotoplace);

    }
    private void setListensItem()
    {
        button_item.setOnClickListener(gotoItem);

    }

    private View.OnClickListener gotoname = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Name = str_name.getText().toString();
            datalistview.setAdapter(null);
            itemNumber.clear();
            itemName.clear();
            unitName.clear();
            personName.clear();
            discard.clear();
            rent.clear();
            if (Name.equals("")) {
                Toast.makeText(InitialActivity.this, "請先輸入文字再做查詢", Toast.LENGTH_LONG).show();
            }
            else{
                String url = "http://140.136.151.67/hsSqlSearch.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        result = response.trim();

                        if (result.equals("no")) {
                            Toast.makeText(InitialActivity.this, "查無資料", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                JSONArray array = new JSONArray(result);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject = array.getJSONObject(i);

                                    String item_number = jsonObject.getString("item_number");
                                    String item_name = jsonObject.getString("item_name");
                                    String unit_name = jsonObject.getString("unit_name");
                                    String person_name = jsonObject.getString("person_name");
                                    String discard1 = jsonObject.getString(("discard"));
                                    String rent1 = jsonObject.getString(("rent"));


                                    itemNumber.add(item_number);
                                    itemName.add(item_name);
                                    unitName.add(unit_name);
                                    personName.add(person_name);
                                    discard.add(discard1);
                                    rent.add(rent1);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }

                        datalistlayoutadapter adasports = new datalistlayoutadapter(InitialActivity.this);
                        datalistview.setAdapter(adasports);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InitialActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected  Map<String,String>getParams(){
                        Map<String,String> params = new HashMap<String,String>();
                        params.put("QRname",Name);//和php的參數做連結
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(InitialActivity.this);
                requestQueue.add(stringRequest);

            }

        }

    };
    private View.OnClickListener gotoplace = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            datalistview.setAdapter(null);
            itemNumber.clear();
            itemName.clear();
            unitName.clear();
            personName.clear();
            discard.clear();
            rent.clear();
            Place = str_place.getSelectedItem().toString();

            String url = "http://140.136.151.67/hsSearchPlace.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response){
                    result = response.trim();

                    if (result.equals("no")) {
                        Toast.makeText(InitialActivity.this, "查無資料", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            JSONArray array = new JSONArray(result);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);

                                String item_number = jsonObject.getString("item_number");
                                String item_name = jsonObject.getString("item_name");
                                String unit_name = jsonObject.getString("unit_name");
                                String person_name = jsonObject.getString("person_name");
                                String discard1 = jsonObject.getString(("discard"));
                                String rent1 = jsonObject.getString(("rent"));

                                itemNumber.add(item_number);
                                itemName.add(item_name);
                                unitName.add(unit_name);
                                personName.add(person_name);
                                discard.add(discard1);
                                rent.add(rent1);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                    datalistlayoutadapter adasports = new datalistlayoutadapter(InitialActivity.this);
                    datalistview.setAdapter(adasports);


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(InitialActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected  Map<String,String>getParams(){
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("QRplace", Place);//和php的參數做連結
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(InitialActivity.this);
            requestQueue.add(stringRequest);

        }
    };
    private View.OnClickListener gotoItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            datalistview.setAdapter(null);
            itemNumber.clear();
            itemName.clear();
            unitName.clear();
            personName.clear();
            discard.clear();
            rent.clear();
            Item = str_item.getSelectedItem().toString();

            String url = "http://140.136.151.67/hsSearchItem.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response){
                    result = response.trim();

                    if (result.equals("no")) {
                        Toast.makeText(InitialActivity.this, "查無資料", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            JSONArray array = new JSONArray(result);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);

                                String item_number = jsonObject.getString("item_number");
                                String item_name = jsonObject.getString("item_name");
                                String unit_name = jsonObject.getString("unit_name");
                                String person_name = jsonObject.getString("person_name");
                                String discard1 = jsonObject.getString(("discard"));
                                String rent1 = jsonObject.getString(("rent"));

                                itemNumber.add(item_number);
                                itemName.add(item_name);
                                unitName.add(unit_name);
                                personName.add(person_name);
                                discard.add(discard1);
                                rent.add(rent1);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }

                    //check2.setText(rent.get(0));
                    datalistlayoutadapter adasports = new datalistlayoutadapter(InitialActivity.this);
                    datalistview.setAdapter(adasports);


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(InitialActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected  Map<String,String>getParams(){
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("QRname", Item);//和php的參數做連結
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(InitialActivity.this);
            requestQueue.add(stringRequest);
        }
    };

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        switch (menuItem.getItemId()) {
            case R.id.menuHome:
                Toast.makeText(InitialActivity.this, "已位於此頁面", Toast.LENGTH_LONG).show();
                break;

            case R.id.menuManagement:
                intent.setClass(InitialActivity.this,InventoryActivity .class);
                bundle.putString("KEY_userinfo", Accountstr);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.menuNFC:
                intent.setClass(InitialActivity.this,NFCScanActivity .class);
                bundle.putString("KEY_userinfo", Accountstr);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.menuQRcode:
                intent.setClass(InitialActivity.this,QRcodeScanActivity .class);
                bundle.putString("KEY_userinfo", Accountstr);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    public class datalistlayoutadapter extends BaseAdapter {
        //記得在設定樣板(hotelmessage的地方盡量把方塊以及顯示自行要拉大，否則很有可能是因為太小所以沒顯示出來)
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



            convertView = listlayoutInflater.inflate(R.layout.itemmessage,null);//內容是要設定在hotelmessage中

            //設定自訂樣板上物件對應的資料。
            TextView item_name = (TextView) convertView.findViewById(R.id.item__name);
            TextView item__number = (TextView) convertView.findViewById(R.id.item__code);
            TextView item_place = (TextView) convertView.findViewById(R.id.item__place);
            TextView item_person = (TextView) convertView.findViewById(R.id.item__person);
            TextView item_rent = (TextView) convertView.findViewById(R.id.item__rent);
            TextView item_discard = (TextView) convertView.findViewById(R.id.item__discard);

            //新增要解析的參數名稱************************************

            //要用get(position)取得資料 (要注意，跟array不同之處)
            item__number.setText(itemName.get(position));
            item_name.setText(itemNumber.get(position));

            if(rent.get(position).equals("0")){
                item_rent.append("未租借/未使用");
                item_place.append("無");
                item_person.append("無");
            }else {
                item_rent.append("租借中/使用過");
                item_place.append(unitName.get(position));
                item_person.append(personName.get(position));
            }
            if(discard.get(position).equals("0")){
                item_discard.append("未報銷");
            }else{
                item_discard.append("已報銷");
            }


            /*new  Thread(new Runnable() {
                @Override
                public void run() {
                    //ToDo Auto-grnerated method stub
                    final Bitmap mBitmap = getBitmapFromUrl(hotelphoto.get(position));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //ToDo Auto-grnerated method stub
                            ImageView jpg_hotel= (ImageView) findViewById(R.id.hotel__pic);
                            jpg_hotel.setImageBitmap(mBitmap);
                        }
                    });
                }
            }).start();*/
            //新增要解析的參數名稱************************************
            /*sure_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    hotel__nameinfo = hotelname.get(position);
                    hotel__priceinfo = hotelprice.get(position);
                    hotel__roominfo = hotelroom.get(position);
                    hotel__addressinfo = hoteladdress.get(position);
                    hotel__photoinfo = hotelphoto.get(position);
                    //修改+++++++++++++
                    Intent intent = new Intent();
                    intent.setClass(ReportActivity.this, bookhotel.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("KEY_priceinfo", hotel__priceinfo);
                    bundle.putString("KEY_nameinfo", hotel__nameinfo);
                    bundle.putString("KEY_roominfo", hotel__roominfo);
                    bundle.putString("KEY_addressinfo", hotel__addressinfo);
                    bundle.putString("KEY_photoinfo", hotel__photoinfo);
                    bundle.putString("KEY_userinfo", Accountstr);
                    bundle.putDouble("KEY_checkinmon", checkinmon);
                    bundle.putDouble("KEY_checkind", checkind);
                    bundle.putDouble("KEY_checkoutmon", checkoutmon);
                    bundle.putDouble("KEY_checkoutd", checkoutd);
                    //修改+++++++++++++

                    intent.putExtras(bundle);
                    startActivity(intent);

                }
            });*/

            return convertView;


        }
    }
}
