package tw.edu.ntu.mobilehero;

import java.util.ArrayList;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class ActivityGroupSetting extends ActivityGroup{
	
    public static ActivityGroupSetting groupSetting;
    /** Back Stack */
    private ArrayList<View> history;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.history = new ArrayList<View>();
        groupSetting = this;
        
        View view = getLocalActivityManager().startActivity("Activity1", new Intent(ActivityGroupSetting.this, Setting.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
        replaceView(view);
    }
    
    public void replaceView(View v) {
        history.add(v);
        setContentView(v);
    }
 
    public void back() {
        if(history.size() > 1) {
            history.remove(history.size()-1);
            View v = history.get(history.size()-1);
            setContentView(v);
        }else {
            new AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Exit?")
            .setMessage("Do you want to exit?")
            .setPositiveButton("yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //Stop the activity
                    finish();    
                }

            })
            .setNegativeButton("no", null)
            .show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                back();
                break;
        }
        return true;
    }
}