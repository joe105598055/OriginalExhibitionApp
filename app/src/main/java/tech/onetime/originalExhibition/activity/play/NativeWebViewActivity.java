package tech.onetime.originalExhibition.activity.play;


import android.content.DialogInterface;
//import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

//import org.androidannotations.annotations.Click;
//import org.androidannotations.annotations.ViewById;


public class NativeWebViewActivity extends AppCompatActivity  {

    private final String TAG = "NativeWebViewActivity";



//    @ViewById(R.id.floatingActionButton)
//    FloatingActionButton fab;

    //取得展場的所有播放清單。動態的放進清單，第0個為 auto detect。
    int selectedIndex = 0;

//    @Click(R.id.floatingActionButton)
    void getAudioPlayList() {

        final AlertDialog.Builder dialog_list = new AlertDialog.Builder(this);
        final String playlist[] = {"movie1", "movie2"};


        dialog_list.setTitle("test"); // play list dialog title
        dialog_list.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                /** ... Code ... */

                Toast.makeText(NativeWebViewActivity.this, "pick one" + playlist[selectedIndex], Toast.LENGTH_SHORT).show();

            }
        });

        dialog_list.setSingleChoiceItems(playlist, selectedIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                selectedIndex = which;

            }
        });

        dialog_list.show();
    }

}
