package tech.onetime.originalExhibition.ble;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.util.Log;

import tech.onetime.originalExhibition.schema.BeaconObject;

/**
 * Created by Alexandro on 2016/7/1.
 */
@TargetApi(Build.VERSION_CODES.M)
public class LollipopScanCallback extends ScanCallback {

    private final String TAG = "LollipopScanCallback";
//    private int triggerTimes = 0;

    public iLollipopScanCallback iEvent;

    private BeaconObject beaconObject;

    public LollipopScanCallback(iLollipopScanCallback iEvent) {
        this.iEvent = iEvent;
    }

    @Override
    public void onScanResult(int callbackType, ScanResult result) {

//        Log.d(TAG, "onScanResult__Trigger times : " + (++triggerTimes));

        // get the discovered device as you wish
        // this will trigger each time a new device is found
        BluetoothDevice device = result.getDevice();

//        Log.d(TAG, "onScanResult__device.getName() : " + device.getName());

        if (device.getName() == null) {
            return;
        }

        ScanRecord record = result.getScanRecord();

//        Log.d(TAG, "onScanResult__result.getScanRecord().toString() : " + record.toString());


        byte[] scanRecord_bytes = new byte[0];
        if (record != null) {
            scanRecord_bytes = record.getBytes();
        }

        int rssi = result.getRssi();

        int startByte = 2;
        boolean patternFound = false;
        while (startByte <= 5) {
            if (((int) scanRecord_bytes[startByte + 2] & 0xff) == 0x02 && //Identifies an iBeacon
                    ((int) scanRecord_bytes[startByte + 3] & 0xff) == 0x15) { //Identifies correct data length
                patternFound = true;
                break;
            }
            startByte++;
        }

//        Log.d(TAG, "patternFound : " + patternFound);

        if (patternFound) {
            iEvent.lollipop_beaconScanned(new BeaconObject(device, rssi, scanRecord_bytes, startByte));
        }
    }

    /*@Override
    public void onBatchScanResults(java.util.List<android.bluetooth.le.ScanResult> results) {
        System.out.println("--------------------batch scan--------------------\nresults:" + results.size());
        int maxRSSI = -1000;
        BeaconObject nearest = null;
        if (results.size() > 0) {
            for (ScanResult scanResult : results) {
                BeaconObject object = parseSingleBeaconObject(scanResult);
                if (object != null) {
                    if (maxRSSI < object.rssi) {
                        nearest = object;
                        maxRSSI = object.rssi;
                    }
                }
            }

            if (nearest != null) {
                //System.out.println("nearest:" + nearest.deviceName + ",(" + nearest.major + "," + nearest.minor + ")");
                if (iEvent != null)
                    iEvent.lollipop_NearestBeaconScanned(nearest);
            }
        }
    }*/

    @Override
    public void onScanFailed(int errorCode) {
        Log.d(TAG, "onScanFailed");
        Log.e("Scan Failed", "Error Code: " + errorCode);
    }

    public interface iLollipopScanCallback {
        /**
         * lollipop - 偵測到 beacon (單一)
         * */
        void lollipop_beaconScanned(BeaconObject beaconObject);
    }
}
