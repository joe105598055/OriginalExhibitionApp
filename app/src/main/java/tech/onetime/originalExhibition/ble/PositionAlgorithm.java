package tech.onetime.originalExhibition.ble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import tech.onetime.originalExhibition.schema.BeaconObject;

/**
 * Created by joe on 2018/3/22.
 */

public class PositionAlgorithm {
    private final String TAG = "PositionAlgorithm";

    private ArrayList<BeaconObject> beacons = new ArrayList<>();
    private int logarithm, power;

    public PositionAlgorithm(ArrayList<BeaconObject> beacons) {
        this.beacons = beacons;
//        for (int i = 0; i < beacons.size(); i++)
//            Log.d(TAG,Integer.toString(i) + Integer.toString(beacons.get(i).rssi));
        sortBeacons();
    }

    /**
     * 回傳估算的座標 和 平均RSSI
     */
//    public double[] getCurrentPositionAndAVGRssi() throws IndexOutOfBoundsException {
    public String getCurrentPosition() throws IndexOutOfBoundsException {


        for(int i = 0 ;i < beacons.size(); i++){
            System.out.println("[" + beacons.get(i).major + "," + beacons.get(i).minor + "] =" + beacons.get(i).rssi);
        }
        BeaconObject base = beacons.get(0);

        return transPosition(base.getMajorMinorString());
    }

    /**
     * 轉換 RSSI
     */
    private double getTransRSSI(int rssi) {
        return Math.abs(rssi) * Math.pow(power, Math.log(Math.abs(rssi) / Math.log(logarithm)));
    }

    /**
     * 由大到小排序 RSSI
     */
    private void sortBeacons() {
        Collections.sort(beacons, new Comparator() {

            @Override
            public int compare(Object lhs, Object rhs) {
                return ((BeaconObject) rhs).rssi - ((BeaconObject) lhs).rssi;
            }
        });
    }
    private String transPosition (String MajorMinor){
        String position;
        switch (MajorMinor){
            case "(0,0)":
            case "(0,5)":
            case "(0,8)":
                position = "A";
                break;
            case "(5,0)":
            case "(5,5)":
            case "(5,8)":
                position = "B";
                break;
            case "(8,0)":
            case "(8,5)":
            case "(8,8)":
                position = "C";
                break;
            default:
                position = "Missing";
        }
        return position;
    }

}
