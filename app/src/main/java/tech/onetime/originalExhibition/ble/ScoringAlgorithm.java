package tech.onetime.originalExhibition.ble;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tech.onetime.originalExhibition.schema.BeaconObject;

/**
 * Created by joe on 2018/4/7.
 */

public class ScoringAlgorithm {
    private final String TAG = "ScoringAlgorithm";
    private ArrayList<ArrayList<BeaconObject>> beacons = new ArrayList<>();
//    private  ArrayList<String> positionList = new ArrayList<>();
    private Map<String, Integer> beaconMap = new HashMap<>();
    private  ArrayList<String> resultList = new ArrayList<>();
    private final Integer MOVING_THRESHOLD = 300;

    public ScoringAlgorithm(ArrayList<ArrayList<BeaconObject>> beacons) {
        this.beacons = beacons;
//        mappingPosition();
    }
    public String getCurrentPosition(){
        Log.d(TAG, "---------[getCurrentPosition]------------");
        for(int i = 0; i < beacons.size(); i++){ // 1...10 round
            initMap();
            for(int j = 0; j < beacons.get(i).size(); j++){ // (0,0)...(8,8) each Beacons
                beaconMap.put(beacons.get(i).get(j).getMajorMinorString(),beacons.get(i).get(j).rssi);
            }
            Log.d(TAG, "-----[Round] = " + i);
            scoring();
        }

        for(Map.Entry entry : beaconMap.entrySet()){
            Log.d(TAG, "Key : " + entry.getKey() + " Value : " + entry.getValue());
        }
//
        for(String Area:resultList){
            Log.d(TAG, "[Area]" + Area);
        }
//        String candidate = null;
//        for(int i = 0,count = 0; i < positionList.size(); i++){
//            Log.d(TAG,positionList.get(i));
//            if(count == 0){
//                candidate = positionList.get(i);
//                count = 1;
//            }else if(candidate != positionList.get(i)){
//                count--;
//            }else{
//                count++;
//            }
//        }
//        if(isMajorityElement(candidate))
//            return candidate;
//        else
//            return "not majority";
        String currentPostition = voting();

        return currentPostition;

    }

    private String voting(){
        String candidate = null;
        for(int i = 0,count = 0; i < resultList.size(); i++){
            Log.d(TAG,resultList.get(i));
            if(count == 0){
                candidate = resultList.get(i);
                count = 1;
            }else if(candidate != resultList.get(i)){
                count--;
            }else{
                count++;
            }
        }
        if(isMajorityElement(candidate))
            return candidate;
        else
            return "not majority";
    }
    private void scoring(){

        int weightA = Math.max(beaconMap.get("(0,0)"),Math.max(beaconMap.get("(0,5)"),beaconMap.get("(0,8)"))) + 100;
        int weightB = Math.max(beaconMap.get("(5,0)"),Math.max(beaconMap.get("(5,5)"),beaconMap.get("(5,8)"))) + 100;
        int weightC = Math.max(beaconMap.get("(8,0)"),Math.max(beaconMap.get("(8,5)"),beaconMap.get("(8,8)"))) + 100;
        int ScoringA = (beaconMap.get("(0,0)") + beaconMap.get("(0,5)") + beaconMap.get("(0,8)") + 300)  * weightA;
        int ScoringB = (beaconMap.get("(5,0)") + beaconMap.get("(5,5)") + beaconMap.get("(5,8)") + 300) * weightB;
        int ScoringC = (beaconMap.get("(8,0)") + beaconMap.get("(8,5)") + beaconMap.get("(8,8)") + 300) * weightC;

        Log.d(TAG, "weightA = " + weightA + ", " + "ScoringA = " + ScoringA );
        Log.d(TAG, "weightB = " + weightB + ", " + "ScoringB = " + ScoringB );
        Log.d(TAG, "weightC = " + weightC + ", " + "ScoringC = " + ScoringC );

        if(ScoringA == Math.max(ScoringA,Math.max(ScoringB,ScoringC))){
            if(isMoving(ScoringA,ScoringB,ScoringC)){
                resultList.add("critical");
                return;
            }
            resultList.add("A");
        }else if(ScoringB == Math.max(ScoringA,Math.max(ScoringB,ScoringC))){
            if(isMoving(ScoringB,ScoringA,ScoringC)){
                resultList.add("critical");
                return;
            }
            resultList.add("B");
        }else{
            if(isMoving(ScoringC,ScoringA,ScoringB)){
                resultList.add("critical");
                return;
            }
            resultList.add("C");
        }
    }
    private void initMap(){
        beaconMap.clear();
        beaconMap.put("(0,0)",-100);
        beaconMap.put("(0,5)",-100);
        beaconMap.put("(0,8)",-100);
        beaconMap.put("(5,0)",-100);
        beaconMap.put("(5,5)",-100);
        beaconMap.put("(5,8)",-100);
        beaconMap.put("(8,0)",-100);
        beaconMap.put("(8,5)",-100);
        beaconMap.put("(8,8)",-100);

    }

    private boolean isMoving(int target, int compareA, int compareB){
        if(target-compareA < MOVING_THRESHOLD ||target-compareB < MOVING_THRESHOLD)
            return true;
        else
            return false;
    }

    private Boolean isMajorityElement(String candidate){
        int count = 0;
        for(int i = 0; i < resultList.size(); i++){
            if(candidate == resultList.get(i))
                count++;
        }
        if(count > resultList.size()/2)
            return true;
        else
            return false;
    }
//    private void mappingPosition (){
//
//        for(int i = 0; i < beacons.size(); i++ ){
//            switch (beacons.get(i).getMajorMinorString()){
//                case "(0,0)":
//                case "(0,5)":
//                case "(0,8)":
//                    positionList.add("A");
//                    break;
//                case "(5,0)":
//                case "(5,5)":
//                case "(5,8)":
//                    positionList.add("B");
//                    break;
//                case "(8,0)":
//                case "(8,5)":
//                case "(8,8)":
//                    positionList.add("C");
//                    break;
//            }
//        }
//    }
}
