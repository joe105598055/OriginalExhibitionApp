package tech.onetime.originalExhibition.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import tech.onetime.originalExhibition.R;

@EActivity(R.layout.activity_init_activity_v3)
public class InitActivityV3 extends AppCompatActivity{

    public final String TAG = "InitActivityV3";

    static final int SETTING_REQUEST = 1;
    static final int REQUEST_ENABLE_BT = 1001; // The request code

    @ViewById Button entry;



    @Click void entry() {

        Log.d(TAG, "Setting");

        Intent intent = new Intent(this, ExhibitionActivity_.class);
        startActivityForResult(intent, SETTING_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            }

    @UiThread
    public void updateView() {

        Log.d(TAG, "updateView");

    }


    @Override
    public void onResume() {

        super.onResume();

        Log.d(TAG, "onResume");

    }

    protected void onDestroy(){

        super.onDestroy();

    }

}
