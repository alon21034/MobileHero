package tw.edu.ntu.mobilehero;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class PicView extends Activity implements OnClickListener, OnTouchListener {

	ImageView edit;
	
	ImageView comic1;
	ImageView comic2;
	ImageView comic3;
	ImageView comic4;
	ImageView preview;
	ImageView number1;
	ImageView number2;
	ImageView number3;
	ImageView number4;
	//private int mEnd_x;
	//private int mEnd_y;
	private boolean isEditable;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picview);

        edit = (ImageView)findViewById(R.id.picview_edit);
        edit.setOnClickListener(this);
        
        isEditable = false;
        
        comic1 = (ImageView) findViewById(R.id.picview_pic1);
        comic1.setOnTouchListener(this);
        comic2 = (ImageView) findViewById(R.id.picview_pic2);
        comic2.setOnTouchListener(this);
        comic3 = (ImageView) findViewById(R.id.picview_pic3);
        comic3.setOnTouchListener(this);
        comic4 = (ImageView) findViewById(R.id.picview_pic4);
        comic4.setOnTouchListener(this);
        preview = (ImageView) findViewById(R.id.picview_preview);
        preview.setVisibility(4);
        number1 = (ImageView) findViewById(R.id.picview_number1);
        number2 = (ImageView) findViewById(R.id.picview_number2);
        number3 = (ImageView) findViewById(R.id.picview_number3);
        number4 = (ImageView) findViewById(R.id.picview_number4);
    }
    
    public void setAnimation(View v){
        TranslateAnimation translateAnimation = new TranslateAnimation(0,0,0,0);
        translateAnimation.setDuration(10);
        v.startAnimation(translateAnimation);
    }    
    
    public void setAnimation1(View v , int mEnd_x, int mEnd_y){
        Random random = new Random();
        final int end_x = (random.nextInt())%10;
        final int end_y = (random.nextInt())%10;
        TranslateAnimation translateAnimation = new TranslateAnimation(mEnd_x,end_x,mEnd_y, end_y);
        int flag = Math.max(Math.abs(end_x-mEnd_x), Math.abs(end_y-mEnd_y));
        mEnd_x = end_x;
        mEnd_y = end_y;
        
        translateAnimation.setAnimationListener(new AnimationListener() {
     
                    @Override
                    public void onAnimationStart(Animation animation) {
                            // TODO Auto-generated method stub
                    }
                    
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                            // TODO Auto-generated method stub
                    }
                    
                    @Override
                    public void onAnimationEnd(Animation animation) {
                            // TODO Auto-generated method stub
                    	setAnimation1(number1 ,end_x, end_y);
                    }
            });
        	translateAnimation.setDuration(getDuration(flag));
        	v.startAnimation(translateAnimation);
    }
    
    public void setAnimation2(View v , int mEnd_x, int mEnd_y){
        Random random = new Random();
        final int end_x = (random.nextInt())%10;
        final int end_y = (random.nextInt())%10;
        TranslateAnimation translateAnimation = new TranslateAnimation(mEnd_x,end_x,mEnd_y, end_y);
        int flag = Math.max(Math.abs(end_x-mEnd_x), Math.abs(end_y-mEnd_y));
        mEnd_x = end_x;
        mEnd_y = end_y;
        
        translateAnimation.setAnimationListener(new AnimationListener() {
     
                    @Override
                    public void onAnimationStart(Animation animation) {
                            // TODO Auto-generated method stub
                    }
                    
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                            // TODO Auto-generated method stub
                    }
                    
                    @Override
                    public void onAnimationEnd(Animation animation) {
                            // TODO Auto-generated method stub
                    	setAnimation2(number2 ,end_x, end_y);
                    }
            });
        	translateAnimation.setDuration(getDuration(flag));
        	v.startAnimation(translateAnimation);
    }    
    
    public void setAnimation3(View v , int mEnd_x, int mEnd_y){
        Random random = new Random();
        final int end_x = (random.nextInt())%10;
        final int end_y = (random.nextInt())%10;
        TranslateAnimation translateAnimation = new TranslateAnimation(mEnd_x,end_x,mEnd_y, end_y);
        int flag = Math.max(Math.abs(end_x-mEnd_x), Math.abs(end_y-mEnd_y));
        mEnd_x = end_x;
        mEnd_y = end_y;
        
        translateAnimation.setAnimationListener(new AnimationListener() {
     
                    @Override
                    public void onAnimationStart(Animation animation) {
                            // TODO Auto-generated method stub
                    }
                    
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                            // TODO Auto-generated method stub
                    }
                    
                    @Override
                    public void onAnimationEnd(Animation animation) {
                            // TODO Auto-generated method stub
                    	setAnimation3(number3 ,end_x, end_y);
                    }
            });
        	translateAnimation.setDuration(getDuration(flag));
        	v.startAnimation(translateAnimation);
    }    
    
    public void setAnimation4(View v , int mEnd_x, int mEnd_y){
        Random random = new Random();
        final int end_x = (random.nextInt())%10;
        final int end_y = (random.nextInt())%10;
        TranslateAnimation translateAnimation = new TranslateAnimation(mEnd_x,end_x,mEnd_y, end_y);
        int flag = Math.max(Math.abs(end_x-mEnd_x), Math.abs(end_y-mEnd_y));
        mEnd_x = end_x;
        mEnd_y = end_y;
        
        translateAnimation.setAnimationListener(new AnimationListener() {
     
                    @Override
                    public void onAnimationStart(Animation animation) {
                            // TODO Auto-generated method stub
                    }
                    
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                            // TODO Auto-generated method stub
                    }
                    
                    @Override
                    public void onAnimationEnd(Animation animation) {
                            // TODO Auto-generated method stub
                    	setAnimation4(number4 ,end_x, end_y);
                    }
            });
        	translateAnimation.setDuration(getDuration(flag));
        	v.startAnimation(translateAnimation);
    }
    
    
    public int getDuration(int flag){
        return flag*100;
    }    

    
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if ( arg0 == edit){
			if(isEditable == false){
				isEditable = true;
				//Animation shake1 = AnimationUtils.loadAnimation(this, R.anim.shake);
				setAnimation4(number4,0,0);
				setAnimation1(number1,0,0);
				setAnimation2(number2,0,0);
				setAnimation3(number3,0,0);
			}else {
				isEditable = false;
				setAnimation(number4);
				setAnimation(number1);
				setAnimation(number2);
				setAnimation(number3);
			}
		}
	}
	
	@Override 
	public boolean onTouchEvent(MotionEvent event)
	{
    	float x = event.getX();
    	float y = event.getY();
    	
    	Log.i("touch","touch " + x +" " +y);
    	
		return false;
    	
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		
		if(event.getAction() == MotionEvent.ACTION_UP){
			preview.setVisibility(4);
		}

		
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			if(isEditable == false){
				if (v == comic1){
					preview.setImageResource(R.drawable.androidpic);
					preview.setAlpha(200);
					preview.setVisibility(0);
				
				}else if (v == comic2){
					preview.setImageResource(R.drawable.androidpic);
					preview.setAlpha(200);
					preview.setVisibility(0);			
				}else if (v == comic3){
					preview.setImageResource(R.drawable.androidpic);
					preview.setAlpha(200);
					preview.setVisibility(0);			
				}else if (v == comic4){
					preview.setImageResource(R.drawable.androidpic);
					preview.setAlpha(200);
					preview.setVisibility(0);			
				}
			}else{
				
				setAnimation(number1);
				setAnimation(number2);
				setAnimation(number3);
				setAnimation(number4);
				if(v == comic3 || v == comic1 || v == comic2 || v == comic4){
					View view = ActivityGroupView.groupView.getLocalActivityManager().startActivity("Activity2", new Intent(PicView.this, Painting.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
                	ActivityGroupView.groupView.replaceView(view);
				}else if (v == edit){
					
				}
			}
		}
		
		return true;
	}

}
