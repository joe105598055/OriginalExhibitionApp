package tech.onetime.originalExhibition.api;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import tech.onetime.originalExhibition.schema.BeaconObject;



public class ExcelBuilder {

    public static final String TAG = "ExcelBuilder";

    private static Workbook _wb = null;

    public static int rowIndex = 1;
    public static int rowRoundIndex = 1;

    public static int colIndex = 0;
    private static int[] distances = {1, 2, 3, 5, 8, 13, 20, 30, 40, 50};

    public static void initExcel() {

        if (_wb == null) {
            _wb = new HSSFWorkbook();
            _wb.createSheet("RSSI");
            _wb.createSheet("Beacon");
        }
        Sheet RSSISheet = _wb.getSheet("RSSI");
        RSSISheet.createRow(0).createCell(0).setCellValue("winner");
        RSSISheet.getRow(0).createCell(1).setCellValue("rssi");
        RSSISheet.getRow(0).createCell(3).setCellValue("(0,0)");
        RSSISheet.getRow(0).createCell(4).setCellValue("(0,2)");
        RSSISheet.getRow(0).createCell(5).setCellValue("(0,4)");
        RSSISheet.getRow(0).createCell(6).setCellValue("(5,11)");
        RSSISheet.getRow(0).createCell(7).setCellValue("(5,13)");
        RSSISheet.getRow(0).createCell(8).setCellValue("(5,15)");
        RSSISheet.getRow(0).createCell(9).setCellValue("(8,22)");
        RSSISheet.getRow(0).createCell(10).setCellValue("(8,24)");
        RSSISheet.getRow(0).createCell(11).setCellValue("(8,26)");

    }

    public static void clearExcel() {

        _wb = null;

    }

    public static void setCellByRowInOrder(BeaconObject content) {

        Sheet RSSISheet = _wb.getSheet("RSSI");
        RSSISheet.createRow(rowIndex).createCell(colIndex).setCellValue("[" + content.major + "," + content.minor + "]");
        RSSISheet.getRow(rowIndex).createCell(1).setCellValue(content.rssi);
        rowIndex++;
    }

    public static void setRoundInOrder(ArrayList<BeaconObject> content){
        System.out.println("[setRoundInOrder]");
        Sheet RSSISheet = _wb.getSheet("RSSI");
        for(int j=0;j < content.size() ; j++){
            BeaconObject beaconObject = content.get(j);
            System.out.println(beaconObject.getMajorMinorString() + " , " + beaconObject.rssi);
            switch (beaconObject.getMajorMinorString()){
                case "(0,0)":
                    RSSISheet.getRow(rowRoundIndex).createCell(3).setCellValue(beaconObject.rssi);
                    break;
                case "(0,2)":
                    RSSISheet.getRow(rowRoundIndex).createCell(4).setCellValue(beaconObject.rssi);
                    break;
                case "(0,4)":
                    RSSISheet.getRow(rowRoundIndex).createCell(5).setCellValue(beaconObject.rssi);
                    break;
                case "(5,11)":
                    RSSISheet.getRow(rowRoundIndex).createCell(6).setCellValue(beaconObject.rssi);
                    break;
                case "(5,13)":
                    RSSISheet.getRow(rowRoundIndex).createCell(7).setCellValue(beaconObject.rssi);
                    break;
                case "(5,15)":
                    RSSISheet.getRow(rowRoundIndex).createCell(8).setCellValue(beaconObject.rssi);
                    break;
                case "(8,22)":
                    RSSISheet.getRow(rowRoundIndex).createCell(9).setCellValue(beaconObject.rssi);
                    break;
                case "(8,24)":
                    RSSISheet.getRow(rowRoundIndex).createCell(10).setCellValue(beaconObject.rssi);
                    break;
                case "(8,26)":
                    RSSISheet.getRow(rowRoundIndex).createCell(11).setCellValue(beaconObject.rssi);
                    break;
            }
        }
        rowRoundIndex++;
    }
    public static boolean saveExcelFile(Context context, String fileName) {

        // check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e(TAG, "Storage not available or read only");
            return false;
        }

        boolean success = false;

        // Create a path where we will place our List of objects on external storage
        File file = new File(context.getExternalFilesDir(null), fileName + ".xls");
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            _wb.write(os);
            Log.w("FileUtils", "Writing file" + file);
            success = true;
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return success;
    }

    @Deprecated
    public static void readExcelFile(Context context, String fileName) {

        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e(TAG, "Storage not available or read only");
            return;
        }

        try{
            // Creating Input Stream
            File file = new File(context.getExternalFilesDir(null), fileName + ".xls");
            FileInputStream myInput = new FileInputStream(file);

            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);

            /** We now need something to iterate through the cells.**/
            Iterator rowIter = mySheet.rowIterator();

            while(rowIter.hasNext()){
                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator cellIter = myRow.cellIterator();
                while(cellIter.hasNext()){
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    Log.d(TAG, "Cell Value: " +  myCell.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return;
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }


    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

}
