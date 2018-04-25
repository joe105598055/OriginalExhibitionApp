package tech.onetime.originalExhibition.activity;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import tech.onetime.originalExhibition.R;
import tech.onetime.originalExhibition.api.ExcelBuilder;
import tech.onetime.originalExhibition.ble.BeaconScanCallback;
import tech.onetime.originalExhibition.schema.BeaconObject;

/**
 * Created by JianFa on 2017/2/26
 */

@EActivity(R.layout.activity_setting)
public class ExhibitionActivity extends AppCompatActivity implements BeaconScanCallback.iBeaconScanCallback{

    private static final String TAG = "ExhibitionActivity";
    private BeaconScanCallback _beaconCallback;
    private long lastUpdateTime = 0;

    @ViewById ImageView areaImage;
    @ViewById TextView times;
    private ArrayList<ArrayList<BeaconObject>> eachRoundBeacons = new ArrayList<>();
    private Queue<BeaconObject> _scanResultQueue = new LinkedList<>();

    static final int REQUEST_ENABLE_BT = 1001; // The request code


    @AfterViews
    void afterViews() {

        Log.d(TAG, "afterViews");
        if(bleInit()){
            Log.d(TAG, "[bleInit] true");
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private boolean bleInit() {

        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "This device does not support bluetooth", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        BluetoothManager bm = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        BluetoothAdapter mBluetoothAdapter = bm.getAdapter();

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return false;
        }

        return scanBeacon();

    }

    private boolean scanBeacon() {

        if (_beaconCallback != null)
            _beaconCallback.stopScan();

        _beaconCallback = new BeaconScanCallback(this, this);
//        Log.d(TAG, "scanBeacon __ set beacon mac : " + currentBeaconObject.mac);
        // TO Set beacon filter
//        _beaconCallback.setScanFilter_address(currentBeaconObject.mac);
        _beaconCallback.startScan();

        return true;

    }

    @Override
    public void onBackPressed() {

//        super.onBackPressed();
        setResult(RESULT_CANCELED, ExhibitionActivity.this.getIntent());
        ExhibitionActivity.this.finish();

    }

    @Override
    public void scannedBeacons(BeaconObject beaconObject) {
        /**To do  each beaconObject be scanned */

    }

    int roundTimes = 0;
    @Override
    public void getNearestBeacon(BeaconObject beaconObject) {

//        Log.d(TAG, "getNearestBeacon-------------------" + beaconObject);
//        times.setText(Integer.toString(++roundTimes));

    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


    @Override
    public void getCurrentPosition(String position, ArrayList<BeaconObject> beaconArray,BeaconObject beaconObject) {
        long currentUpdateTime = System.currentTimeMillis();
//
//        Log.d(TAG, "[currentUpdateTime]" + currentUpdateTime);
//        Log.d(TAG, "[lastUpdateTime   ]" + lastUpdateTime);
        Log.d(TAG, "[delta]" + (currentUpdateTime - lastUpdateTime));

        if (currentUpdateTime - lastUpdateTime < 2000){
            return;
        }

        lastUpdateTime = currentUpdateTime;
        Log.d(TAG, "************************getCurrentPosition: " + position);
        _scanResultQueue.offer(beaconObject);
        eachRoundBeacons.add(beaconArray);
        updatePosition(position);
    }
    private String currentPosition = "";
    private void updatePosition(String position){
//        roundTimes = 0;
        times.setText(Integer.toString(++roundTimes));

        if(currentPosition != position){
            currentPosition = position;
            switch (currentPosition){
                case "A":
                    new DownloadImageTask((ImageView) findViewById(R.id.areaImage)).execute("http://140.124.181.85:3000/image/A.png");
                    break;
                case "B":
                    new DownloadImageTask((ImageView) findViewById(R.id.areaImage)).execute("http://140.124.181.85:3000/image/B.png");
                    break;
                case "C":
                    new DownloadImageTask((ImageView) findViewById(R.id.areaImage)).execute("http://140.124.181.85:3000/image/C.png");
                    break;
                case "critical":
                    new DownloadImageTask((ImageView) findViewById(R.id.areaImage)).execute("http://140.124.181.85:3000/image/Critical.png");
                    break;
                case "not majority":
                    new DownloadImageTask((ImageView) findViewById(R.id.areaImage)).execute("http://140.124.181.85:3000/image/Moving.png");
                    break;
            }
        }
    }

    protected void onDestroy(){

        super.onDestroy();

        if (_beaconCallback != null) _beaconCallback.stopScan();

    }

    @Click(R.id.exit)
    void exit(){
        setResult(RESULT_OK, ExhibitionActivity.this.getIntent());
        Log.d(TAG, "Store result");
        doSaveResult();
        ExhibitionActivity.this.finish();
    }

    @Background
    void doSaveResult() {

        Log.d(TAG, "Saving result");

        ExcelBuilder.initExcel();
        Log.d(TAG, "[eachRoundBeacons Size]" + eachRoundBeacons.size());
        while(!_scanResultQueue.isEmpty()) {
            ExcelBuilder.setCellByRowInOrder(_scanResultQueue.poll());
        }
        while(eachRoundBeacons.size() != 0 ){

            ExcelBuilder.setRoundInOrder(eachRoundBeacons.get(0));
            eachRoundBeacons.remove(0);
        }

        ExcelBuilder.saveExcelFile(this, "originalTemp");

    }
}
