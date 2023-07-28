package com.example.mybmi;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
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
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QRcodeAcitivity extends Activity {
    String name,ampm,place,qrcontent ,qritemnumber,qritemname,qrplace,appearance,maintain,function;
    double year ,month,day,hour,minute;
    ArrayList<String> hotelname;//存名字
    ArrayList<String> hotelroom;//存標號
    String result;
    String Accountstr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_acitivity);
        getCameraPermission();
        initViews();
        hotelname = new ArrayList<>();//初始化名稱陣列
        hotelroom  = new ArrayList<>();//初始化標號陣列
        try
        {
            Bundle bundle = this.getIntent().getExtras();
            name = bundle.getString("KEY_name");
            ampm = bundle.getString("KEY_ampm");
            place = bundle.getString("KEY_place");
            year = Double.parseDouble(bundle.getString("KEY_year"));
            month = Double.parseDouble(bundle.getString("KEY_month"));
            day = Double.parseDouble(bundle.getString("KEY_day"));
            hour = Double.parseDouble(bundle.getString("KEY_hour"));
            minute = Double.parseDouble(bundle.getString("KEY_minute"));
            Accountstr = bundle.getString("KEY_userinfo");
        }
        catch(Exception obj) {
            Toast.makeText(this, "資料格式有誤", Toast.LENGTH_SHORT).show();
        }


        scene.getHolder().addCallback(new SurfaceHolder.Callback(){
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder){
                if(ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA) !=PackageManager.PERMISSION_GRANTED)
                    return;
                try{
                    cameraSource.start(surfaceHolder);
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder,int i,int i1,int i2){

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }

        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>(){
            @Override
            public void release(){

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes=detections.getDetectedItems();
                if(qrCodes.size()!=0){
                    context.post(new Runnable() {
                        @Override
                        public void run() {

                            qrcontent = qrCodes.valueAt(0).displayValue;
                            stringSpilt(qrcontent);
                        }

                    });
                }
            }
        });
        setListenseroperate();
    }


    private TextView context;
    private SurfaceView scene;
    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;
    private Spinner str_appearance;
    private Spinner str_maintain;
    private Spinner str_function;
    private Button button_sure;
    private Button button_finish;
    private Button sure;
    private ListView itemlistview;
    private void initViews() {
        button_sure = (Button) findViewById(R.id.qrcodesure);
        button_finish = (Button) findViewById(R.id.qrcodefinish);
        sure = (Button) findViewById(R.id.sure);
        context = (TextView) findViewById(R.id.QRcodecontext);
        scene = (SurfaceView) findViewById(R.id.camerascene);
        str_appearance = (Spinner) findViewById(R.id.appearance);
        str_maintain = (Spinner) findViewById(R.id.maintain);
        str_function = (Spinner) findViewById(R.id.function);
        itemlistview = (ListView) findViewById(R.id.itemlistview);
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource=new CameraSource.Builder(this,barcodeDetector).setRequestedPreviewSize(300,300).build();
        cameraSource = new CameraSource.Builder(this,barcodeDetector).setAutoFocusEnabled(true).build();
    }
    public  void  getCameraPermission(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(QRcodeAcitivity.this, Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(this).setCancelable(false).setTitle("需要相機權限").setMessage("需要啟動相機才能使用QR code功能").setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(QRcodeAcitivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                    }
                }).show();
            }
            else{
                ActivityCompat.requestPermissions(QRcodeAcitivity.this,new String[]{Manifest.permission.CAMERA},1);
            }
        }
    }
    public void stringSpilt(String args){
        String[] arrSplit = args.split("\n");
        for (int i=0; i < arrSplit.length; i++)
            System.out.println(arrSplit[i]);
        if(arrSplit.length>=3) {
            qritemnumber = arrSplit[0];
            qritemname = arrSplit[1];
            qrplace = arrSplit[2];
            context.setText("資產代號:"+qritemnumber+"\n"+"資產名稱:"+qritemname+"\n"+"使用單位:"+qrplace);
        }
        else{
            qritemnumber = "";
            qritemname = "";
            qrplace = "";
            context.setText("資產代號:暫無資料"+"\n"+"資產名稱:暫無資料"+"\n"+"使用單位:暫無資料");
            Toast.makeText(QRcodeAcitivity.this, "此QR code非盤點標籤", Toast.LENGTH_SHORT).show();
        }
    }
    private void setListenseroperate()
    {
        button_sure.setOnClickListener(gotosure);
        button_finish.setOnClickListener(gotofinish);
        sure.setOnClickListener(showlistview);
    }
    private View.OnClickListener gotosure = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(place.equals(qrplace)){
                appearance = str_appearance.getSelectedItem().toString();
                maintain = str_maintain.getSelectedItem().toString();
                function = str_function.getSelectedItem().toString();

                //增加php到資料庫
                String url = "http://140.136.151.67/hsQRcodeScanData.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        result = response.trim();
                        if (result.equals("success")) {
                            Toast.makeText(QRcodeAcitivity.this, "掃描成功", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(QRcodeAcitivity.this, "此物品已掃描過", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(QRcodeAcitivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    protected Map getParams() {
                        Map params = new HashMap();
                        params.put("name", name);//和php的參數做連結
                        params.put("ampm", ampm);
                        params.put("itemnumber",qritemnumber);
                        params.put("itemname",qritemname);
                        params.put("apperance",appearance);
                        params.put("maintain",maintain);
                        params.put("place",place);
                        params.put("function",function);
                        params.put("year",String.valueOf(year));
                        params.put("month",String.valueOf(month));
                        params.put("day",String.valueOf(day));
                        params.put("hour",String.valueOf(hour));
                        params.put("minute",String.valueOf(minute));
                        return params;

                    }

                };
                RequestQueue requestQueue = Volley.newRequestQueue(QRcodeAcitivity.this);
                requestQueue.add(stringRequest);

                //Toast.makeText(QRcodeMainActivity.this, "盤點成功", Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(QRcodeAcitivity.this, "此資產不屬於此使用單位", Toast.LENGTH_LONG).show();
            }
        }
    };
    private View.OnClickListener gotofinish = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(QRcodeAcitivity.this, InitialActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("KEY_userinfo", Accountstr);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };
    private View.OnClickListener showlistview = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url = "http://140.136.151.67/ChenReport.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    result = response.trim();

                    if(result.equals("no")){
                        Toast.makeText(QRcodeAcitivity.this,"沒有資料", Toast.LENGTH_SHORT).show();
                    }
                    else {

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


                    listlayoutadapter adasports = new listlayoutadapter(QRcodeAcitivity.this);

                    itemlistview.setAdapter(adasports);////要把adapter的地方設定在activity_report.xml中的hotellistview 其中listVIew的大小要設定大一點否則可能會看不見





                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(QRcodeAcitivity.this,error.toString(), Toast.LENGTH_SHORT).show();

                }


            }){

                protected Map<String,String> getParams(){

                    Map<String,String> params = new HashMap<String,String>();

                    //params.put("dataofAddress",county);

                    //params.put("dataofHprice",strHprice);

                    //params.put("dataofLprice",strLprice);

                    //params.put("dataofPerson",person);

                    //新增要解析的參數名稱******************************************

                    return params;

                }

            };


            RequestQueue requestQueue = Volley.newRequestQueue(QRcodeAcitivity.this);
            requestQueue.add(stringRequest);



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
