package com.example.ky.beaconscanner;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements BeaconConsumer {
    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager;
    private TextView tv_outPut;
    ArrayList<Listviewitem> data;
    ListviewAdapter adapter;
    DecimalFormat df = new DecimalFormat("#.##");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView=(ListView)findViewById(R.id.listview);
        data=new ArrayList<>();


        adapter=new ListviewAdapter(this,R.layout.item,data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(data.get(position).getFlag()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cclab.cbnu.ac.kr/node/check/"+data.get(position).getMinor()));
                    startActivity(intent);
                    data.get(position).setFlag(false);
                }
            }
        });

        // URL 설정.
        String url = "http://cclab.cbnu.ac.kr/node/";
        ContentValues Test=new ContentValues();
        Test.put("id","newst84");
        // AsyncTask를 통해 HttpURLConnection 수행.
        NetworkTask networkTask = new NetworkTask(url, Test);
        networkTask.execute();
        beaconManager = BeaconManager.getInstanceForApplication(this);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
         beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
         beaconManager.setForegroundScanPeriod(2000);
        beaconManager.bind(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                Log.d("minor ", "minor1");
                if (beacons.size() > 0) {
                    Iterator<Beacon> iterator = beacons.iterator();
                    while (iterator.hasNext()) {
                        Beacon beacon = iterator.next();
                        if(beacon.getId1().toString().equals("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")) {
//                            double rssi = beacon.getRssi();
//                            int txPower = beacon.getTxPower();
                            for(int i=0;i<data.size();i++) {
                                if(beacon.getId2().toString().equals(data.get(i).getMajor())
                                        &&beacon.getId3().toString().equals(data.get(i).getMinor())) {
                                    double distance = beacon.getDistance();
                                    int major = beacon.getId2().toInt();
                                    int minor = beacon.getId3().toInt();
                                    data.get(i).setFlag(true);
                                    data.get(i).setMeter("거리 : "+String.valueOf(BigDecimal.valueOf(distance).setScale(2, RoundingMode.HALF_UP)));
                                    Collections.swap(data, 0, i);
                                }
                            }
                        }

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
//                    Log.i(TAG, "The first beacon I see is about "+beacons.iterator().next().getDistance()+" meters away.");

                }
                Log.d("minor ", "minor44");
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            Log.d("minor ", "minor2");
        } catch (RemoteException e) {    }
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) {

            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                    JSONArray jArray = new JSONArray(s);
                    for(int i=0;i<jArray.length();i++) {
                        JSONObject jObject=jArray.getJSONObject(i);
                        Listviewitem temp = new Listviewitem("배달원: "+jObject.getString("name"),"전화번호: "+ jObject.getString("phone"),
                                "회사 : "+jObject.getString("company"),"상품명 : "+ jObject.getString("product"),null);
                        temp.setUUID(jObject.getString("UUID"));
                        temp.setMajor(jObject.getString("Major"));
                        temp.setMinor(jObject.getString("_id"));
                        data.add(temp);
                    }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter.notifyDataSetChanged();


            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.

        }
    }

}
