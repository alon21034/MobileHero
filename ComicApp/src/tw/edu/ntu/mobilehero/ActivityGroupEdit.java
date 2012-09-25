package tw.edu.ntu.mobilehero;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class ActivityGroupEdit extends FragmentActivity {  
    public static FragmentManager fm;  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.fragment_paint);  
        fm = getSupportFragmentManager();    
        initFragment(new OpenFileActivity());
    }  

    public static void changeFragment(Fragment f){  
        changeFragment(f, false);  
    }  
    
    public void initFragment(Fragment f){  
        changeFragment(f, true);  
    }  
    private static void changeFragment(Fragment f, boolean init){  
        FragmentTransaction ft = fm.beginTransaction();  
        ft.replace(R.id.simple_fragment, f);  
        if(!init)  
            ft.addToBackStack(null);  
        ft.commitAllowingStateLoss();  
    	}
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode==1 || requestCode==0){
            	
            	PaintingActivity myFragment = (PaintingActivity) getSupportFragmentManager().findFragmentById(R.id.simple_fragment);
            	myFragment.handleActivityResult(requestCode, resultCode, data);

            }
    }
}
