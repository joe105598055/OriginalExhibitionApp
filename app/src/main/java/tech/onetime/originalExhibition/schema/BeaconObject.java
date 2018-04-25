package tech.onetime.originalExhibition.schema;

import android.bluetooth.BluetoothDevice;

import java.io.Serializable;

/**
 * Created by Alexandro on 2016/7/5.
 */
public class BeaconObject implements Serializable{

    private static final long serialVersionUID = -7060210544600464481L;

    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();
//    public boolean hasObject = false;

    public String mac, deviceName, uuid;
    public int major, minor;
    public int rssi;
    public long time = 0;

    public BeaconObject(final BluetoothDevice device, int rssi,
                        byte[] scanRecord, int startByte) {

        //mBluetoothAdapter.stopLeScan(this);
        // 轉換16進制
        byte[] uuidBytes = new byte[16];
        // 來源、起始位置
        System.arraycopy(scanRecord, startByte + 4, uuidBytes, 0, 16);
        String hexString = bytesToHex(uuidBytes);

        // UUID (ex: 319245CD-8CB4-4861-BC1B-37A78EE82EF1)
        String uuid = hexString.substring(0, 8) + "-"
                + hexString.substring(8, 12) + "-"
                + hexString.substring(12, 16) + "-"
                + hexString.substring(16, 20) + "-"
                + hexString.substring(20, 32);

        // Major
        int major = (scanRecord[startByte + 20] & 0xff) * 0x100
                + (scanRecord[startByte + 21] & 0xff);

        // Minor
        int minor = (scanRecord[startByte + 22] & 0xff) * 0x100
                + (scanRecord[startByte + 23] & 0xff);

        String mac = device.getAddress();
        // txPower
        int txPower = (scanRecord[startByte + 24]);
        double distance = calculateAccuracy(txPower, rssi);

        //System.out.println("{UUID:" + uuid + ",(" + major + "," + minor + "),rssi:" + rssi + "}");

        this.mac = device.getAddress();
        this.deviceName = device.getName();
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
        this.rssi = rssi;
        this.time = System.currentTimeMillis();

    }



    private String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * 取得距離
     */
    public double calculateAccuracy(int txPower, double rssi) {
        if (rssi == 0) {
            return -1.0;
        }

        double ratio = rssi * 1.0 / txPower;

        if (ratio < 1.0) {
            return Math.pow(ratio, 10);
        } else {
            double accuracy = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
            return accuracy;
        }
    }

    public String getMajorMinorString(){
        return "(" + major + "," + minor + ")";
    }


}
