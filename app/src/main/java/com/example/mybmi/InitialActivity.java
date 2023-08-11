package com.example.mybmi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InitialActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener {
    String Accountstr;
    String result;
    String Place,Name;
    ArrayList<String> hotelname;//存名字
    ArrayList<String> hotelroom;//存標號
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        initViews();
        hotelname = new ArrayList<>();//初始化名稱陣列
        hotelroom  = new ArrayList<>();//初始化標號陣列
        Bundle bundle = this.getIntent().getExtras();
        Accountstr = bundle.getString("KEY_userinfo");


        setListensoperate();

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
    private Button sure;
    private ListView itemlistview;
    private Button button_daccount;
    private Button button_cborrow;
    private Spinner str_place;
    private EditText str_name;
    private void initViews() {
        sure = (Button) findViewById(R.id.sure);
        itemlistview = (ListView) findViewById(R.id.itemlistview);
        button_daccount = (Button) findViewById(R.id.deleteaccount);
        button_cborrow = (Button) findViewById(R.id.checkborrow);
        str_name = (EditText) findViewById(R.id.search_name);
        str_place = (Spinner) findViewById(R.id.search_place);
    }
    private void setListensoperate()
    {
        button_daccount.setOnClickListener(gotodelacc);
        button_cborrow.setOnClickListener(gotoborrow);
        sure.setOnClickListener(showlistview);
    }

    private View.OnClickListener gotodelacc = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(InitialActivity.this,DeleteAccount .class);
            Bundle bundle = new Bundle();
            bundle.putString("KEY_userinfo", Accountstr);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };
    private View.OnClickListener gotoborrow = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(InitialActivity.this,CheckBorrowActivity .class);
            Bundle bundle = new Bundle();
            bundle.putString("KEY_userinfo", Accountstr);
            intent.putExtras(bundle);
            startActivity(intent);
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

            case R.id.menuBorrow:
                intent.setClass(InitialActivity.this,InsertActivity .class);
                bundle.putString("KEY_userinfo", Accountstr);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.menuReturn:
                intent.setClass(InitialActivity.this,DeleteActivity .class);
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



    private View.OnClickListener showlistview = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Place = str_place.getSelectedItem().toString();
            Name = str_name.getText().toString();

            if (Name.equals("")) {
                String url = "http://140.136.151.67/ChenReport.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        result = response.trim();

                        if (result.equals("no")) {
                            Toast.makeText(InitialActivity.this, "沒有資料", Toast.LENGTH_SHORT).show();
                        } else {

                            try {
                                JSONArray array = new JSONArray(result);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject = array.getJSONObject(i);

                                    String strname = jsonObject.getString("item_name");//name是對應到資料庫裡name的column
                                    //String strprice = jsonObject.getString("item_number");//price是對應到資料庫裡price的column
                                    String strroom = jsonObject.getString("item_number");//room是對應到資料庫裡room的column
                                    //String straddress = jsonObject.getString("address");
                                    //String strphoto = jsonObject.getString("photo");
                                    //新增要解析的參數名稱*****************************************
                                    hotelname.add(strname);
                                    //hotelprice.add(strprice);
                                    hotelroom.add(strroom);
                                    //hoteladdress.add(straddress);
                                    //hotelphoto.add(strphoto);
                                    //
                                    //新增要解析的參數名稱*****************************************

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }


                        InitialActivity.listlayoutadapter adasports = new InitialActivity.listlayoutadapter(InitialActivity.this);

                        itemlistview.setAdapter(adasports);////要把adapter的地方設定在activity_report.xml中的hotellistview 其中listVIew的大小要設定大一點否則可能會看不見


                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InitialActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

                    }


                }) {

                    protected Map<String, String> getParams() {

                        Map<String, String> params = new HashMap<String, String>();

                        //params.put("dataofAddress",county);

                        //params.put("dataofHprice",strHprice);

                        //params.put("dataofLprice",strLprice);

                        //params.put("dataofPerson",person);

                        //新增要解析的參數名稱******************************************

                        return params;

                    }

                };


                RequestQueue requestQueue = Volley.newRequestQueue(InitialActivity.this);
                requestQueue.add(stringRequest);


            }
            else{

            }
        }


    };

    public class listlayoutadapter extends BaseAdapter {
        //記得在設定樣板(hotelmessage的地方盡量把方塊以及顯示自行要拉大，否則很有可能是因為太小所以沒顯示出來)
        private LayoutInflater listlayoutInflater;

        public listlayoutadapter(Context c){
            listlayoutInflater = LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
            //取得ArrayList的總數 (要注意，跟array不同之處)
            return hotelname.size();
        }

        @Override
        public Object getItem(int position) {
            //要用get(position)取得資料 (要注意，跟array不同之處)
            return  hotelname.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {



            convertView = listlayoutInflater.inflate(R.layout.itemmessage,null);//內容是要設定在hotelmessage中

            //設定自訂樣板上物件對應的資料。
            //ImageView img_logo = (ImageView) convertView.findViewById(R.id.imglogo);
            TextView hotel_name = (TextView) convertView.findViewById(R.id.item__name);
            //TextView hotel_price = (TextView) convertView.findViewById(R.id.hotel__price);
            //Button sure_button = (Button) convertView.findViewById(R.id.sure_button);//////新增的button
            TextView hotel_room = (TextView) convertView.findViewById(R.id.item__code);
            //TextView hotel_address = (TextView) convertView.findViewById(R.id.hotel__address);
            //TextView hotel_photo = (TextView) convertView.findViewById(R.id.hotel__photo);
            //新增要解析的參數名稱************************************

            //要用get(position)取得資料 (要注意，跟array不同之處)
            //img_logo.setImageResource(aryimas.get(position));
            hotel_name.setText(hotelname.get(position));
            //hotel_price.setText("NT$"+hotelprice.get(position));
            hotel_room.setText(hotelroom.get(position));
            //hotel_address.setText(hoteladdress.get(position));
            //hotel_photo.setText(hotelphoto.get(position));

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
