package tech.onetime.originalExhibition.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import tech.onetime.originalExhibition.schema.BeaconObject;

/**
 * Created by Alexandro on 2016/7/1.
 */
public class KitkatScanCallback implements BluetoothAdapter.LeScanCallback {

    public iKitkatScanCallback iEvent;

    public KitkatScanCallback(iKitkatScanCallback iEvent) {
        this.iEvent = iEvent;
    }

    public synchronized void onLeScan(final BluetoothDevice device, int rssi,
                                      byte[] scanRecord) {

        //if (null == device.getName())
        //    return;

        //System.out.println("{api <= kitkat}:" + device.getName());

        int startByte = 2;
        boolean patternFound = false;
        // 尋找ibeacon
        // 先依序尋找第2到第8陣列的元素
        while (startByte <= 5) {
            // Identifies an iBeacon
            if (((int) scanRecord[startByte + 2] & 0xff) == 0x02 &&
                    // Identifies correct data length
                    ((int) scanRecord[startByte + 3] & 0xff) == 0x15) {

                patternFound = true;
                break;
            }
            startByte++;
        }

        // 如果找到了的话
        if (patternFound) {
            if (iEvent != null)
                iEvent.kitkat_beaconScanned(new BeaconObject(device, rssi, scanRecord, startByte));
        }

    }

    public void stopDetect() {
        //syncBeacons.getIns().removeAllBeacons();
        //handler.removeCallbacks(updateTimer);
    }

    public interface iKitkatScanCallback {
        /**
         * kitkat - 偵測到 beacon (單一)
         */
        void kitkat_beaconScanned(BeaconObject beaconObject);
    }

}
