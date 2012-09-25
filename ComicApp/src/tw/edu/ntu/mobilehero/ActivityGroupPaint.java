package tw.edu.ntu.mobilehero;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

public class ActivityGroupPaint extends FragmentActivity {  
    public static FragmentManager fm;  
    private int back;
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.fragment_paint);  
        fm = getSupportFragmentManager();  
        // 只當容器，主要內容已Fragment呈現  
        initFragment(new PaintingActivity());
        back = 0;
    }  

    
    // 切換Fragment  
    public void changeFragment(Fragment f){  
        changeFragment(f, false);  
    }  
    // 初始化Fragment(FragmentActivity中呼叫)  
    public void initFragment(Fragment f){  
        changeFragment(f, true);  
    }  
    private void changeFragment(Fragment f, boolean init){  
        FragmentTransaction ft = fm.beginTransaction();  
        ft.replace(R.id.simple_fragment, f);  
        if(!init)  {
            ft.addToBackStack(null);
            back = back+1;
        }
        ft.commitAllowingStateLoss();  
    	}
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode==1 || requestCode==0){
            	
            	PaintingActivity myFragment = (PaintingActivity) getSupportFragmentManager().findFragmentById(R.id.simple_fragment);
            	myFragment.handleActivityResult(requestCode, resultCode, data);
            	
                    //PaintingActivity activit =(PaintingActivity)getLocalActivityManager().getCurrentActivity();
                    //activit.handleActivityResult(requestCode, resultCode, data);
            }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
            	//int backCount = getFragmentManager().getBackStackEntryCount();
            	//Log.i("back", "back 1 " + backCount);
            	if(back <=0){
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
            	}else{
            		super.onKeyDown(keyCode, event);
            		back = back-1;
            	}//backCount = getFragmentManager().getBackStackEntryCount();
                //Log.i("back", "back 2 " + backCount);
                break;
        }
        return true;
    }
    
	}
/*
public class ActivityGroupPaint extends ActivityGroup{
	
    public static ActivityGroupPaint groupPaint;

    private ArrayList<View> history;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.history = new ArrayList<View>();
        groupPaint = this;
        
        View view = getLocalActivityManager().startActivity("Activity1", 
                new Intent(ActivityGroupPaint.this, 
                           PaintingActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
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
            //finish();
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
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            Log.i("iamge","imageaaa request" + requestCode);
            if(requestCode==1 || requestCode==0){
                    PaintingActivity activit =(PaintingActivity)getLocalActivityManager().getCurrentActivity();
                    activit.handleActivityResult(requestCode, resultCode, data);
            }
    }
    
}
*/