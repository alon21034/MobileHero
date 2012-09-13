package tw.edu.ntu.mobilehero;

import java.util.ArrayList;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class ActivityGroupView extends ActivityGroup{
	
    public static ActivityGroupView groupView;
    /** Back Stack */
    private ArrayList<View> history;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.history = new ArrayList<View>();
        groupView = this;
        
        View view = getLocalActivityManager().startActivity("Activity1", new Intent(ActivityGroupView.this, ComicViewActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
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
            finish();
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